package net.rafael.ravbite.engine.scene;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/25/2022 at 3:35 PM
// In the project Ravbite
//
//------------------------------

import net.rafael.ravbite.engine.component.TestComponent;
import net.rafael.ravbite.engine.graphics.components.camera.CameraComponent;
import net.rafael.ravbite.engine.graphics.components.material.MaterialComponent;
import net.rafael.ravbite.engine.graphics.components.mesh.MeshComponent;
import net.rafael.ravbite.engine.graphics.components.mesh.MeshRendererComponent;
import net.rafael.ravbite.engine.graphics.material.Material;
import net.rafael.ravbite.engine.graphics.mesh.Mesh;
import net.rafael.ravbite.engine.graphics.object.game.GameObject;
import net.rafael.ravbite.engine.graphics.object.scene.Scene;
import net.rafael.ravbite.engine.graphics.window.EngineWindow;

public class MainScene extends Scene {

    public MainScene(EngineWindow engineWindow) {
        super(engineWindow, "Main Scene");
    }

    @Override
    public void prepare() {
        GameObject camera = new GameObject(this, "Camera 1");
        camera.appendComponent(new CameraComponent(camera));

        float[] vertices = {
                -0.5f, 0.5f, 0f,//v0
                -0.5f, -0.5f, 0f,//v1
                0.5f, -0.5f, 0f,//v2
                0.5f, 0.5f, 0f,//v3
        };

        int[] indices = {
                0,1,3,//top left triangle (v0, v1, v3)
                3,1,2//bottom right triangle (v3, v1, v2)
        };
        Mesh mesh = new Mesh(vertices, indices);

        GameObject testCube = new GameObject(this, "Test Cube");
        testCube.appendComponent(new TestComponent(testCube));
        testCube.appendComponent(new MeshComponent(testCube, mesh));
        testCube.appendComponent(new MeshRendererComponent(testCube));
        testCube.appendComponent(new MaterialComponent(testCube, new Material(this.getEngineWindow())));

        getSceneObject().appendChildren(camera, testCube);
    }

    @Override
    public void dispose() {

    }

}
