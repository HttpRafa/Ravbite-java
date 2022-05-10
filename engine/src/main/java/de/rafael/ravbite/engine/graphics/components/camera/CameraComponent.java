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
package de.rafael.ravbite.engine.graphics.components.camera;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/25/2022 at 4:13 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.classes.Component;
import de.rafael.ravbite.engine.graphics.components.classes.ISizeDependent;
import de.rafael.ravbite.engine.graphics.object.game.GameObject;
import org.apache.commons.lang3.ArrayUtils;
import org.joml.Matrix4f;

public class CameraComponent extends Component implements ISizeDependent {

    public int[] renderLayers;

    public float fieldOfView = 100;
    public float nearPlane = 0.1f;
    public float farPlane = 1000f;

    private Matrix4f projectionMatrix;

    public CameraComponent() {
        this.renderLayers = new int[]{0};
    }

    public CameraComponent(int... renderLayers) {
        this.renderLayers = renderLayers;
    }

    public CameraComponent(int[] renderLayers, float fieldOfView, float nearPlane, float farPlane) {
        this.renderLayers = renderLayers;
        this.fieldOfView = fieldOfView;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public CameraComponent fieldOfView(float fieldOfView) {
        this.fieldOfView = fieldOfView;
        return this;
    }

    public CameraComponent nearPlane(float nearPlane) {
        this.nearPlane = nearPlane;
        return this;
    }

    public CameraComponent farPlane(float farPlane) {
        this.farPlane = farPlane;
        return this;
    }

    @Override
    public void initialize() {
        updateProjectionMatrix();
    }

    @Override
    public void valueUpdate(String fieldName) {
        updateProjectionMatrix();
    }

    @Override
    public void sizeChanged() {
        updateProjectionMatrix();
    }

    /**
     * Updates the projectionMatrix
     */
    public void updateProjectionMatrix() {
        float aspectRatio = this.getGameObject().getScene().getEngineWindow().getAspectRatio();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(fieldOfView / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = farPlane - nearPlane;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((farPlane + nearPlane) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * nearPlane * farPlane) / frustum_length));
        projectionMatrix.m33(0);
    }

    /**
     * Called if a frame is render on this camera
     */
    public void startRendering() {
        // TODO: Render to Camera FBO
    }

    /**
     * Called if a frame is finished rendering on this camera
     */
    public void stopRendering() {

    }

    /**
     * Check if the camera is responsible to render this GameObject
     * @param gameObject GameObject
     * @return If the camera is responsible
     */
    public boolean isResponsibleFor(GameObject gameObject) {
        return isResponsibleFor(gameObject.getRenderLayer());
    }

    /**
     * Check if the camera is responsible to render this GameObject
     * @param layer LayerId
     * @return If the camera is responsible
     */
    public boolean isResponsibleFor(int layer) {
        return ArrayUtils.contains(renderLayers, layer);
    }

    /**
     * Sets the renderLayers
     * @param renderLayers New renderLayers
     */
    public void setRenderLayers(int[] renderLayers) {
        this.renderLayers = renderLayers;
    }

    /**
     * Sets the FieldOfView of the camera
     * @param fieldOfView FieldOfView
     */
    public void setFieldOfView(float fieldOfView) {
        this.fieldOfView = fieldOfView;
        updateProjectionMatrix();
    }

    /**
     * Sets the NearPlane of the camera
     * @param nearPlane NearPlane
     */
    public void setNearPlane(float nearPlane) {
        this.nearPlane = nearPlane;
        updateProjectionMatrix();
    }

    /**
     * Sets the FarPlane of the camera
     * @param farPlane FarPlane
     */
    public void setFarPlane(float farPlane) {
        this.farPlane = farPlane;
        updateProjectionMatrix();
    }

    /**
     * @return ProjectionMatrix used by the camera
     */
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    /**
     * Sets the projectionMatrix used by the camera
     * @param projectionMatrix ProjectionMatrix
     */
    public void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

}
