package de.rafael.ravbite.engine.sound.components;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/07/2022 at 3:32 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.Component;
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
     * Updates the position and velocity of the listener
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
