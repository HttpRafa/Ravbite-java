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
import de.rafael.ravbite.engine.graphics.object.scene.Scene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class GameObject {

    private final Scene scene;
    private final String name;

    private int renderLayer = 0;

    private final List<Component> objectComponents = new ArrayList<>();

    private GameObject parentObject = null;
    private final List<GameObject> childrenObjects = new ArrayList<>();

    public GameObject(Scene scene) {
        this.scene = scene;
        this.name = "GameObject-" + System.currentTimeMillis();
    }

    public GameObject(Scene scene, GameObject parentObject) {
        this.scene = scene;
        this.name = "GameObject-" + System.currentTimeMillis();
        this.parentObject = parentObject;
    }

    public GameObject(Scene scene, String name) {
        this.scene = scene;
        this.name = name;
    }

    public GameObject(Scene scene, String name, GameObject parentObject) {
        this.scene = scene;
        this.name = name;
        this.parentObject = parentObject;
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
            if(objectComponent instanceof RenderComponent renderer) renderer.render(cameraComponent);
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
    public Collection<Component> getObjectComponents() {
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
    public Collection<GameObject> getChildrenObjects() {
        return childrenObjects;
    }

}
