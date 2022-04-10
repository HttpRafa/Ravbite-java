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
package de.rafael.ravbite.engine.graphics.object.game;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/22/2022 at 5:12 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.Component;
import de.rafael.ravbite.engine.graphics.components.RenderComponent;
import de.rafael.ravbite.engine.graphics.components.camera.CameraComponent;
import de.rafael.ravbite.engine.graphics.components.transform.Transform;
import de.rafael.ravbite.engine.graphics.object.scene.Scene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class GameObject {

    private final Scene scene;
    private final String name;

    private int renderLayer = 0;

    private final Transform transform;
    private final List<Component> objectComponents = new ArrayList<>();

    private GameObject parentObject = null;
    private final List<GameObject> childrenObjects = new ArrayList<>();

    public GameObject(Scene scene) {
        this.scene = scene;
        this.name = "GameObject-" + System.currentTimeMillis();
        this.transform = new Transform();
        this.objectComponents.add(this.transform);
    }

    public GameObject(Scene scene, GameObject parentObject) {
        this.scene = scene;
        this.name = "GameObject-" + System.currentTimeMillis();
        this.parentObject = parentObject;
        this.transform = new Transform();
        this.objectComponents.add(this.transform);
    }

    public GameObject(Scene scene, String name) {
        this.scene = scene;
        this.name = name;
        this.transform = new Transform();
        this.objectComponents.add(this.transform);
    }

    public GameObject(Scene scene, String name, GameObject parentObject) {
        this.scene = scene;
        this.name = name;
        this.parentObject = parentObject;
        this.transform = new Transform();
        this.objectComponents.add(this.transform);
    }

    /**
     * Append new child to GameObject
     * @param child GameObject to add
     * @return GameObject
     */
    public GameObject appendChild(GameObject child) {
        childrenObjects.add(child);
        return this;
    }

    /**
     * Append new children to GameObject
     * @param children GameObjects to add
     * @return GameObject
     */
    public GameObject appendChildren(GameObject... children) {
        childrenObjects.addAll(List.of(children));
        return this;
    }

    /**
     * Append new component to GameObject
     * @param component Component to add
     * @return GameObject
     */
    public GameObject appendComponent(Component component) {
        if(component instanceof Transform) return this;
        component.setGameObject(this);
        objectComponents.add(component);
        component.initialize();
        return this;
    }

    /**
     * Append new components to GameObject
     * @param components Components to add
     * @return GameObject
     */
    public GameObject appendComponents(Component... components) {
        for (Component component : components) {
            appendComponent(component);
        }
        return this;
    }

    public Optional<Component> hasComponent(Class<? extends Component> type) {
        for (Component objectComponent : objectComponents) {
            if(type.isAssignableFrom(objectComponent.getClass())) {
                return Optional.of(objectComponent);
            }
        }
        return Optional.empty();
    }

    /**
     * Called to render the GameObject
     */
    public void render(CameraComponent cameraComponent) {
        for (Component objectComponent : objectComponents) {
            if(objectComponent instanceof RenderComponent) {
                RenderComponent renderer = (RenderComponent) objectComponent;
                renderer.render(cameraComponent);
            }
        }
    }

    /**
     * Called every frame
     */
    public void update() {
        for (Component objectComponent : objectComponents) {
            objectComponent.update();
        }
    }

    /**
     * Called every fixed update(Physics)
     */
    public void fixedUpdate() {
        for (Component objectComponent : objectComponents) {
            objectComponent.fixedUpdate();
        }
    }

    /**
     * @return All GameObjects under this object
     */
    public Collection<GameObject> getGameObjects() {
        List<GameObject> list = new ArrayList<>();
        for (GameObject childrenObject : childrenObjects) {
            list.add(childrenObject);
            list.addAll(childrenObject.getGameObjects());
        }
        return list;
    }

    /**
     * @return Scene were the GameObject is located
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * @return Name of the GameObject
     */
    public String getName() {
        return name;
    }

    /**
     * @return Layer were the GameObject is rendered
     */
    public int getRenderLayer() {
        return renderLayer;
    }

    /**
     * Updates the renderLayer
     * @param renderLayer New renderLayer
     */
    public void setRenderLayer(int renderLayer) {
        this.renderLayer = renderLayer;
    }

    /**
     * @return List of components
     */
    public List<Component> getObjectComponents() {
        return objectComponents;
    }

    /**
     * @return Parent GameObject
     */
    public GameObject getParentObject() {
        return parentObject;
    }

    /**
     * @return List of children
     */
    public List<GameObject> getChildrenObjects() {
        return childrenObjects;
    }

    /**
     * @return Transform stored in the object
     */
    public Transform getTransform() {
        return transform;
    }

    /**
     * @param space Space of Transform(WORLD_SPACE, OBJECT_SPACE)
     * @return Transform
     */
    public Transform getSpecialTransform(int space) {
        if(space == Transform.OBJECT_SPACE) {
            return this.transform;
        } else {
            Transform transform = this.transform.clone();
            if(parentObject != null) transform.add(parentObject.getSpecialTransform(Transform.WORLD_SPACE));
            return transform;
        }
    }

}
