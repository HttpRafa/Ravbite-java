package de.rafael.ravbite.engine.graphics.components.transform;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/25/2022 at 4:00 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.Component;
import de.rafael.ravbite.engine.math.Maths;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform extends Component {

    public static final int WORLD_SPACE = 0;
    public static final int OBJECT_SPACE = 1;

    private final Vector3f position;
    private final Vector3f scale;
    private final Quaternionf rotation;

    public Transform() {
        this.position = new Vector3f(0f, 0f, 0f);
        this.scale = new Vector3f(1f, 1f, 1f);
        this.rotation = new Quaternionf();
    }

    public Transform(Vector3f position, Vector3f scale, Quaternionf rotation) {
        this.position = position;
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

    public Transform rotation(float x, float y, float z, float w) {
        this.rotation.set(x, y, z, w);
        return this;
    }

    public Transform rotation(Vector3f vector) {
        this.rotation.mul(Maths.eulerToQuaternion(vector.x, vector.y, vector.z));
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
        this.position.add(transform.getPosition());
        this.rotation.add(transform.getRotation());
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
     * @return Position of the GameObject
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * @return Scale of the GameObject
     */
    public Vector3f getScale() {
        return scale;
    }

    /**
     * @return Rotation of the GameObject
     */
    public Quaternionf getRotation() {
        return rotation;
    }

    @Override
    public Transform clone() {
        return new Transform(new Vector3f(this.position), new Vector3f(this.scale), new Quaternionf(this.rotation));
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
