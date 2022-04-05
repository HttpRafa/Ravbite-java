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
import de.rafael.ravbite.engine.input.mouse.Mouse;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class InputSystem {

    private final EngineWindow engineWindow;
    private final List<KeyCallback> callbacks = new ArrayList<>();

    private final Mouse mouse;

    public InputSystem(EngineWindow engineWindow) {
        this.engineWindow = engineWindow;

        mouse = new Mouse(this);
        mouse.addToggleKey(GLFW.GLFW_KEY_ESCAPE);

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
     * @param key GLFW.GLFW_KEY_***
     * @return This method returns true if the key is pressed
     */
    public boolean keyDown(int key) {
        return GLFW.glfwGetKey(engineWindow.getWindow(), key) == 1;
    }

    /**
     * @param key GLFW.GLFW_KEY_***
     * @return This method returns false if the key is pressed
     */
    public boolean keyUp(int key) {
        return GLFW.glfwGetKey(engineWindow.getWindow(), key) == 0;
    }

    /**
     * @return Mouse
     */
    public Mouse getMouse() {
        return mouse;
    }

    /**
     * @return EngineWindow
     */
    public EngineWindow getEngineWindow() {
        return engineWindow;
    }

}
