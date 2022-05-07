package de.rafael.ravbite.engine.input.keyboard;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/07/2022 at 5:12 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.input.InputSystem;
import de.rafael.ravbite.engine.input.callbacks.KeyCallback;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {

    private final InputSystem inputSystem;

    private final List<KeyCallback> callbacks = new ArrayList<>();

    public Keyboard(InputSystem inputSystem) {
        this.inputSystem = inputSystem;

        GLFW.glfwSetKeyCallback(inputSystem.getEngineWindow().getWindow(), (window, key, scancode, action, mods) -> {
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
     * @param key GLFW.GLFW_KEY_***
     * @return This method returns true if the key is pressed
     */
    public boolean keyDown(int key) {
        return GLFW.glfwGetKey(inputSystem.getEngineWindow().getWindow(), key) == 1;
    }

    /**
     * @param key GLFW.GLFW_KEY_***
     * @return This method returns false if the key is pressed
     */
    public boolean keyUp(int key) {
        return GLFW.glfwGetKey(inputSystem.getEngineWindow().getWindow(), key) == 0;
    }

    /**
     * Like in Unity: D = 1 A = -1
     * @return Horizontal direction
     */
    public float getHorizontal() {
        if(keyDown(GLFW.GLFW_KEY_D)) return -1;
        if(keyDown(GLFW.GLFW_KEY_A)) return 1;
        return 0;
    }

    /**
     * Like in Unity: W = 1 S = -1
     * @return Vertical direction
     */
    public float getVertical() {
        if(keyDown(GLFW.GLFW_KEY_W)) return 1;
        if(keyDown(GLFW.GLFW_KEY_S)) return -1;
        return 0;
    }

    /**
     * @return InputSystem
     */
    public InputSystem getInputSystem() {
        return inputSystem;
    }

}
