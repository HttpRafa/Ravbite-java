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
import de.rafael.ravbite.engine.input.InputSystem;

import java.io.IOException;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public abstract class EngineWindow extends Window {

    public static boolean DEBUG_MODE = false;

    private int currentScene = 0;
    private Scene[] scenes = new Scene[0];

    private AbstractShader[] abstractShaders = new AbstractShader[0];

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
        super(width, height);

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

        destroy();
    }

    public void initialize() {
        // Initialize debug
        if(DEBUG_MODE) {
            debugWindow = new DebugWindow(this);
        }

        super.initialize();

        // Setup inputSystem
        inputSystem = new InputSystem(this);

        // Load default assets
        try {
            this.defaultTexture = this.glUtils.rbStaticLoadTexture(AssetLocation.create("/textures/default/standard.png", AssetLocation.INTERNAL));
        } catch (IOException exception) {
            exception.printStackTrace();
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
    @Override
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
        while (!glfwWindowShouldClose(getWindow())) {
            long frameStart = System.currentTimeMillis();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            if(inputSystem != null) inputSystem.getMouse().update();
            render();

            glfwSwapBuffers(getWindow()); // swap the color buffers

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
                    glfwSetWindowTitle(getWindow(), "Ravbite Engine | " + (int) (1000 / frameTime) + " fps / " + frameTime + " ms / delta: " + deltaTime + " sec / running: " + (System.currentTimeMillis() - startTime) + " ms");
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
     * @return All scenes handled by the Engine
     */
    public Scene[] getScenes() {
        return scenes;
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
