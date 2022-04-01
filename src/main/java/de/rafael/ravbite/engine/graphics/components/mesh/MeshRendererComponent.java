package de.rafael.ravbite.engine.graphics.components.mesh;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 11:59 AM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.Component;
import de.rafael.ravbite.engine.graphics.components.RenderComponent;
import de.rafael.ravbite.engine.graphics.components.camera.CameraComponent;
import de.rafael.ravbite.engine.graphics.components.material.MaterialComponent;
import de.rafael.ravbite.engine.graphics.material.Material;
import de.rafael.ravbite.engine.graphics.mesh.StoredMesh;
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
            StoredMesh mesh = meshComponent.getStoredMesh();

            // Material
            Optional<Component> materialComponentOptional = getGameObject().hasComponent(MaterialComponent.class);
            MaterialComponent materialComponent = (MaterialComponent) materialComponentOptional.orElse(null);

            GL30.glBindVertexArray(mesh.getVao());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL20.glEnableVertexAttribArray(2);

            // Apply Material
            Material material = null;
            if(materialComponent != null) {
                material = materialComponent.getMaterial();
            }
            AbstractShader abstractShader = null;
            if(material != null) {
                // Start Shader
                abstractShader = this.getGameObject().getScene().getEngineWindow().getShader(material.getShaderId());
                abstractShader.bind();
                abstractShader.prepareObject(this.getGameObject(), cameraComponent, this);

                // Active Texture
                if(material.getAlbedo() != null) {
                    // TODO: Implement material.getAlbedo().getColor()

                    GL13.glActiveTexture(GL13.GL_TEXTURE0);
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, material.getAlbedo().getTextureId());
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
        } else {
            // TODO: Warning MeshRender without a meshComponent
        }
    }

}
