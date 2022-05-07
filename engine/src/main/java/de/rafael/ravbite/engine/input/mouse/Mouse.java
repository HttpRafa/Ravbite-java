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
package de.rafael.ravbite.engine.input.mouse;

//------------------------------
//
// This class was developed by Rafael K.
// On 4/2/2022 at 8:17 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.input.InputSystem;
import de.rafael.ravbite.engine.input.callbacks.KeyCallback;
import de.rafael.ravbite.engine.input.mouse.state.MouseState;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

public class Mouse {

    private final InputSystem inputSystem;

    private final List<Integer> toggleLockKeys = new ArrayList<>();
    private final MouseState[] mouseState = new MouseState[] {MouseState.UNLOCKED, null};

    private double mouseX, mouseY;
    private double deltaX, deltaY;

    public Mouse(InputSystem inputSystem) {
        this.inputSystem = inputSystem;

        this.inputSystem.getKeyboard().listenGlobal(new KeyCallback() {
            @Override
            public void pressed(int key, long window, int scancode, int action, int mods) {
                if(toggleLockKeys.contains(key)) {
                    if(mouseState[0] == MouseState.LOCKED || mouseState[0] == MouseState.LOCKED_HIDDEN) {
                        mouseState[1] = mouseState[0];
                        changeState(MouseState.UNLOCKED);
                    } else if (mouseState[1] != null) {
                        changeState(mouseState[1]);
                    }
                }
            }

            @Override
            public void released(int key, long window, int scancode, int action, int mods) {

            }
        });
    }

    public void update() {
        double windowWidth = getInputSystem().getEngineWindow().getWidth();
        double windowHeight = getInputSystem().getEngineWindow().getHeight();

        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

        GLFW.glfwGetCursorPos(this.inputSystem.getEngineWindow().getWindow(), x, y);
        x.rewind();
        y.rewind();

        this.mouseX = x.get();
        this.mouseY = y.get();

        if (mouseState[0] == MouseState.LOCKED || mouseState[0] == MouseState.LOCKED_HIDDEN) {
            // Calculate delta
            this.deltaX = this.mouseX - (windowWidth / 2);
            this.deltaY = this.mouseY - (windowHeight / 2);
            GLFW.glfwSetCursorPos(this.inputSystem.getEngineWindow().getWindow(), windowWidth / 2, windowHeight / 2);
        }
    }

    public void changeState(MouseState state) {
        this.mouseState[0] = state;
        if (this.mouseState[0] == MouseState.LOCKED_HIDDEN) {
            // Hide Cursor
            GLFW.glfwSetInputMode(this.inputSystem.getEngineWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        } else {
            // Show Cursor
            GLFW.glfwSetInputMode(this.inputSystem.getEngineWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        }
    }

    public void addToggleKey(int key) {
        this.toggleLockKeys.add(key);
    }

    public void removeToggleKey(int key) {
        this.toggleLockKeys.remove(key);
    }

    public float getMouseX() {
        return (float) mouseX;
    }

    public float getMouseY() {
        return (float) mouseY;
    }

    public float getDeltaX() {
        return (float) deltaX;
    }

    public float getDeltaY() {
        return (float) deltaY;
    }

    public MouseState getState() {
        return mouseState[0];
    }

    /**
     * @return InputSystem
     */
    public InputSystem getInputSystem() {
        return inputSystem;
    }

}
