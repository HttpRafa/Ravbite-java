package net.rafael.ravbite.engine.graphics.components.transform;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/25/2022 at 4:00 PM
// In the project Ravbite
//
//------------------------------

import net.rafael.ravbite.engine.graphics.components.Component;
import net.rafael.ravbite.engine.graphics.object.game.GameObject;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class TransformComponent extends Component {

    private final Vector3f position;
    private final Vector3f scale;
    private final Quat4f rotation;

    public TransformComponent(GameObject gameObject) {
        super(gameObject);
        this.position = new Vector3f(0f, 0f, 0f);
        this.scale = new Vector3f(1f, 1f, 1f);
        this.rotation = new Quat4f();
    }

    public TransformComponent position(float x, float y, float z) {
        this.position.set(x, y, z);
        return this;
    }

    public TransformComponent position(Vector3f vector) {
        this.position.set(vector);
        return this;
    }

    public TransformComponent rotation(float x, float y, float z, float w) {
        this.rotation.set(x, y, z, w);
        return this;
    }

    public TransformComponent rotation(float x, float y, float z) {
        this.rotation.set(x, y, z, 0f);
        return this;
    }

    public TransformComponent rotation(Quat4f quaternion) {
        this.rotation.set(quaternion);
        return this;
    }

    public TransformComponent scale(float x, float y, float z) {
        this.scale.set(x, y, z);
        return this;
    }

    public TransformComponent scale(Vector3f vector) {
        this.scale.set(vector);
        return this;
    }

    public TransformComponent scale(float s) {
        this.scale.set(s, s, s);
        return this;
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
    public Quat4f getRotation() {
        return rotation;
    }

}
