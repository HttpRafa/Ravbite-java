package net.rafael.ravbite.engine.graphics.components.mesh;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 12:03 PM
// In the project Ravbite
//
//------------------------------

import net.rafael.ravbite.engine.graphics.components.Component;
import net.rafael.ravbite.engine.graphics.mesh.Mesh;
import net.rafael.ravbite.engine.graphics.mesh.StoredMesh;
import net.rafael.ravbite.engine.graphics.object.game.GameObject;

public class MeshComponent extends Component {

    private Mesh mesh;
    private StoredMesh storedMesh;

    public MeshComponent(GameObject gameObject, Mesh mesh) {
        super(gameObject);
        this.mesh = mesh;
        this.storedMesh = this.mesh.store(gameObject.getScene().getEngineWindow());
    }

    /**
     * @return Mesh
     */
    public Mesh getMesh() {
        return mesh;
    }

    /**
     * Sets the mesh handled by the Component
     *
     * @param mesh New Mesh
     */
    public void setMesh(Mesh mesh) {
        // TODO: Dispose/Delete old mesh from OpenGL
        this.mesh = mesh;
        this.storedMesh = mesh.store(getGameObject().getScene().getEngineWindow());
    }

    /**
     * @return Mesh stored in OpenGL
     */
    public StoredMesh getStoredMesh() {
        return storedMesh;
    }

}
