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
package de.rafael.ravbite.engine.graphics.object.scene;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/22/2022 at 5:13 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.Component;
import de.rafael.ravbite.engine.graphics.components.camera.CameraComponent;
import de.rafael.ravbite.engine.graphics.components.light.LightComponent;
import de.rafael.ravbite.engine.graphics.components.transform.Transform;
import de.rafael.ravbite.engine.graphics.object.game.GameObject;
import de.rafael.ravbite.engine.graphics.utils.GLUtils;
import de.rafael.ravbite.engine.graphics.window.EngineWindow;
import de.rafael.ravbite.engine.input.InputSystem;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public abstract class Scene {

    private final EngineWindow engineWindow;
    private final String name;

    private final GameObject sceneObject;

    private final HashMap<Integer, Object> storedObjects = new HashMap<>();

    public Scene(EngineWindow engineWindow, String name) {
        this.engineWindow = engineWindow;
        this.name = name;

        this.sceneObject = new GameObject(this, "Scene");
    }

    /**
     * Initializes all GameObjects
     */
    public abstract void prepare();

    /**
     * Delete all GameObjects
     */
    public abstract void dispose();

    public void storeObject(int slot, Object object) {
        storedObjects.put(slot, object);
    }

    public Object getStoredObject(int slot) {
        return storedObjects.getOrDefault(slot, null);
    }

    public HashMap<Integer, Object> getStoredObjects() {
        return storedObjects;
    }

    public void deleteStoredObject(int slot) {
        storedObjects.remove(slot);
    }

    /**
     * Called to render the frame
     */
    public void render() {
        Collection<GameObject> gameObjects = getGameObjects();
        // TODO: Optimize
        for (GameObject gameObject : gameObjects) {
            Optional<Component> componentOptional = gameObject.hasComponent(CameraComponent.class);
            if(componentOptional.isPresent()) {
                CameraComponent cameraComponent = (CameraComponent) componentOptional.get();
                cameraComponent.startRendering();
                renderCamera(gameObjects, cameraComponent);
                cameraComponent.stopRendering();
            }
        }
    }

    /**
     * Called to render the frame in the camera
     * @param cameraComponent Camera
     */
    private void renderCamera(Collection<GameObject> gameObjects, CameraComponent cameraComponent) {
        for (GameObject gameObject : gameObjects) {
            if(cameraComponent.isResponsableFor(gameObject)) {
                gameObject.render(cameraComponent);
            }
        }
    }

    /**
     * Called every frame
     */
    public void update() {
        Collection<GameObject> gameObjects = getGameObjects();
        for (GameObject gameObject : gameObjects) {
            gameObject.update();
        }
    }

    /**
     * Called every fixed update(Physics)
     */
    public void fixedUpdate() {

    }

    /**
     * @return All GameObjects in scene
     */
    // TODO: Optimize
    public Collection<GameObject> getGameObjects() {
        return sceneObject.getGameObjects();
    }

    /**
     * @param limit Max amount of lights in this array
     * @param centerPosition Position of the GameObject
     * @return Light array
     */
    // TODO: Optimize
    public LightComponent[] getLights(int limit, Vector3f centerPosition) {
        Collection<GameObject> gameObjects = getGameObjects();
        Collection<GameObject> lights = gameObjects.stream().filter(item -> item.hasComponent(LightComponent.class).isPresent()).toList();
        List<GameObject> sortedLights = lights.stream().sorted((o1, o2) -> {
            float o1Distance = centerPosition.distance(o1.getSpecialTransform(Transform.WORLD_SPACE).getPosition());
            float o2Distance = centerPosition.distance(o2.getSpecialTransform(Transform.WORLD_SPACE).getPosition());

            if (o2Distance > o1Distance) { //  || !light1.hasAttenuation()
                return -1;
            } else if (o2Distance < o1Distance) { //  || !light2.hasAttenuation()
                return 1;
            } else {
                return 0;
            }
        }).toList();

        LightComponent[] components = new LightComponent[Math.min(sortedLights.size(), limit)];
        for (int i = 0; i < components.length; i++) {
            components[i] = (LightComponent) sortedLights.get(i).hasComponent(LightComponent.class).orElseGet(null);
        }
        return components;
    }

    /**
     * @return Window were the scene is rendered
     */
    public EngineWindow getEngineWindow() {
        return engineWindow;
    }

    /**
     * @return InputSystem for this scene
     */
    public InputSystem getInputSystem() {
        return engineWindow.getInputSystem();
    }

    /**
     * @return Name of the scene
     */
    public String getName() {
        return name;
    }

    /**
     * @return GameObject responsible for managing all GameObjects in the scene
     */
    public GameObject getSceneObject() {
        return sceneObject;
    }

    /**
     * @return Utils class for openGL
     */
    public GLUtils getGLUtils() {
        return this.engineWindow.getGLUtils();
    }

}
