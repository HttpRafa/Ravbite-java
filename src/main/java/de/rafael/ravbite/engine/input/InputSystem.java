package de.rafael.ravbite.engine.input;

//------------------------------
//
// This class was developed by Rafael K.
// On 4/2/2022 at 1:32 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.window.EngineWindow;
import de.rafael.ravbite.engine.input.callbacks.KeyCallback;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class InputSystem {

    private final EngineWindow engineWindow;
    private final List<KeyCallback> callbacks = new ArrayList<>();

    public InputSystem(EngineWindow engineWindow) {
        this.engineWindow = engineWindow;

        GLFW.glfwSetKeyCallback(engineWindow.getWindow(), (window, key, scancode, action, mods) -> {
            for (KeyCallback callback : callbacks) {
                if(callback.getKey() == null || callback.getKey() == key) {
                    if(action == GLFW.GLFW_RELEASE) {
                        callback.released(key, window, scancode, action, mods);
                    } else if(action == GLFW.GLFW_PRESS) {
                        callback.pressed(key, window, scancode, action, mods);
                    }
                }
            }
        });
    }

    /**
     * Adds a key callback / This callback is not deleted when the scene is changed
     * @param keyCallback Key callback
     */
    public void listenGlobal(KeyCallback keyCallback) {
        keyCallback.setGlobal(true);
        callbacks.add(keyCallback);
    }

    /**
     * Adds a key callback / This callback is doing to be deleted when the scene is changed
     * @param keyCallback Key callback
     */
    public void listen(KeyCallback keyCallback) {
        keyCallback.setGlobal(false);
        callbacks.add(keyCallback);
    }

    /**
     * Removes a key callback
     * @param keyCallback Key callback
     */
    public void stopListening(KeyCallback keyCallback) {
        callbacks.remove(keyCallback);
    }

    /**
     * Deletes all key callbacks with the globalValue of false
     */
    public void delTempCallbacks() {
        callbacks.removeIf(keyCallback -> !keyCallback.isGlobal());
    }

    /**
     *
     * @param key GLFW.GLFW_KEY_***
     * @return This method returns true if the key is pressed
     */
    public boolean keyDown(int key) {
        return GLFW.glfwGetKey(engineWindow.getWindow(), key) == 1;
    }

    /**
     *
     * @param key GLFW.GLFW_KEY_***
     * @return This method returns false if the key is pressed
     */
    public boolean keyUp(int key) {
        return GLFW.glfwGetKey(engineWindow.getWindow(), key) == 0;
    }

    /**
     * @return EngineWindow
     */
    public EngineWindow getEngineWindow() {
        return engineWindow;
    }

}
