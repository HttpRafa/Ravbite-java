package net.rafael.ravbite.engine.graphics.object.scene;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/22/2022 at 5:13 PM
// In the project Ravbite
//
//------------------------------

import net.rafael.ravbite.engine.graphics.components.Component;
import net.rafael.ravbite.engine.graphics.components.camera.CameraComponent;
import net.rafael.ravbite.engine.graphics.object.game.GameObject;
import net.rafael.ravbite.engine.graphics.window.EngineWindow;

import java.util.Collection;
import java.util.Optional;

public abstract class Scene {

    private final EngineWindow engineWindow;
    private final String name;

    private final GameObject sceneObject;

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

    /**
     * Called to render the frame
     */
    public void render() {
        Collection<GameObject> gameObjects = getGameObjects();
        // TODO: Optimize
        for (GameObject gameObject : gameObjects) {
            Optional<Component> componentOptional = gameObject.hasComponent(CameraComponent.class);
            if (componentOptional.isPresent()) {
                CameraComponent cameraComponent = (CameraComponent) componentOptional.get();
                cameraComponent.startRendering();
                renderCamera(gameObjects, cameraComponent);
                cameraComponent.stopRendering();
            }
        }
    }

    /**
     * Called to render the frame in the camera
     *
     * @param cameraComponent Camera
     */
    private void renderCamera(Collection<GameObject> gameObjects, CameraComponent cameraComponent) {
        for (GameObject gameObject : gameObjects) {
            if (cameraComponent.isResponsableFor(gameObject)) {
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
     * @return Window were the scene is rendered
     */
    public EngineWindow getEngineWindow() {
        return engineWindow;
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

}
