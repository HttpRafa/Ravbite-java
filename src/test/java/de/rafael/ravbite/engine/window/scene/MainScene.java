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
import de.rafael.ravbite.engine.graphics.components.light.LightComponent;
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
import java.util.Random;

public class MainScene extends Scene {

    public MainScene(EngineWindow engineWindow) {
        super(engineWindow, "Main Scene");
    }

    @Override
    public void prepare() {
        GameObject camera = new GameObject(this, "Camera 1");
        camera.appendComponent(new CameraComponent());

        GameObject light = new GameObject(this, "Light");
        light.getTransform().move(0, 20, 0);
        light.appendComponent(new LightComponent(new Color(255, 255, 255)));

        Mesh mesh = null;
        Material material = new Material(this.getEngineWindow());
        try {
            int textureId = getGLUtils().rbLoadTexture(AssetLocation.create("/textures/ground.png", AssetLocation.INTERNAL));
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

        Random random = new Random();
        for (int i = 0; i <= 101; i++) {
            GameObject testModel = new GameObject(this, "Test Model");
            if(i == 101) {
                testModel.getTransform().move(0, -1, -5);
            } else {
                testModel.getTransform().move(random.nextFloat() * 80 - 40, random.nextFloat() * 40 - 20, -60);
            }
            testModel.appendComponent(new TestComponent());
            testModel.appendComponent(new MeshComponent(mesh));
            testModel.appendComponent(new MeshRendererComponent());
            testModel.appendComponent(new MaterialComponent(material));
            getSceneObject().appendChildren(testModel);
            super.storeObject(2 + i, testModel);
        }

        super.storeObject(0, camera);
        super.storeObject(1, light);

        getSceneObject().appendChildren(camera, light);
    }

    @Override
    public void update() {
        GameObject camera = (GameObject) super.getStoredObject(0);
        GameObject light = (GameObject) super.getStoredObject(1);
        for (int i = 0; i < super.getStoredObjects().values().size(); i++) {
            if(i >= 2) {
                GameObject testModel = (GameObject) super.getStoredObject(i);
                testModel.getTransform().rotate(0, i == (2 + 101) ? 1 : ((i % 2) == 0) ? -1 : 1, 0);
            }
        }
        super.update();
    }

    @Override
    public void dispose() {

    }

}
