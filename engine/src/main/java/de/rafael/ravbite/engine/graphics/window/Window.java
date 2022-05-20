/*
 * Copyright (c) 2022. All rights reserved.
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
// On 05/10/2022 at 8:29 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.utils.ImageUtils;
import de.rafael.ravbite.engine.input.InputSystem;
import de.rafael.ravbite.utils.asset.AssetLocation;
import de.rafael.ravbite.utils.performance.TasksType;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class Window extends EngineView {

    private int initialWidth, initialHeight;

    private long window;

    public Window(int initialWidth, int initialHeight) {
        super(initialWidth, initialHeight);
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;
    }

    /**
     * Changes the scene
     * @param index Index of the new scene
     * @return Index of the old scene
     */
    @Override
    public int changeScene(int index) {
        super.getInputSystem().getKeyboard().delTempCallbacks();
        return super.changeScene(index);
    }

    /**
     * Initializes the window
     */
    @Override
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
            glViewport(0, 0, width, height);
            super.changeViewPortSize(width, height);
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

        super.inputSystem(new InputSystem(this));

        super.initialize();

        // Enable OpenGL Features
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        // Set the clear color
        glClearColor(220f/255f,220f/255f,220f/255f, 0.0f);
    }

    /**
     * Destroys the engine and the window
     */
    @Override
    public void destroy() {
        super.destroy();

        glfwFreeCallbacks(getWindow());
        glfwDestroyWindow(getWindow());

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    /**
     * Runs the window in a new thread
     */
    public void runThreaded() {
        Thread renderThread = new Thread(this::run);
        renderThread.start();
    }

    /**
     * Runs the window in the same thread
     */
    public void run() {
        this.initialize();
        this.loop();
        this.destroy();
    }

    /**
     * Starts the gameLoop
     */
    public void loop() {
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        final long[] nextDebugUpdate = {0};
        while(!glfwWindowShouldClose(getWindow())) {
            super.startFrame();

            super.getEngineWatcher().update(TasksType.LOOP_CLEAR_BUFFERS, () -> glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)); // clear the framebuffer
            if(super.getInputSystem() != null) super.getEngineWatcher().update(TasksType.LOOP_INPUT_UPDATE, () -> super.getInputSystem().getMouse().update());

            super.renderFrame();

            super.getEngineWatcher().update(TasksType.LOOP_SWAP_BUFFERS, () -> glfwSwapBuffers(getWindow())); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            super.getEngineWatcher().update(TasksType.LOOP_POLL_EVENTS, org.lwjgl.glfw.GLFW::glfwPollEvents);

            super.endFrame();

            super.getEngineWatcher().update(TasksType.LOOP_DEBUG_WINDOWS, () -> {
                if(super.getDebugWindow() != null) {
                    super.getDebugWindow().updateGameObjects();
                    super.getDebugWindow().updatePerformanceTable();
                }
                if(nextDebugUpdate[0] < System.currentTimeMillis()) {
                    nextDebugUpdate[0] = System.currentTimeMillis() + 500;
                    if(DEBUG_MODE) {
                        try {
                            glfwSetWindowTitle(getWindow(), "Ravbite Engine[" + super.getWidth() + "x" + super.getHeight() + "] | " + (int) (1000 / super.getFrameTime()) + " fps / " + super.getFrameTime() + " ms / delta: " + super.getDeltaTime() + " sec / running: " + (System.currentTimeMillis() - super.getStartTime()) + " ms");
                        } catch (ArithmeticException ignored) {

                        }
                    }
                }
            });
        }
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
     * Sets the windowTitle
     * @param title Title for the window
     */
    public void setTitle(String title) {
        glfwSetWindowTitle(window, title);
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

    /**
     * @return InputSystem for this window
     */
    public InputSystem getInputSystem() {
        return super.getInputSystem();
    }

}
