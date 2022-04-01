package de.rafael.ravbite.engine.graphics.shader.standard;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/27/2022 at 1:26 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.exception.ShaderCompilationException;
import de.rafael.ravbite.engine.graphics.asset.AssetLocation;
import de.rafael.ravbite.engine.graphics.components.RenderComponent;
import de.rafael.ravbite.engine.graphics.components.camera.CameraComponent;
import de.rafael.ravbite.engine.graphics.components.light.LightComponent;
import de.rafael.ravbite.engine.graphics.components.transform.Transform;
import de.rafael.ravbite.engine.graphics.object.game.GameObject;
import de.rafael.ravbite.engine.graphics.shader.AbstractShader;
import de.rafael.ravbite.engine.graphics.window.EngineWindow;
import de.rafael.ravbite.engine.math.Maths;
import org.joml.Matrix4f;

import java.io.IOException;

public class StandardShader extends AbstractShader {

    private int transformationMatrix;
    private int projectionMatrix;
    private int viewMatrix;

    private int lightPosition;
    private int lightColor;

    public StandardShader(EngineWindow engineWindow) {
        super(engineWindow);
        try {
            super.vertexShader(AssetLocation.create("/shaders/standard/vertexShader.glsl", AssetLocation.INTERNAL));
            super.fragmentShader(AssetLocation.create("/shaders/standard/fragmentShader.glsl", AssetLocation.INTERNAL));
            super.createProgram();
        } catch (IOException | ShaderCompilationException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void prepareObject(GameObject gameObject, CameraComponent cameraComponent, RenderComponent renderer) {
        Transform cameraTransform = cameraComponent.getGameObject().getSpecialTransform(Transform.WORLD_SPACE);

        loadProjectionMatrix(cameraComponent.getProjectionMatrix());
        loadViewMatrix(Maths.createViewMatrix(cameraTransform));
        loadTransformationMatrix(Maths.createTransformationMatrix(gameObject.getSpecialTransform(Transform.WORLD_SPACE)));

        LightComponent[] lights = gameObject.getScene().getLights(15, cameraTransform.getPosition());
        if(lights.length > 0) loadLightsComponent(lights);

    }

    /**
     * Method to load the lights into the shader
     * @param lightComponents Lights
     */
    // TODO: Support multiple lights
    private void loadLightsComponent(LightComponent[] lightComponents) {
        super.load(lightPosition, lightComponents[0].getGameObject().getSpecialTransform(Transform.WORLD_SPACE).getPosition());
        super.load(lightColor, lightComponents[0].getColorAsVector());
    }

    /**
     * Method to load the transformationMatrix into the shader
     * @param transformationMatrix TransformationMatrix
     */
    private void loadTransformationMatrix(Matrix4f transformationMatrix) {
        super.load(this.transformationMatrix, transformationMatrix);
    }

    /**
     * Method to load the projectionMatrix into the shader
     * @param projectionMatrix ProjectionMatrix
     */
    private void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.load(this.projectionMatrix, projectionMatrix);
    }

    /**
     * Method to load the viewMatrix into the shader
     * @param viewMatrix ViewMatrix
     */
    private void loadViewMatrix(Matrix4f viewMatrix) {
        super.load(this.viewMatrix, viewMatrix);
    }

    @Override
    public void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    public void updateUniformLocations() {
        transformationMatrix = super.getUniformLocation("transformationMatrix");
        projectionMatrix = super.getUniformLocation("projectionMatrix");
        viewMatrix = super.getUniformLocation("viewMatrix");

        lightPosition = super.getUniformLocation("lightPosition");
        lightColor = super.getUniformLocation("lightColor");
    }

    /**
     * @return Location of the uniform variable transformationMatrix
     */
    public int getTransformationMatrix() {
        return transformationMatrix;
    }

}
