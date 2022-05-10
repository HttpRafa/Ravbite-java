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
package de.rafael.ravbite.engine.graphics.object.game.mesh;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 11:32 AM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.object.game.material.IMaterial;
import de.rafael.ravbite.engine.graphics.window.EngineWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mesh {

    private String name = "Mesh";

    private Mesh parentMesh;
    private StoredMesh storedMesh;

    private IMaterial material;

    private float[] vertices;
    private float[] normals;
    private float[] tangents;

    private float[] textureCoords;
    private int[] indices;

    private Mesh[] subMeshes = new Mesh[0];

    public Mesh(IMaterial material, float[] vertices, float[] normals, float[] tangents, float[] textureCoords, int[] indices) {
        this.material = material;
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.indices = indices;
        this.normals = normals;
        this.tangents = tangents;
    }

    public Mesh(String name, IMaterial material, float[] vertices, float[] normals, float[] tangents, float[] textureCoords, int[] indices) {
        this.name = name;
        this.material = material;
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.indices = indices;
        this.normals = normals;
        this.tangents = tangents;
    }

    /**
     * Store the meshData into openGL VAOs and VBOs
     * @param engineWindow Window to handle the VAOs and VBOs
     */
    public void store(EngineWindow engineWindow) {
        storedMesh = engineWindow.getUtils().rbLoadToVAO(this);
    }

    /**
     * Stores all meshes in OpenGL
     * @param engineWindow Window to handle the VAOs and VBOs
     */
    public void storeMeshes(EngineWindow engineWindow) {
        store(engineWindow);
        for (Mesh subMesh : subMeshes) {
            subMesh.storeMeshes(engineWindow);
        }
    }

    /**
     * @return All subMesh and the mesh
     */
    public Mesh[] collectMeshes() {
        List<Mesh> meshList = new ArrayList<>();
        meshList.add(this);
        for (Mesh subMesh : subMeshes) {
            meshList.addAll(List.of(subMesh.collectMeshes()));
        }
        return meshList.toArray(new Mesh[0]);
    }

    /**
     * Adds a new subMesh to the mesh
     * @param subMesh subMesh
     */
    public void addSubMesh(Mesh subMesh) {
        subMeshes = Arrays.copyOf(subMeshes, subMeshes.length + 1);
        subMeshes[subMeshes.length - 1] = subMesh;
        subMesh.setParentMesh(this);
    }

    /**
     * Sets the name of the mesh
     * @param name New name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Name of the mesh
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the parentMesh
     * @param parentMesh parentMesh
     */
    public void setParentMesh(Mesh parentMesh) {
        this.parentMesh = parentMesh;
    }

    /**
     * @return parentMesh
     */
    public Mesh getParentMesh() {
        return parentMesh;
    }

    /**
     * Sets the storedMesh
     * @param storedMesh Stored version of the mesh
     */
    public void setStoredMesh(StoredMesh storedMesh) {
        this.storedMesh = storedMesh;
    }

    /**
     * @return Stored version of the mesh
     */
    public StoredMesh getStoredMesh() {
        return storedMesh;
    }

    /**
     * Sets the material of the mesh
     * @param material Material
     */
    public void setMaterial(IMaterial material) {
        this.material = material;
    }

    /**
     * Sets the material of every mesh to ...
     * @param material Material
     */
    public void overwriteMaterials(IMaterial material) {
        for (Mesh mesh : collectMeshes()) {
            mesh.setMaterial(material);
        }
    }

    /**
     * @return Material of the mesh
     */
    public IMaterial getMaterial() {
        return material;
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

    /**
     * Sets the subMesh array
     * @param subMeshes New subMeshes array for the mesh
     */
    public void setSubMeshes(Mesh[] subMeshes) {
        this.subMeshes = subMeshes;
    }

    /**
     * @return All subMeshes for this mesh
     */
    public Mesh[] getSubMeshes() {
        return subMeshes;
    }

}
