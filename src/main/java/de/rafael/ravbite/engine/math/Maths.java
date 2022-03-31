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

}
