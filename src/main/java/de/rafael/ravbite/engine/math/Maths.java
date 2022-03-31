package de.rafael.ravbite.engine.math;

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

public class Maths {

    /**
     * Creates transformationMatrix
     * @param transform Transform of the GameObject
     * @return transformationMatrix
     */
    public static Matrix4f createTransformationMatrix(Transform transform) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(transform.getPosition());
        matrix.scale(transform.getScale());
        matrix.rotate(transform.getRotation());
        return matrix;
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
        rotationDelta.rotationXYZ(newX, newY, newZ);

        return new Quaternionf().mul(rotationDelta);
    }

}
