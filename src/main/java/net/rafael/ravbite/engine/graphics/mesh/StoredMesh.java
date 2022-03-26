package net.rafael.ravbite.engine.graphics.mesh;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 11:30 AM
// In the project Ravbite
//
//------------------------------

public class StoredMesh {

    private final int vao;
    private final int vertexCount;

    public StoredMesh(int vao, int vertexCount) {
        this.vao = vao;
        this.vertexCount = vertexCount;
    }

    /**
     * @return OpenGL vaoId
     */
    public int getVao() {
        return vao;
    }

    /**
     * @return Amount of vertexes in mesh
     */
    public int getVertexCount() {
        return vertexCount;
    }

}
