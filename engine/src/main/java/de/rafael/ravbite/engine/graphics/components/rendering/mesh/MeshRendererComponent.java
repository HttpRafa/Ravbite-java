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
package de.rafael.ravbite.engine.graphics.components.rendering.mesh;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 11:59 AM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.camera.CameraComponent;
import de.rafael.ravbite.engine.graphics.components.classes.Component;
import de.rafael.ravbite.engine.graphics.components.classes.RenderComponent;
import de.rafael.ravbite.engine.graphics.components.mesh.MeshComponent;
import de.rafael.ravbite.engine.graphics.object.game.material.IMaterial;
import de.rafael.ravbite.engine.graphics.object.game.material.standard.Material;
import de.rafael.ravbite.engine.graphics.object.game.mesh.Mesh;
import de.rafael.ravbite.engine.graphics.object.game.mesh.StoredMesh;
import de.rafael.ravbite.engine.graphics.shader.AbstractShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.Optional;

public class MeshRendererComponent extends RenderComponent {

    @Override
    public void render(CameraComponent cameraComponent) {
        // TODO: Optimize
        Optional<Component> componentOptional = getGameObject().hasComponent(MeshComponent.class);
        if(componentOptional.isPresent()) {
            // Mesh
            MeshComponent meshComponent = (MeshComponent) componentOptional.get();
            Mesh[] meshes = meshComponent.getMesh().collectMeshes();
            for (Mesh engineMesh : meshes) {
                StoredMesh mesh = engineMesh.getStoredMesh();

                // Material
                IMaterial material = engineMesh.getMaterial();

                GL30.glBindVertexArray(mesh.getVao());
                GL20.glEnableVertexAttribArray(0);
                GL20.glEnableVertexAttribArray(1);
                GL20.glEnableVertexAttribArray(2);

                // Apply Material
                AbstractShader abstractShader = null;
                if(material != null) {
                    // Start Shader
                    abstractShader = getGameObject().getScene().getEngineView().getShader(material.getShaderId());
                    abstractShader.bind();
                    abstractShader.prepareObject(this.getGameObject(), material, cameraComponent, this);

                    if(material instanceof Material standardMaterial) {
                        // Active Texture
                        if(standardMaterial.getDiffuse() != null) {
                            // TODO: Implement material.getAlbedo().getColor()

                            GL13.glActiveTexture(GL13.GL_TEXTURE0);
                            GL11.glBindTexture(GL11.GL_TEXTURE_2D, standardMaterial.getDiffuse().getDiffuseTextureId());
                        }
                    }
                }

                // Render the mesh
                GL20.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

                // Stop Shader
                if(abstractShader != null) abstractShader.unbind();

                GL20.glDisableVertexAttribArray(0);
                GL20.glDisableVertexAttribArray(1);
                GL20.glDisableVertexAttribArray(2);
                GL30.glBindVertexArray(0);
            }
        } else {
            // TODO: Warning MeshRender without a meshComponent
        }
    }

}
