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
package de.rafael.ravbite.engine.graphics.shader.standard;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/27/2022 at 1:26 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.camera.CameraComponent;
import de.rafael.ravbite.engine.graphics.components.classes.RenderComponent;
import de.rafael.ravbite.engine.graphics.components.light.LightComponent;
import de.rafael.ravbite.engine.graphics.components.transform.Transform;
import de.rafael.ravbite.engine.graphics.objects.game.GameObject;
import de.rafael.ravbite.engine.graphics.objects.game.material.IMaterial;
import de.rafael.ravbite.engine.graphics.objects.game.material.standard.Material;
import de.rafael.ravbite.engine.graphics.shader.AbstractShader;
import de.rafael.ravbite.engine.graphics.view.EngineView;
import de.rafael.ravbite.engine.utils.exception.ShaderCompilationException;
import de.rafael.ravbite.engine.utils.math.Maths;
import de.rafael.ravbite.utils.asset.AssetLocation;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.io.IOException;

public class StandardShader extends AbstractShader {

    private int transformationMatrix;
    private int projectionMatrix;
    private int viewMatrix;

    private int diffuseColor;

    private int lightPosition;
    private int lightColor;
    private int shineDamper;
    private int reflectivity;

    public StandardShader(EngineView engineView) {
        super(engineView);
        try {
            super.vertexShader(AssetLocation.create("/shaders/standard/vertexShader.glsl", AssetLocation.INTERNAL));
            super.fragmentShader(AssetLocation.create("/shaders/standard/fragmentShader.glsl", AssetLocation.INTERNAL));
            super.createProgram();
        } catch (IOException | ShaderCompilationException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void prepareObject(GameObject gameObject, IMaterial universalMaterial, CameraComponent cameraComponent, RenderComponent renderer) {
        Material material = (Material) universalMaterial;

        Transform cameraTransform = cameraComponent.getGameObject().getSpecialTransform(Transform.WORLD_SPACE);

        loadProjectionMatrix(cameraComponent.getProjectionMatrix());
        loadViewMatrix(cameraTransform.viewMatrix());
        loadTransformationMatrix(Maths.createTransformationMatrix(gameObject.getSpecialTransform(Transform.WORLD_SPACE)));

        loadDiffuseColor(material.getDiffuse().getColorAsVector());

        loadSpecular(material.getShineDamper(), material.getReflectivity());

        LightComponent[] lights = gameObject.getScene().getLights(15, cameraTransform.position);
        if(lights.length > 0) loadLightsComponent(lights);
    }

    /**
     * Method to load the specular values into the shader
     * @param shineDamper ShineDamper value
     * @param reflectivity Reflectivity value
     */
    private void loadSpecular(float shineDamper, float reflectivity) {
        super.load(this.shineDamper, shineDamper);
        super.load(this.reflectivity, reflectivity);
    }

    /**
     * Method to load the lights into the shader
     * @param lightComponents Lights
     */
    // TODO: Support multiple lights
    private void loadLightsComponent(LightComponent[] lightComponents) {
        super.load(lightPosition, lightComponents[0].getGameObject().getSpecialTransform(Transform.WORLD_SPACE).position);
        super.load(lightColor, lightComponents[0].getColorAsVector());
    }

    /**
     * Method to load the diffuseColor into the shader
     * @param diffuseColor DiffuseColor
     */
    private void loadDiffuseColor(Vector4f diffuseColor) {
        super.load(this.diffuseColor, diffuseColor);
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

        diffuseColor = super.getUniformLocation("diffuseColor");

        lightPosition = super.getUniformLocation("lightPosition");
        lightColor = super.getUniformLocation("lightColor");
        shineDamper = super.getUniformLocation("shineDamper");
        reflectivity = super.getUniformLocation("reflectivity");
    }

    /**
     * @return Location of the uniform variable transformationMatrix
     */
    public int getTransformationMatrix() {
        return transformationMatrix;
    }

}
