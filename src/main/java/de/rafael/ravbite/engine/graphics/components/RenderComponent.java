package de.rafael.ravbite.engine.graphics.components;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/31/2022 at 10:14 AM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.camera.CameraComponent;

public abstract class RenderComponent extends Component {

    /**
     * Called to render the GameObject
     */
    public abstract void render(CameraComponent cameraComponent);

}
