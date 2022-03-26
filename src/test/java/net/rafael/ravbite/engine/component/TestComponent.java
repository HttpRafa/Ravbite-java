package net.rafael.ravbite.engine.component;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/25/2022 at 6:18 PM
// In the project Ravbite
//
//------------------------------

import net.rafael.ravbite.engine.graphics.components.Component;
import net.rafael.ravbite.engine.graphics.components.camera.CameraComponent;
import net.rafael.ravbite.engine.graphics.object.game.GameObject;

public class TestComponent extends Component {

    public TestComponent(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void render(CameraComponent cameraComponent) {

    }

}
