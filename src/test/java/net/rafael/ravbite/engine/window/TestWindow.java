package net.rafael.ravbite.engine.window;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/24/2022 at 9:45 PM
// In the project Ravbite
//
//------------------------------

import net.rafael.ravbite.engine.graphics.window.EngineWindow;
import net.rafael.ravbite.engine.scene.MainScene;

public class TestWindow extends EngineWindow {

    public TestWindow() {
        super(1920 / 2, 1080 / 2);
    }

    @Override
    public void prepare() {
        addScene(new MainScene(this));
    }

}
