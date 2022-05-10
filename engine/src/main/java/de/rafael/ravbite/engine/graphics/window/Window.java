/*
 * Copyright (c) 2022.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *         this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *     * Neither the name of the developer nor the names of its contributors
 *         may be used to endorse or promote products derived from this software
 *         without specific prior written permission.
 *     * Redistributions in source or binary form must keep the original package
 *         and class name.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.rafael.ravbite.engine.graphics.window;

//------------------------------
//
// This class was developed by Rafael K.
// On 04/15/2022 at 1:18 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.utils.ImageUtils;
import de.rafael.ravbite.utils.asset.AssetLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class Window {

    private int initialWidth, initialHeight;
    private WindowSizeChangeCallback windowSizeChangeCallback = null;

    private long window;

    public Window(int initialWidth, int initialHeight) {
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;
    }

    /**
     * Initializes the GLFW window and shows it
     */
    public void initialize() {
        // Create the window
        window = glfwCreateWindow(initialWidth, initialHeight, "Ravbite Engine", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            assert videoMode != null;
            glfwSetWindowPos(window, (videoMode.width() - pWidth.get(0)) / 2, (videoMode.height() - pHeight.get(0)) / 2);
        } // the stack frame is popped automatically

        glfwSetWindowSizeCallback(window, (windowId, width, height) -> {
            if(windowSizeChangeCallback != null) windowSizeChangeCallback.change(width, height);
            GL11.glViewport(0, 0, width, height);
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        // Set default icon
        try {
            BufferedImage iconImage = ImageUtils.loadImage(new AssetLocation("/textures/icon/icon128.png", AssetLocation.INTERNAL));
            setIcon(iconImage);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    }

    public abstract void loop();

    public void destroy() {
        glfwFreeCallbacks(getWindow());
        glfwDestroyWindow(getWindow());

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    /**
     * Sets the icon of the window
     * @param bufferedImage Icon
     */
    public void setIcon(BufferedImage bufferedImage) {
        this.setIcons(new BufferedImage[] {bufferedImage});
    }

    /**
     * Sets the icons of the window
     * @param bufferedImages Array of icons
     */
    public void setIcons(BufferedImage[] bufferedImages) {
        GLFWImage[] glfwImages = ImageUtils.bufferedImageArrayToGLFWImageArray(bufferedImages);
        try(GLFWImage.Buffer iconBuffer = GLFWImage.malloc(glfwImages.length)) {
            for (int i = 0; i < glfwImages.length; i++) {
                iconBuffer.put(i, glfwImages[i]);
            }
            glfwSetWindowIcon(window, iconBuffer);
        }
    }

    /**
     * Sets the initialWidth
     * @param initialWidth initialWidth
     */
    public void setInitialWidth(int initialWidth) {
        this.initialWidth = initialWidth;
    }

    /**
     * @return The initial width of the window
     */
    public int getInitialWidth() {
        return initialWidth;
    }

    /**
     * Sets the initialHeight
     * @param initialHeight initialHeight
     */
    public void setInitialHeight(int initialHeight) {
        this.initialHeight = initialHeight;
    }

    /**
     * @return The initial height of the window
     */
    public int getInitialHeight() {
        return initialHeight;
    }

    /**
     * Sets the sizeChangeCallback
     * @param windowSizeChangeCallback Callback
     */
    public void windowSizeChangeCallback(WindowSizeChangeCallback windowSizeChangeCallback) {
        this.windowSizeChangeCallback = windowSizeChangeCallback;
    }

    /**
     * @return Size of the window in IntBuffers
     */
    public IntBuffer[] getWindowSize() {
        IntBuffer width = BufferUtils.createIntBuffer(4);
        IntBuffer height = BufferUtils.createIntBuffer(4);
        glfwGetWindowSize(window, width, height);
        return new IntBuffer[] {width, height};
    }

    /**
     * @return Width of the window
     */
    public int getWindowWidth() {
        return getWindowSize()[0].get();
    }

    /**
     * @return Height of the window
     */
    public int getWindowHeight() {
        return getWindowSize()[1].get();
    }

    /**
     * @return AspectRatio of the window
     */
    public float getWindowAspectRatio() {
        return ((float) getWindowWidth()) / ((float) getWindowHeight());
    }

    /**
     * @return WindowId of window created by GLFW
     */
    public long getWindow() {
        return window;
    }

}
