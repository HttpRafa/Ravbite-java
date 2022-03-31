package de.rafael.ravbite.engine.window.scene;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/25/2022 at 3:35 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.asset.AssetLocation;
import de.rafael.ravbite.engine.graphics.components.camera.CameraComponent;
import de.rafael.ravbite.engine.graphics.components.material.MaterialComponent;
import de.rafael.ravbite.engine.graphics.components.mesh.MeshComponent;
import de.rafael.ravbite.engine.graphics.components.mesh.MeshRendererComponent;
import de.rafael.ravbite.engine.graphics.material.AlbedoProperty;
import de.rafael.ravbite.engine.graphics.material.Material;
import de.rafael.ravbite.engine.graphics.mesh.Mesh;
import de.rafael.ravbite.engine.graphics.model.ModelUtils;
import de.rafael.ravbite.engine.graphics.object.game.GameObject;
import de.rafael.ravbite.engine.graphics.object.scene.Scene;
import de.rafael.ravbite.engine.graphics.window.EngineWindow;
import de.rafael.ravbite.engine.window.component.TestComponent;

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
                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,0.5f,-0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,-0.5f,0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                0.5f,0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,0.5f,-0.5f,
                -0.5f,-0.5f,-0.5f,
                -0.5f,-0.5f,0.5f,
                -0.5f,0.5f,0.5f,

                -0.5f,0.5f,0.5f,
                -0.5f,0.5f,-0.5f,
                0.5f,0.5f,-0.5f,
                0.5f,0.5f,0.5f,

                -0.5f,-0.5f,0.5f,
                -0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,-0.5f,
                0.5f,-0.5f,0.5f
        };

        float[] textureCoords = {
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0,
                0,0,
                0,1,
                1,1,
                1,0
        };

        int[] indices = {
                0,1,3,
                3,1,2,
                4,5,7,
                7,5,6,
                8,9,11,
                11,9,10,
                12,13,15,
                15,13,14,
                16,17,19,
                19,17,18,
                20,21,23,
                23,21,22
        };
        Mesh mesh = new Mesh(vertices, new float[0], new float[0], textureCoords, indices);
        Material material = new Material(this.getEngineWindow());
        try {
            int textureId = getGLUtils().rbLoadTexture(AssetLocation.create("/testTexture.png", AssetLocation.INTERNAL));
            material.albedo(new AlbedoProperty(material, new Color(255, 255, 255)).texture(textureId));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        material.create();

        try {
            mesh = ModelUtils.rbLoadMeshesFromModel(AssetLocation.create("/models/suzanne.obj", AssetLocation.INTERNAL))[0];
        } catch (IOException e) {
            e.printStackTrace();
        }

        GameObject testCube = new GameObject(this, "Test Cube");
        testCube.getTransform().move(0, -1, -10);
        testCube.appendComponent(new TestComponent());
        testCube.appendComponent(new MeshComponent(mesh));
        testCube.appendComponent(new MeshRendererComponent());
        testCube.appendComponent(new MaterialComponent(material));

        super.storeObject(0, camera);
        super.storeObject(1, testCube);

        getSceneObject().appendChildren(camera, testCube);
    }

    @Override
    public void update() {
        GameObject camera = (GameObject) super.getStoredObject(0);
        GameObject testCube = (GameObject) super.getStoredObject(1);
        testCube.getTransform().rotate(0, 1, 0);
        super.update();
    }

    @Override
    public void dispose() {

    }

}
