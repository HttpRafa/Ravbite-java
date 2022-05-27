/*
 * Copyright (c) 2022. All rights reserved.
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
import de.rafael.ravbite.engine.graphics.components.mesh.MeshComponent;
import de.rafael.ravbite.engine.graphics.components.rendering.mesh.MeshRendererComponent;
import de.rafael.ravbite.engine.graphics.models.ModelUtils;
import de.rafael.ravbite.engine.graphics.objects.game.GameObject;
import de.rafael.ravbite.engine.graphics.objects.scene.Scene;
import de.rafael.ravbite.engine.graphics.window.EngineView;
import de.rafael.ravbite.engine.input.callbacks.KeyCallback;
import de.rafael.ravbite.engine.sound.components.AudioListener;
import de.rafael.ravbite.engine.sound.components.AudioSource;
import de.rafael.ravbite.engine.sound.utils.Sound;
import de.rafael.ravbite.utils.asset.AssetLocation;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class GameScene extends Scene {

    public GameScene(EngineView engineView) {
        super(engineView, "Scene 1");
    }

    @Override
    public void prepare() {
        Sound music = getEngineView().getUtils().alLoadSound(AssetLocation.create("/sounds/music.ogg", AssetLocation.INTERNAL));
        AudioSource musicBoxSource = new AudioSource().use(music).playOnInitialization();
        musicBoxSource.setPitch(1.2f);

        Sound sound = getEngineView().getUtils().alLoadSound(AssetLocation.create("/sounds/bounce.ogg", AssetLocation.INTERNAL));
        AudioSource audioSource = new AudioSource().use(sound);

        GameObject camera = new GameObject(this, "Camera 1");
        camera.appendComponent(new CameraComponent());
        camera.appendComponent(new AudioListener());
        camera.appendComponent(audioSource);
        getEngineView().getDebugWindow().addGameObject(camera);

        GameObject musicBox = new GameObject(this, "Music Box");
        musicBox.getTransform().position(0, 0, -2f);
        musicBox.appendComponent(musicBoxSource);
        musicBox.appendComponent(new MeshComponent(ModelUtils.rbLoadModel(AssetLocation.create("/models/cube.obj", AssetLocation.INTERNAL), getEngineView())));
        musicBox.appendComponent(new MeshRendererComponent());
        getEngineView().getDebugWindow().addGameObject(musicBox);

        GameObject light = new GameObject(this, "Light");
        light.getTransform().move(0, 2, 0);
        light.appendComponent(new LightComponent(new Color(255, 255, 255)));
        getEngineView().getDebugWindow().addGameObject(light);

        GameObject testModel = new GameObject(this, "Test Model");
        testModel.getTransform().move(-1.5f, 0f, -2.5f);
        testModel.appendComponent(new MeshComponent(ModelUtils.rbLoadModel(AssetLocation.create("/models/viking-room.obj", AssetLocation.INTERNAL), getEngineView())));
        testModel.appendComponent(new MeshRendererComponent());
        getEngineView().getDebugWindow().addGameObject(testModel);

        getInputSystem().getKeyboard().listen(new KeyCallback() {
            @Override
            public void pressed(int key, long window, int scancode, int action, int mods) {
                if(key == GLFW.GLFW_KEY_P) {
                    audioSource.play(1f, 2f);
                }
            }

            @Override
            public void released(int key, long window, int scancode, int action, int mods) {

            }
        });

        super.storeObject(0, camera);
        super.storeObject(1, musicBox);
        getSceneObject().appendChildren(camera, musicBox, light, testModel);
    }

    int state = 0;
    int musicState = 0;
    @Override
    public void update() {
        GameObject camera = (GameObject) super.getStoredObject(0);
        GameObject musicBox = (GameObject) super.getStoredObject(1);

        if(camera.getTransform().rotation.y < -0.65) {
            state = 1;
        } else if(camera.getTransform().rotation.y > 0.15) {
            state = 0;
        }

        if(state == 1) {
            camera.getTransform().rotate(0f, 0.025f, 0f); //  * Time.deltaTime
        } else {
            camera.getTransform().rotate(0f, -0.025f, 0f); //  * Time.deltaTime
        }

        if(musicBox.getTransform().position.x > 15f) {
            musicState = 0;
        } else if(musicBox.getTransform().position.x < -15f){
            musicState = 1;
        }

        if(musicState == 1) {
            musicBox.getTransform().move(new Vector3f(2f, 0, 0).mul(getEngineView().getDeltaTime()));
        } else {
            musicBox.getTransform().move(new Vector3f(-2f, 0, 0).mul(getEngineView().getDeltaTime()));
        }

        super.update();
    }

    @Override
    public void dispose() {

    }

}
