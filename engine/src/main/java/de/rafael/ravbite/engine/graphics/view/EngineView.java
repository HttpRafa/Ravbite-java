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

package de.rafael.ravbite.engine.graphics.view;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/10/2022 at 8:27 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.objects.scene.Scene;
import de.rafael.ravbite.engine.graphics.shader.AbstractShader;
import de.rafael.ravbite.engine.graphics.shader.standard.StandardShader;
import de.rafael.ravbite.engine.graphics.utils.DataWatcher;
import de.rafael.ravbite.engine.graphics.view.executor.ThreadExecutor;
import de.rafael.ravbite.engine.input.InputSystem;
import de.rafael.ravbite.engine.sound.SoundSystem;
import de.rafael.ravbite.engine.utils.RavbiteUtils;
import de.rafael.ravbite.engine.utils.debug.DebugWindow;
import de.rafael.ravbite.utils.asset.AssetLocation;
import de.rafael.ravbite.utils.performance.EngineWatcher;
import de.rafael.ravbite.utils.performance.TasksType;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.Arrays;

public abstract class EngineView {

    public static boolean DEBUG_MODE = false;

    private String glVersion = "";

    private int width;
    private int height;

    private int currentScene = 0;
    private Scene[] scenes = new Scene[0];

    private AbstractShader[] abstractShaders = new AbstractShader[0];

    private SoundSystem soundSystem;
    private InputSystem inputSystem;

    private DebugWindow debugWindow;
    private long startTime = 0;

    private long frameTime = 0;
    private float deltaTime = 0;

    private int defaultTexture = 0;

    private DataWatcher dataWatcher;

    private final EngineWatcher engineWatcher = new EngineWatcher();
    private final RavbiteUtils ravbiteUtils;
    private final ThreadExecutor threadExecutor;

    /**
     * WARNING: Not in the render thread
     * @param width Width of the window
     * @param height Height of the window
     */
    public EngineView(int width, int height) {
        this.width = width;
        this.height = height;

        this.ravbiteUtils = RavbiteUtils.use(this);
        this.threadExecutor = new ThreadExecutor();
    }

    /**
     * Sets the inputSystem
     * @param inputSystem InputSystem
     */
    public void inputSystem(InputSystem inputSystem) {
        this.inputSystem = inputSystem;
    }

    /**
     * Initializes the engine
     */
    public void initialize() {
        this.glVersion = GL11.glGetString(GL11.GL_VERSION);

        if(DEBUG_MODE) this.debugWindow = new DebugWindow(this);

        // Setup soundEngine
        this.soundSystem = new SoundSystem(this).initialize();

        // Load default assets
        try {
            this.defaultTexture = this.ravbiteUtils.rbStaticLoadTexture(AssetLocation.create("/textures/default/standard.png", AssetLocation.INTERNAL));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        prepareShaders();
        prepare();

        startTime = System.currentTimeMillis();

        // Trigger prepare method in scene
        changeScene(0);
    }

    private long frameStart;

    /**
     * Starts the frameRender process
     */
    public void startFrame() {
        frameStart = System.currentTimeMillis();

        // Start render to frameBuffer
    }

    /**
     * Renders the frame
     */
    public void renderFrame() {
        engineWatcher.update(TasksType.LOOP_SCENE_RENDER_ALL, this::render);

        // Stop render to frameBuffer

        // Postprocessing
    }

    /**
     * End of the frameRender process
     */
    public void endFrame() {
        // Execute all tasks stored in the stack
        engineWatcher.update(TasksType.LOOP_STACK_TASKS, threadExecutor::executeAllTasksInStack);

        long frameEnd = System.currentTimeMillis();
        frameTime = frameEnd - frameStart;
        deltaTime = frameTime / 1000f;
    }

    /**
     * Destroys the engine
     */
    public void destroy() {
        dataWatcher.rbCleanUp();
        for (AbstractShader abstractShader : abstractShaders) {
            abstractShader.dispose();
        }

        this.soundSystem.destroy();
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
        if(dataWatcher != null) dataWatcher.rbCleanUp();

        // Prepare a new scene
        dataWatcher = new DataWatcher();
        currentScene = index;
        scenes[currentScene].prepare();
        return old;
    }

    /**
     * Called to change the viewPortSize
     * @param width New width
     * @param height New height
     */
    public void changeViewPortSize(int width, int height) {
        this.width = width;
        this.height = height;

        scenes[currentScene].sizeChanged();
    }

    /**
     * @return OpenGL version used
     */
    public String getGlVersion() {
        return glVersion;
    }

    /**
     * @return Width of the viewPort
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return Height of the viewPort
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return AspectRatio of the viewPort
     */
    public float getAspectRatio() {
        return ((float) width) / ((float) height);
    }

    /**
     * @return All scenes handled by the Engine
     */
    public Scene[] getScenes() {
        return scenes;
    }

    /**
     * @return SoundSystem for this window
     */
    public SoundSystem getSoundSystem() {
        return soundSystem;
    }

    /**
     * @return InputSystem if you use standard Window.java
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
     * @return EngineWatcher that records every task performance
     */
    public EngineWatcher getEngineWatcher() {
        return engineWatcher;
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
     * @return Utils instance
     */
    public RavbiteUtils getUtils() {
        return ravbiteUtils;
    }

    /**
     * @return Executor to run tasks in the renderThread
     */
    public ThreadExecutor getThreadExecutor() {
        return threadExecutor;
    }

}
