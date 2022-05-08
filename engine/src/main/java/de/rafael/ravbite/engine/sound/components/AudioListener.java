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
import org.joml.Vector3f;
import org.lwjgl.openal.AL10;

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

        AL10.alListener3f(AL10.AL_POSITION, position.x, position.y, position.z);
        AL10.alListener3f(AL10.AL_VELOCITY, velocity.x, velocity.y, velocity.z);
    }

}
