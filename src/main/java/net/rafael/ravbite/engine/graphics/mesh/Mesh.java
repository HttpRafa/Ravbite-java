package net.rafael.ravbite.engine.graphics.mesh;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 11:32 AM
// In the project Ravbite
//
//------------------------------

import net.rafael.ravbite.engine.graphics.window.EngineWindow;

public class Mesh {

    private float[] vertices;
    private float[] textureCoords;
    private int[] indices;

    public Mesh(float[] vertices, float[] textureCoords, int[] indices) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.indices = indices;
    }

    /**
     * Store the meshData into openGL VAOs and VBOs
     * @param engineWindow Window to handle the VAOs and VBOs
     * @return Stored version of the mesh
     */
    public StoredMesh store(EngineWindow engineWindow) {
        return engineWindow.getGLUtils().rbLoadToVAO(this);
    }

    /**
     * Sets the vertices
     * @param vertices New vertices
     */
    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    /**
     * @return Vertices in this mesh
     */
    public float[] getVertices() {
        return vertices;
    }

    /**
     * Sets the textureCoords
     * @param textureCoords New textureCoords
     */
    public void setTextureCoords(float[] textureCoords) {
        this.textureCoords = textureCoords;
    }

    /**
     * @return TextureCoords used for this mesh
     */
    public float[] getTextureCoords() {
        return textureCoords;
    }

    /**
     * Sets the indices
     * @param indices New indices
     */
    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    /**
     * @return Indices in this mesh
     */
    public int[] getIndices() {
        return indices;
    }

}
