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
// On 3/23/2022 at 5:55 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.window.executor.ThreadExecutor;
import de.rafael.ravbite.engine.utils.debug.DebugWindow;
import de.rafael.ravbite.utils.asset.AssetLocation;
import de.rafael.ravbite.engine.graphics.object.scene.Scene;
import de.rafael.ravbite.engine.graphics.shader.AbstractShader;
import de.rafael.ravbite.engine.graphics.shader.standard.StandardShader;
import de.rafael.ravbite.engine.graphics.utils.DataWatcher;
import de.rafael.ravbite.engine.graphics.utils.GLUtils;
import de.rafael.ravbite.engine.graphics.utils.ImageUtils;
import de.rafael.ravbite.engine.input.InputSystem;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class EngineWindow {

    public static boolean DEBUG_MODE = false;

    private final int initialWidth, initialHeight;

    private int currentScene = 0;
    private Scene[] scenes = new Scene[0];

    private AbstractShader[] abstractShaders = new AbstractShader[0];

    private long window;
    private InputSystem inputSystem;

    private DebugWindow debugWindow = null;
    private long startTime = 0;

    private long frameTime = 0;
    private float deltaTime = 0;

    private int defaultTexture = 0;

    private DataWatcher dataWatcher;
    private final GLUtils glUtils;
    private final ThreadExecutor threadExecutor;

    /**
     * WARNING: Not in the render thread
     * @param width Width of the window
     * @param height Height of the window
     */
    public EngineWindow(int width, int height) {
        initialHeight = height;
        initialWidth = width;

        glUtils = new GLUtils(this);
        threadExecutor = new ThreadExecutor();
    }

    public void runThreaded() {
        Thread renderThread = new Thread(this::run);
        renderThread.start();
    }

    /**
     * Opens the window and starts the render loop
     */
    public void run() {
        initialize();
        prepareShaders();
        prepare();
        loop();

        dataWatcher.rbCleanUp();
        for (AbstractShader abstractShader : abstractShaders) {
            abstractShader.dispose();
        }

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    /**
     * Initializes the GLFW window and shows it
     */
    public void initialize() {
        // Initialize debug
        debugWindow = new DebugWindow(this);
        debugWindow.getFrame().setVisible(true);

        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

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

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        // Set default icon
        try {
            BufferedImage iconImage = ImageUtils.loadImage(new AssetLocation("/textures/icon/icon128.png", AssetLocation.INTERNAL));
            debugWindow.getFrame().setIconImage(iconImage);
            setIcon(iconImage);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // Setup inputSystem
        inputSystem = new InputSystem(this);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Load default assets
        try {
            this.defaultTexture = this.glUtils.rbStaticLoadTexture(AssetLocation.create("/textures/default/standard.png", AssetLocation.INTERNAL));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setIcon(BufferedImage bufferedImage) {
        this.setIcons(new BufferedImage[] {bufferedImage});
    }

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
     * Initializes all Scenes
     */
    public abstract void prepare();

    /**
     * Initializes all Shaders
     */
    public void prepareShaders() {
        addShader(new StandardShader(this));
    }

    /**
     * Tell the engine to handle a new scene
     * @param scene Scene to handle
     * @return Index of the added scene
     */
    public int addScene(Scene scene) {
        scenes = Arrays.copyOf(scenes, scenes.length + 1);
        scenes[scenes.length - 1] = scene;
        return scenes.length - 1;
    }

    /**
     * Tell the engine to add a new shader
     * @param abstractShader Shader to add
     * @return ID of the added shader
     */
    public int addShader(AbstractShader abstractShader) {
        abstractShaders = Arrays.copyOf(abstractShaders, abstractShaders.length + 1);
        abstractShaders[abstractShaders.length - 1] = abstractShader;
        abstractShader.setShaderId(abstractShaders.length - 1);
        return abstractShaders.length - 1;
    }

    /**
     * @param type Type of the shader like StandardShader.class
     * @return ID of the shader or null
     */
    public Integer getIdOfShader(Class<? extends AbstractShader> type) {
        for (AbstractShader abstractShader : abstractShaders) {
            if(type.isAssignableFrom(abstractShader.getClass())) {
                return abstractShader.getShaderId();
            }
        }
        return null;
    }

    /**
     * @param type Type of the shader like StandardShader.class
     * @return Shader or null
     */
    public AbstractShader getShaderOfType(Class<? extends AbstractShader> type) {
        for (AbstractShader abstractShader : abstractShaders) {
            if(type.isAssignableFrom(abstractShader.getClass())) {
                return abstractShader;
            }
        }
        return null;
    }

    /**
     * @param id ID of the shader
     * @return Shader instance
     */
    public AbstractShader getShader(int id) {
        return abstractShaders[id];
    }

    /**
     * Tell the engine to change the rendered scene
     * @param index Index of the new scene
     * @return Index of the old scene
     */
    public int changeScene(int index) {
        int old = currentScene;

        // Cleanup old scene
        scenes[currentScene].dispose();
        inputSystem.delTempCallbacks();
        if(dataWatcher != null) dataWatcher.rbCleanUp();

        // Prepare new scene
        dataWatcher = new DataWatcher();
        currentScene = index;
        scenes[currentScene].prepare();
        return old;
    }

    /**
     * Starts the render loop
     */
    public void loop() {
        startTime = System.currentTimeMillis();

        // Trigger prepare method in scene
        changeScene(0);

        // Enable OpenGL Features
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        // Set the clear color
        glClearColor(220f/255f,220f/255f,220f/255f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        long nextDebugUpdate = 0;
        while (!glfwWindowShouldClose(window)) {
            long frameStart = System.currentTimeMillis();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            if(inputSystem != null) inputSystem.getMouse().update();
            render();

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            // Execute all tasks stored in the stack
            threadExecutor.executeAllTasksInStack();

            long frameEnd = System.currentTimeMillis();
            frameTime = frameEnd - frameStart;
            deltaTime = frameTime / 1000f;

            if(debugWindow != null) debugWindow.updateGameObjects();
            if(nextDebugUpdate < System.currentTimeMillis()) {
                nextDebugUpdate = System.currentTimeMillis() + 500;
                if(DEBUG_MODE) {
                    glfwSetWindowTitle(window, "Ravbite Engine | " + (int) (1000 / frameTime) + " fps / " + frameTime + " ms / delta: " + deltaTime + " sec / running: " + (System.currentTimeMillis() - startTime) + " ms");
                }
            }
        }
    }

    /**
     * Called to render the frame
     */
    public void render() {
        scenes[currentScene].render();
        update();
    }

    /**
     * Called every frame
     */
    public void update() {
        scenes[currentScene].update();
    }

    /**
     * Called every fixed update(Physics)
     */
    public void fixedUpdate() {
        // TODO: Implement
    }

    /**
     * @return The initial width of the window
     */
    public int getInitialWidth() {
        return initialWidth;
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
    public int getWidth() {
        return getWindowSize()[0].get();
    }

    /**
     * @return Height of the window
     */
    public int getHeight() {
        return getWindowSize()[1].get();
    }

    /**
     * @return AspectRatio of the window
     */
    public float getAspectRatio() {
        return ((float) getWidth()) / ((float) getHeight());
    }

    /**
     * @return All scenes handled by the Engine
     */
    public Scene[] getScenes() {
        return scenes;
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
        return inputSystem;
    }

    /**
     * @return DebugWindow if debugMode is enabled
     */
    public DebugWindow getDebugWindow() {
        return debugWindow;
    }

    /**
     * @return Time were the window started to render
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @return Last renderTime
     */
    public long getFrameTime() {
        return frameTime;
    }

    /**
     * @return Last renderTime in seconds
     */
    public float getDeltaTime() {
        return deltaTime;
    }

    /**
     * @return ID of defaultTexture
     */
    public int getDefaultTexture() {
        return defaultTexture;
    }

    /**
     * @return DataWatcher
     */
    public DataWatcher getDataWatcher() {
        return dataWatcher;
    }

    /**
     * @return Utils class for openGL
     */
    public GLUtils getGLUtils() {
        return glUtils;
    }

    /**
     * @return Executor to run tasks in the renderThread
     */
    public ThreadExecutor getThreadExecutor() {
        return threadExecutor;
    }
}
