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

        GLFW.glfwSetKeyCallback(inputSystem.getWindow().getWindow(), (window, key, scancode, action, mods) -> {
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
        return GLFW.glfwGetKey(inputSystem.getWindow().getWindow(), key) == 1;
    }

    /**
     * @param key GLFW.GLFW_KEY_***
     * @return This method returns false if the key is pressed
     */
    public boolean keyUp(int key) {
        return GLFW.glfwGetKey(inputSystem.getWindow().getWindow(), key) == 0;
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
