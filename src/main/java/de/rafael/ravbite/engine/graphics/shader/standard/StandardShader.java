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
        loadProjectionMatrix(cameraComponent.getProjectionMatrix());
        loadViewMatrix(Maths.createViewMatrix(cameraComponent.getGameObject().getSpecialTransform(Transform.WORLD_SPACE)));
        loadTransformationMatrix(Maths.createTransformationMatrix(gameObject.getSpecialTransform(Transform.WORLD_SPACE)));
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
    }

    @Override
    public void updateUniformLocations() {
        transformationMatrix = super.getUniformLocation("transformationMatrix");
        projectionMatrix = super.getUniformLocation("projectionMatrix");
        viewMatrix = super.getUniformLocation("viewMatrix");
    }

    /**
     * @return Location of the uniform variable transformationMatrix
     */
    public int getTransformationMatrix() {
        return transformationMatrix;
    }

}
