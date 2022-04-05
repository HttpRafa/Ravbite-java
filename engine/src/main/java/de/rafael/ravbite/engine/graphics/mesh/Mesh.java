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
package de.rafael.ravbite.engine.graphics.mesh;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 11:32 AM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.window.EngineWindow;

public class Mesh {

    private float[] vertices;
    private float[] normals;
    private float[] tangents;

    private float[] textureCoords;
    private int[] indices;

    public Mesh(float[] vertices, float[] normals, float[] tangents, float[] textureCoords, int[] indices) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.indices = indices;
        this.normals = normals;
        this.tangents = tangents;
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
     * Sets the normals
     * @param normals New normals
     */
    public void setNormals(float[] normals) {
        this.normals = normals;
    }

    /**
     * @return Normals in this mesh
     */
    public float[] getNormals() {
        return normals;
    }

    /**
     * Sets the tangents
     * @param tangents New tangents
     */
    public void setTangents(float[] tangents) {
        this.tangents = tangents;
    }

    /**
     * @return Tangents in this mesh
     */
    public float[] getTangents() {
        return tangents;
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
