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
        loadTransformationMatrix(Maths.createTransformationMatrix(gameObject.getSpecialTransform(Transform.WORLD_SPACE)));
    }

    private void loadTransformationMatrix(Matrix4f matrix) {
        super.load(transformationMatrix, matrix);
    }

    @Override
    public void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    @Override
    public void updateUniformLocations() {
        transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    /**
     * @return Location of the uniform variable transformationMatrix
     */
    public int getTransformationMatrix() {
        return transformationMatrix;
    }

}
