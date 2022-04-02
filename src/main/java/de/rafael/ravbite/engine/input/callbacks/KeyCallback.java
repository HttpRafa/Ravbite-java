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
package de.rafael.ravbite.engine.input.callbacks;

//------------------------------
//
// This class was developed by Rafael K.
// On 4/2/2022 at 1:32 PM
// In the project Ravbite
//
//------------------------------

public abstract class KeyCallback {

    private final Integer key;
    private boolean global = false;

    public KeyCallback() {
        this.key = null;
    }

    /**
     * @param key GLFW.GLFW_KEY_***
     */
    public KeyCallback(Integer key) {
        this.key = key;
    }

    public abstract void pressed(int key, long window, int scancode, int action, int mods);
    public abstract void released(int key, long window, int scancode, int action, int mods);

    /**
     * @return Key to listen for
     */
    public Integer getKey() {
        return key;
    }

    /**
     * Sets the global value
     * @param global If true, this callback is not deleted when the scene is changed
     */
    public void setGlobal(boolean global) {
        this.global = global;
    }

    /**
     * @return If true, this callback is not deleted when the scene is changed
     */
    public boolean isGlobal() {
        return global;
    }

}
