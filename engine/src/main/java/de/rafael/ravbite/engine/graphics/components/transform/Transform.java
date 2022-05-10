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
package de.rafael.ravbite.engine.graphics.components.transform;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/25/2022 at 4:00 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.classes.Component;
import de.rafael.ravbite.engine.utils.math.Maths;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform extends Component {

    public static final int WORLD_SPACE = 0;
    public static final int OBJECT_SPACE = 1;

    public final Vector3f position;
    public final Vector3f velocity;

    public final Vector3f scale;
    public final Quaternionf rotation;

    public Transform() {
        this.position = new Vector3f(0f, 0f, 0f);
        this.velocity = new Vector3f(0f, 0f, 0f);
        this.scale = new Vector3f(1f, 1f, 1f);
        this.rotation = new Quaternionf();
    }

    public Transform(Vector3f position, Vector3f initializeVelocity, Vector3f scale, Quaternionf rotation) {
        this.position = position;
        this.velocity = initializeVelocity;
        this.scale = scale;
        this.rotation = rotation;
    }

    public Transform position(float x, float y, float z) {
        this.position.set(x, y, z);
        return this;
    }

    public Transform position(Vector3f vector) {
        this.position.set(vector);
        return this;
    }

    public Transform velocity(float x, float y, float z) {
        this.velocity.set(x, y, z);
        return this;
    }

    public Transform velocity(Vector3f vector) {
        this.velocity.set(vector);
        return this;
    }

    public Transform rotation(float x, float y, float z, float w) {
        this.rotation.set(x, y, z, w);
        return this;
    }

    public Transform rotation(Vector3f vector) {
        this.rotation.set(Maths.eulerToQuaternion(vector.x, vector.y, vector.z));
        return this;
    }

    public Transform rotation(Quaternionf quaternion) {
        this.rotation.set(quaternion);
        return this;
    }

    public Transform scale(float x, float y, float z) {
        this.scale.set(x, y, z);
        return this;
    }

    public Transform scale(Vector3f vector) {
        this.scale.set(vector);
        return this;
    }

    public Transform scale(float s) {
        this.scale.set(s, s, s);
        return this;
    }

    /**
     * Adds a transform to this transform
     * @param transform Transform to add
     */
    public void add(Transform transform) {
        this.position.add(transform.position);
        this.rotation.mul(transform.rotation);
    }

    /**
     * Moves the GameObject
     * @param x X value
     * @param y Y value
     * @param z Z value
     */
    public void move(float x, float y, float z) {
        this.position.add(x, y, z);
    }

    /**
     * Moves the GameObject
     * @param vector Vector to add
     */
    public void move(Vector3f vector) {
        this.move(vector.x, vector.y, vector.z);
    }

    /**
     * Rotates the GameObject using euler rotations
     * @param x X value
     * @param y Y value
     * @param z Z value
     */
    public void rotate(float x, float y, float z) {
        this.rotate(Maths.eulerToQuaternion(x, y, z));
    }

    /**
     * Rotates the GameObject
     * @param rotation Quaternion to add
     */
    public void rotate(Quaternionf rotation) {
        this.rotation.mul(rotation);
    }

    /**
     * Converts the transform into a viewMatrix
     * @return The viewMatrix
     */
    public Matrix4f viewMatrix() {
        return Maths.createViewMatrix(this);
    }

    @Override
    public Transform clone() {
        return new Transform(new Vector3f(this.position), new Vector3f(this.velocity), new Vector3f(this.scale), new Quaternionf(this.rotation));
    }

    @Override
    public String toString() {
        return "Transform{" +
                "position=" + position +
                ", scale=" + scale +
                ", rotation=" + rotation +
                '}';
    }

}
