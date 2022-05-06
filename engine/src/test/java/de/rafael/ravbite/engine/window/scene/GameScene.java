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
import de.rafael.ravbite.engine.graphics.components.mesh.MeshComponent;
import de.rafael.ravbite.engine.graphics.components.rendering.mesh.MeshRendererComponent;
import de.rafael.ravbite.engine.graphics.model.ModelUtils;
import de.rafael.ravbite.engine.graphics.object.game.GameObject;
import de.rafael.ravbite.engine.graphics.object.scene.Scene;
import de.rafael.ravbite.engine.graphics.window.EngineWindow;
import de.rafael.ravbite.engine.input.mouse.state.MouseState;
import de.rafael.ravbite.utils.asset.AssetLocation;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

public class GameScene extends Scene {

    public GameScene(EngineWindow engineWindow) {
        super(engineWindow, "Scene 1");
    }

    @Override
    public void prepare() {
        GameObject camera = new GameObject(this, "Camera 1");
        camera.appendComponent(new CameraComponent());
        getEngineWindow().getDebugWindow().addGameObject(camera);

        GameObject light = new GameObject(this, "Light");
        light.getTransform().move(0, 15, 0);
        light.appendComponent(new LightComponent(new Color(255, 255, 255)));
        getEngineWindow().getDebugWindow().addGameObject(light);

        GameObject swampBiome = new GameObject(this, "Site");
        swampBiome.getTransform().move(-1.5f, 0f, -2.5f);
        swampBiome.appendComponent(new MeshComponent(ModelUtils.rbLoadModel(AssetLocation.create("/models/viking-room.obj", AssetLocation.INTERNAL), getEngineWindow())));
        swampBiome.appendComponent(new MeshRendererComponent());

        super.storeObject(0, camera);
        getSceneObject().appendChildren(camera, light, swampBiome);
    }

    int state = 0;
    @Override
    public void update() {
        GameObject camera = (GameObject) super.getStoredObject(0);

        if(camera.getTransform().getRotation().y < -0.65) {
            state = 1;
        } else if(camera.getTransform().getRotation().y > 0.15) {
            state = 0;
        }

        if(state == 1) {
            camera.getTransform().rotate(0f, 0.025f, 0f); //  * Time.deltaTime
        } else {
            camera.getTransform().rotate(0f, -0.025f, 0f); //  * Time.deltaTime
        }

        super.update();
    }

    @Override
    public void dispose() {

    }

}