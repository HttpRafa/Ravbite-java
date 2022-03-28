package net.rafael.ravbite.engine.graphics.components;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/25/2022 at 3:39 PM
// In the project Ravbite
//
//------------------------------

import net.rafael.ravbite.engine.graphics.components.camera.CameraComponent;
import net.rafael.ravbite.engine.graphics.object.game.GameObject;

public abstract class Component {

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

    /**
     * Called when the component gets added to a GameObject
     */
    public void initialize() {

    }

    /**
     * Called every frame
     */
    public void update() {}

    /**
     * Called every fixed update(Physics)
     */
    public void fixedUpdate() {}

    /**
     * Called to render the GameObject
     */
    public void render(CameraComponent cameraComponent) {}

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
