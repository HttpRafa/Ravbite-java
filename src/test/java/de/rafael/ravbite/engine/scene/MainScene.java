package de.rafael.ravbite.engine.scene;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/25/2022 at 3:35 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.component.TestComponent;
import de.rafael.ravbite.engine.graphics.asset.AssetLocation;
import de.rafael.ravbite.engine.graphics.components.camera.CameraComponent;
import de.rafael.ravbite.engine.graphics.components.material.MaterialComponent;
import de.rafael.ravbite.engine.graphics.components.mesh.MeshComponent;
import de.rafael.ravbite.engine.graphics.components.mesh.MeshRendererComponent;
import de.rafael.ravbite.engine.graphics.material.AlbedoProperty;
import de.rafael.ravbite.engine.graphics.material.Material;
import de.rafael.ravbite.engine.graphics.mesh.Mesh;
import de.rafael.ravbite.engine.graphics.object.game.GameObject;
import de.rafael.ravbite.engine.graphics.object.scene.Scene;
import de.rafael.ravbite.engine.graphics.window.EngineWindow;

import java.awt.*;
import java.io.IOException;

public class MainScene extends Scene {

    public MainScene(EngineWindow engineWindow) {
        super(engineWindow, "Main Scene");
    }

    @Override
    public void prepare() {
        GameObject camera = new GameObject(this, "Camera 1");
        camera.appendComponent(new CameraComponent());

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

        float[] textureCoords = {
                0,0,
                0,1,
                1,1,
                1,0
        };

        Mesh mesh = new Mesh(vertices, textureCoords, indices);
        Material material = new Material(this.getEngineWindow());
        try {
            int textureId = getEngineWindow().getGLUtils().rbLoadTexture(AssetLocation.create("/testTexture.png", AssetLocation.INTERNAL));
            material.albedo(new AlbedoProperty(material, new Color(255, 255, 255)).texture(textureId));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        material.create();

        GameObject testCube = new GameObject(this, "Test Cube");
        testCube.getTransform().move(-1, 0, 0);
        testCube.appendComponent(new TestComponent());
        testCube.appendComponent(new MeshComponent(mesh));
        testCube.appendComponent(new MeshRendererComponent());
        testCube.appendComponent(new MaterialComponent(material));

        getSceneObject().appendChildren(camera, testCube);
    }

    @Override
    public void dispose() {

    }

}
