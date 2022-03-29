package de.rafael.ravbite.engine.graphics.components.mesh;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 12:03 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.mesh.Mesh;
import de.rafael.ravbite.engine.graphics.mesh.StoredMesh;
import de.rafael.ravbite.engine.graphics.components.Component;

public class MeshComponent extends Component {

    private Mesh mesh;
    private StoredMesh storedMesh;

    public MeshComponent(Mesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public void initialize() {
        this.storedMesh = this.mesh.store(getGameObject().getScene().getEngineWindow());
    }

    /**
     * @return Mesh
     */
    public Mesh getMesh() {
        return mesh;
    }

    /**
     * @return Mesh stored in OpenGL
     */
    public StoredMesh getStoredMesh() {
        return storedMesh;
    }

    /**
     * Sets the mesh handled by the Component
     * @param mesh New Mesh
     */
    public void setMesh(Mesh mesh) {
        // TODO: Dispose/Delete old mesh from OpenGL
        this.mesh = mesh;
        this.storedMesh = mesh.store(getGameObject().getScene().getEngineWindow());
    }

}
