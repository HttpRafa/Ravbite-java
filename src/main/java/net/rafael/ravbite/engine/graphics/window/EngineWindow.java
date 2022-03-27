package net.rafael.ravbite.engine.graphics.window;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/23/2022 at 5:55 PM
// In the project Ravbite
//
//------------------------------

import net.rafael.ravbite.engine.graphics.object.scene.Scene;
import net.rafael.ravbite.engine.graphics.shader.Shader;
import net.rafael.ravbite.engine.graphics.shader.standard.StandardShader;
import net.rafael.ravbite.engine.graphics.utils.GLUtils;
import net.rafael.ravbite.engine.graphics.utils.DataWatcher;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public abstract class EngineWindow {

    private final int initialWidth, initialHeight;

    private int currentScene = 0;
    private Scene[] scenes = new Scene[0];

    private Shader[] shaders = new Shader[0];

    private long window;

    private DataWatcher dataWatcher;
    private final GLUtils glUtils;

    /**
     * WARNING: Not in the render thread
     * @param width Width of the window
     * @param height Height of the window
     */
    public EngineWindow(int width, int height) {
        initialHeight = height;
        initialWidth = width;

        glUtils = new GLUtils(this);
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
        for (Shader shader : shaders) {
            shader.dispose();
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
        // Set up an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this
        if ( !glfwInit() )
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

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
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
     * @param shader Shader to add
     * @return ID of the added shader
     */
    public int addShader(Shader shader) {
        shaders = Arrays.copyOf(shaders, shaders.length + 1);
        shaders[shaders.length - 1] = shader;
        shader.setShaderId(shaders.length - 1);
        return shaders.length - 1;
    }

    /**
     * @param type Type of the shader like StandardShader.class
     * @return ID of the shader or null
     */
    public Integer getIdOfShader(Class<? extends Shader> type) {
        for (Shader shader : shaders) {
            if(type.isAssignableFrom(shader.getClass())) {
                return shader.getShaderId();
            }
        }
        return null;
    }

    /**
     * @param type Type of the shader like StandardShader.class
     * @return Shader or null
     */
    public Shader getShaderOfType(Class<? extends Shader> type) {
        for (Shader shader : shaders) {
            if(type.isAssignableFrom(shader.getClass())) {
                return shader;
            }
        }
        return null;
    }

    /**
     * @param id ID of the shader
     * @return Shader instance
     */
    public Shader getShader(int id) {
        return shaders[id];
    }

    /**
     * Tell the engine to change the rendered scene
     * @param index Index of the new scene
     * @return Index of the old scene
     */
    public int changeScene(int index) {
        int old = currentScene;
        scenes[currentScene].dispose();
        if(dataWatcher != null) dataWatcher.rbCleanUp();
        dataWatcher = new DataWatcher();
        currentScene = index;
        scenes[currentScene].prepare();
        return old;
    }

    /**
     * Starts the render loop
     */
    public void loop() {
        // Trigger prepare method in scene
        changeScene(0);

        // Set the clear color
        glClearColor(220f/255f,220f/255f,220f/255f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            render();

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
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
     * @return The initial height of the window
     */
    public int getInitialHeight() {
        return initialHeight;
    }

    /**
     * @return The initial width of the window
     */
    public int getInitialWidth() {
        return initialWidth;
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

}
