package net.rafael.ravbite.engine.graphics.components.mesh;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 11:59 AM
// In the project Ravbite
//
//------------------------------

import net.rafael.ravbite.engine.graphics.components.Component;
import net.rafael.ravbite.engine.graphics.components.camera.CameraComponent;
import net.rafael.ravbite.engine.graphics.mesh.StoredMesh;
import net.rafael.ravbite.engine.graphics.object.game.GameObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.Optional;

public class MeshRendererComponent extends Component {

    public MeshRendererComponent(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void render(CameraComponent cameraComponent) {
        // TODO: Optimize
        Optional<Component> componentOptional = getGameObject().hasComponent(MeshComponent.class);
        if(componentOptional.isPresent()) {
            MeshComponent meshComponent = (MeshComponent) componentOptional.get();
            StoredMesh mesh = meshComponent.getStoredMesh();

            // Render the mesh
            GL30.glBindVertexArray(mesh.getVao());
            GL20.glEnableVertexAttribArray(0);
            GL20.glDrawElements(GL11.GL_TRIANGLES, mesh.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            GL20.glDisableVertexAttribArray(0);
            GL30.glBindVertexArray(0);
        } else {
            // TODO: Warning MeshRender without a meshComponent
        }
    }

}
