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

package de.rafael.ravbite.engine.sound.components;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/07/2022 at 3:32 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.classes.Component;
import de.rafael.ravbite.engine.graphics.components.transform.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import java.nio.FloatBuffer;

public class AudioListener extends Component {

    @Override
    public void initialize() {
        updateListener();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void update() {
        updateListener();
    }

    /**
     * Updates the position, rotation and velocity of the listener
     */
    private void updateListener() {
        Transform transform = getGameObject().getTransform();
        Vector3f position = transform.position;
        Vector3f velocity = transform.velocity;

        Matrix4f viewMatrix = transform.viewMatrix();
        FloatBuffer listenerOrientation = BufferUtils.createFloatBuffer(6 * 4);
        listenerOrientation.put(0, viewMatrix.m01());
        listenerOrientation.put(1, viewMatrix.m02());
        listenerOrientation.put(2, viewMatrix.m03());
        listenerOrientation.put(3, viewMatrix.m11());
        listenerOrientation.put(4, viewMatrix.m12());
        listenerOrientation.put(5, viewMatrix.m13());

        AL10.alListener3f(AL10.AL_POSITION, position.x, position.y, position.z);
        AL10.alListenerfv(AL10.AL_ORIENTATION, listenerOrientation);
        AL10.alListener3f(AL10.AL_VELOCITY, velocity.x, velocity.y, velocity.z);
    }

}
