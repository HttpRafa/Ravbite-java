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
import org.lwjgl.glfw.GLFW;

public class TestWindow extends EngineWindow {

    private final long startTime;

    public TestWindow() {
        super(1920/2, 1080/2);
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void prepare() {
        addScene(new MainScene(this));
    }

    @Override
    public void render() {
        super.render();
        if((this.startTime + (1000 * 10)) < System.currentTimeMillis()) {
            GLFW.glfwSetWindowShouldClose(this.getWindow(), true); // We will detect this in the rendering loop
        }
    }

}
