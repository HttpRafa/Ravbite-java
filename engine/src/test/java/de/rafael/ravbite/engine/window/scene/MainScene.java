/*
 * Copyright (c) 2022.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *         this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *     * Neither the name of the developer nor the names of its contributors
 *         may be used to endorse or promote products derived from this software
 *         without specific prior written permission.
 *     * Redistributions in source or binary form must keep the original package
 *         and class name.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.rafael.ravbite.engine.window.scene;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/25/2022 at 3:35 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.camera.CameraComponent;
import de.rafael.ravbite.engine.graphics.components.light.LightComponent;
import de.rafael.ravbite.engine.graphics.components.material.MaterialComponent;
import de.rafael.ravbite.engine.graphics.components.mesh.MeshComponent;
import de.rafael.ravbite.engine.graphics.components.rendering.mesh.MeshRendererComponent;
import de.rafael.ravbite.engine.graphics.object.game.material.standard.AlbedoProperty;
import de.rafael.ravbite.engine.graphics.object.game.material.standard.Material;
import de.rafael.ravbite.engine.graphics.object.game.mesh.Mesh;
import de.rafael.ravbite.engine.graphics.model.ModelUtils;
import de.rafael.ravbite.engine.graphics.object.game.GameObject;
import de.rafael.ravbite.engine.graphics.object.scene.Scene;
import de.rafael.ravbite.engine.graphics.window.EngineWindow;
import de.rafael.ravbite.engine.input.callbacks.KeyCallback;
import de.rafael.ravbite.engine.input.mouse.state.MouseState;
import de.rafael.ravbite.engine.window.component.TestComponent;
import de.rafael.ravbite.utils.asset.AssetLocation;
import org.lwjgl.glfw.GLFW;

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
        getEngineWindow().getDebugWindow().addGameObject(camera);

        getInputSystem().listen(new KeyCallback() {
            @Override
            public void pressed(int key, long window, int scancode, int action, int mods) {
                if(key == GLFW.GLFW_KEY_UP) {
                    camera.getTransform().move(0, 0, -0.5f);
                }
                if(key == GLFW.GLFW_KEY_DOWN) {
                    camera.getTransform().move(0, 0, 0.5f);
                }
            }

            @Override
            public void released(int key, long window, int scancode, int action, int mods) {

            }
        });

        GameObject light = new GameObject(this, "Light");
        light.getTransform().move(0, 15, 0);
        light.appendComponent(new LightComponent(new Color(255, 255, 255)));
        getEngineWindow().getDebugWindow().addGameObject(light);

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
            Mesh[] meshes = ModelUtils.rbLoadMeshesFromModel(AssetLocation.create("/models/chair.obj", AssetLocation.INTERNAL));
            System.out.println("Meshes: " + meshes.length);
            mesh = meshes[0];
        } catch (IOException e) {
            e.printStackTrace();
        }

        GameObject testModel = new GameObject(this, "Test Model");
        testModel.getTransform().move(0, -1, -10);
        testModel.getTransform().scale(2f);
        testModel.appendComponent(new TestComponent());
        testModel.appendComponent(new MeshComponent(mesh));
        testModel.appendComponent(new MeshRendererComponent());
        testModel.appendComponent(new MaterialComponent(material));
        getEngineWindow().getDebugWindow().addGameObject(testModel);
        getSceneObject().appendChildren(testModel);

        super.storeObject(2, testModel);

        /*Random random = new Random();
        for (int i = 0; i <= 101; i++) {
            GameObject testModel = new GameObject(this, "Test Model");
            if(i == 101) {
                testModel.getTransform().move(0, -1, -5);
                getEngineWindow().getDebugWindow().addGameObject(testModel);
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
         */

        super.storeObject(0, camera);
        super.storeObject(1, light);

        getSceneObject().appendChildren(camera, light);
    }

    @Override
    public void update() {
        GameObject camera = (GameObject) super.getStoredObject(0);
        GameObject light = (GameObject) super.getStoredObject(1);
        GameObject testModel = (GameObject) super.getStoredObject(2);

        testModel.getTransform().rotate(0f, 0.25f, 0f);
        /*for (int i = 0; i < super.getStoredObjects().values().size(); i++) {
            if(i >= 2) {
                GameObject testModel = (GameObject) super.getStoredObject(i);
                testModel.getTransform().rotate(0f, i == (2 + 101) ? 1 : ((i % 2) == 0) ? -1 : 1, 0f);
            }
        }*/

        // Move camera
        if(getInputSystem().keyDown(GLFW.GLFW_KEY_W)) {
            camera.getTransform().move(0, 0, -0.1f);
        }
        if(getInputSystem().keyDown(GLFW.GLFW_KEY_S)) {
            camera.getTransform().move(0, 0, 0.1f);
        }
        if(getInputSystem().keyDown(GLFW.GLFW_KEY_D)) {
            camera.getTransform().move(0.1f, 0, 0);
        }
        if(getInputSystem().keyDown(GLFW.GLFW_KEY_A)) {
            camera.getTransform().move(-0.1f, 0, 0);
        }
        if(getInputSystem().keyDown(GLFW.GLFW_KEY_SPACE)) {
            camera.getTransform().move(0, 0.1f, 0);
        }
        if(getInputSystem().keyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            camera.getTransform().move(0, -0.1f, 0);
        }

        if(getInputSystem().keyDown(GLFW.GLFW_KEY_O)) {
            getInputSystem().getMouse().changeState(MouseState.LOCKED_HIDDEN);
        }

        camera.getTransform().rotate((float) getInputSystem().getMouse().getDeltaY() * 0.1f, (float) getInputSystem().getMouse().getDeltaX() * 0.1f, 0f); //  * Time.deltaTime

        super.update();
    }

    @Override
    public void dispose() {

    }

}
