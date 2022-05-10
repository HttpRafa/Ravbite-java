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
package de.rafael.ravbite.engine.utils.math;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/31/2022 at 10:23 AM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.transform.Transform;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Maths {

    /**
     * Creates transformationMatrix
     * @param transform Transform of the GameObject
     * @return transformationMatrix
     */
    public static Matrix4f createTransformationMatrix(Transform transform) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(transform.position);
        matrix.scale(transform.scale);
        matrix.rotate(transform.rotation);
        return matrix;
    }

    /**
     * Creates viewMatrix
     * @param transform Transform
     * @return ViewMatrix
     */
    public static Matrix4f createViewMatrix(Transform transform) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotate(transform.rotation);
        Vector3f position = transform.position;
        viewMatrix.translate(new Vector3f(-position.x, -position.y, -position.z));
        return viewMatrix;
    }

    /**
     * Euler rotation to quaternion
     * @param x X value
     * @param y Y value
     * @param z Z value
     * @return Quaternion
     */
    public static Quaternionf eulerToQuaternion(float x, float y, float z) {
        //Use modulus to fix values to below 360 then convert values to radians
        float newX = (float) Math.toRadians(x % 360);
        float newY = (float) Math.toRadians(y % 360);
        float newZ = (float) Math.toRadians(z % 360);

        //Create a quaternion with the delta rotation values
        Quaternionf rotationDelta = new Quaternionf();
        rotationDelta.rotateX(newX);
        rotationDelta.rotateY(newY);
        rotationDelta.rotateZ(newZ);

        return rotationDelta;
    }

}
