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
package de.rafael.ravbite.engine.graphics.components.classes;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/25/2022 at 3:39 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.object.game.GameObject;
import de.rafael.ravbite.utils.gui.IGui;

public abstract class Component implements IGui {

    private GameObject gameObject;
    private boolean enabled = true;

    public Component() {

    }

    public Component(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public Component(GameObject gameObject, boolean enabled) {
        this.gameObject = gameObject;
        this.enabled = enabled;
    }

    @Override
    public void gui() {
    }

    /**
     * Called when the component gets added to a GameObject
     */
    public void initialize() {

    }

    /**
     * Called when the component gets removed from a GameObject
     */
    // TODO: If components can be removed
    public void dispose() {

    }

    /**
     * Called everytime a value is changed in the editor
     */
    public void valueUpdate(String fieldName) {}

    /**
     * Called every frame
     */
    public void update() {}

    /**
     * Called every fixed update(Physics)
     */
    public void fixedUpdate() {}

    /**
     * @return GameObject of the component
     */
    public GameObject getGameObject() {
        return gameObject;
    }

    /**
     * Sets to parent GameObject
     * @param gameObject GameObject
     */
    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    /**
     * @return If the component is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled state
     * @param enabled New State
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
