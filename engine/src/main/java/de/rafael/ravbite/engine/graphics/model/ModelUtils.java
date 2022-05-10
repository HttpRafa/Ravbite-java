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
package de.rafael.ravbite.engine.graphics.model;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/31/2022 at 1:34 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.object.game.material.standard.DiffuseProperty;
import de.rafael.ravbite.engine.graphics.object.game.material.standard.Material;
import de.rafael.ravbite.engine.graphics.object.game.mesh.Mesh;
import de.rafael.ravbite.engine.graphics.window.EngineWindow;
import de.rafael.ravbite.utils.asset.AssetLocation;
import de.rafael.ravbite.utils.io.IOUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModelUtils {

    /**
     * Loads all available meshes from a modelFile
     * @param assetLocation Path to the modelFile
     * @return MeshArray
     */
    public static Mesh rbLoadModel(AssetLocation assetLocation, EngineWindow engineWindow) {
        try {
            AIScene aiScene = ModelUtils.rbLoadScene(assetLocation);

            PointerBuffer pointerBuffer = aiScene.mMeshes();
            Mesh mesh = null;
            for (int i = 0; i < Objects.requireNonNull(pointerBuffer).limit(); i++) {
                AIMesh aiMesh = AIMesh.create(pointerBuffer.get(i));
                Mesh loadedMesh = ModelUtils.rbProcessMesh(aiScene, aiMesh, engineWindow);
                if(mesh == null) {
                    mesh = loadedMesh;
                } else {
                    mesh.addSubMesh(loadedMesh);
                }
            }
            Assimp.aiReleaseImport(aiScene);
            return mesh;
        } catch(Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Loads the aiScene from a modelFile
     * @param assetLocation Path to the modelFile
     * @return Scene of the model or null
     * @throws IOException ?
     */
    public static AIScene rbLoadScene(AssetLocation assetLocation) throws IOException {
        AIScene aiScene = null;
        int i = Assimp.aiProcess_Triangulate |
                Assimp.aiProcess_OptimizeMeshes |
                Assimp.aiProcess_FlipUVs |
                Assimp.aiProcess_CalcTangentSpace |
                Assimp.aiProcess_JoinIdenticalVertices |
                Assimp.aiProcess_ValidateDataStructure |
                Assimp.aiProcess_ImproveCacheLocality |
                Assimp.aiProcess_GenUVCoords |
                Assimp.aiProcess_TransformUVCoords |
                Assimp.aiProcess_LimitBoneWeights |
                Assimp.aiProcess_OptimizeMeshes |
                Assimp.aiProcess_GenSmoothNormals |
                Assimp.aiProcess_SplitLargeMeshes;
        if(assetLocation.getLocation() == AssetLocation.INTERNAL) {
            AIFileIO aiFileIO = rbCreateAIFileIO();
            aiScene = Assimp.aiImportFileEx(assetLocation.getPath(true), i, aiFileIO);
            aiFileIO.OpenProc().free();
            aiFileIO.CloseProc().free();
        } else if(assetLocation.getLocation() == AssetLocation.EXTERNAL) {
            aiScene = Assimp.aiImportFile(new File(assetLocation.getPath(false)).getAbsolutePath(), i);
        }
        return aiScene;
    }

    /**
     * Create virtual filesystem for assimp
     * @return Filesystem
     */
    public static AIFileIO rbCreateAIFileIO() {
        return AIFileIO.create()
                .OpenProc((pFileIO, fileName, openMode) -> {
                    ByteBuffer data;
                    String fileNameUtf8 = MemoryUtil.memUTF8(fileName);
                    try {
                        data = IOUtils.ioResourceToByteBuffer(fileNameUtf8, 8192);
                    } catch (IOException e) {
                        throw new RuntimeException("Could not open file: " + fileNameUtf8);
                    }

                    return AIFile.create()
                            .ReadProc((pFile, pBuffer, size, count) -> {
                                long max = Math.min(data.remaining(), size * count);
                                MemoryUtil.memCopy(MemoryUtil.memAddress(data) + data.position(), pBuffer, max);
                                return max;
                            })
                            .SeekProc((pFile, offset, origin) -> {
                                if (origin == Assimp.aiOrigin_CUR) {
                                    data.position(data.position() + (int) offset);
                                } else if (origin == Assimp.aiOrigin_SET) {
                                    data.position((int) offset);
                                } else if (origin == Assimp.aiOrigin_END) {
                                    data.position(data.limit() + (int) offset);
                                }
                                return 0;
                            })
                            .FileSizeProc(pFile -> data.limit())
                            .address();
                })
                .CloseProc((pFileIO, pFile) -> {
                    AIFile aiFile = AIFile.create(pFile);

                    aiFile.ReadProc().free();
                    aiFile.SeekProc().free();
                    aiFile.FileSizeProc().free();
                });
    }

    /**
     * Converts a aiMesh to an engine Mesh
     * @param aiMesh InputMesh
     * @return OutputMesh
     * @throws IOException ?
     */
    public static Mesh rbProcessMesh(AIScene aiScene, AIMesh aiMesh, EngineWindow engineWindow) throws IOException {
        List<Float> verticesList = new ArrayList<>();
        List<Float> normalsList = new ArrayList<>();
        List<Float> tangentsList = new ArrayList<>();
        List<Float> textureCoordsList = new ArrayList<>();
        List<Integer> indicesList = new ArrayList<>();

        AIVector3D.Buffer verticesData = aiMesh.mVertices();
        for (int i = 0; i < verticesData.limit(); i++) {
            AIVector3D vertex = verticesData.get(i);
            verticesList.add(vertex.x());
            verticesList.add(vertex.y());
            verticesList.add(vertex.z());
        }
        
        AIVector3D.Buffer normalsData = aiMesh.mNormals();
        for (int i = 0; i < Objects.requireNonNull(normalsData).limit(); i++) {
            AIVector3D normal = normalsData.get(i);
            normalsList.add(normal.x());
            normalsList.add(normal.y());
            normalsList.add(normal.z());
        }

        AIVector3D.Buffer tangentsData = aiMesh.mTangents();
        for (int i = 0; i < Objects.requireNonNull(tangentsData).limit(); i++) {
            AIVector3D tangent = tangentsData.get(i);
            tangentsList.add(tangent.x());
            tangentsList.add(tangent.y());
            tangentsList.add(tangent.z());
        }

        AIVector3D.Buffer textureCoordsData = aiMesh.mTextureCoords(0);
        for (int i = 0; i < Objects.requireNonNull(textureCoordsData).limit(); i++) {
            AIVector3D textureCord = textureCoordsData.get(i);
            textureCoordsList.add(textureCord.x());
            textureCoordsList.add(textureCord.y());
        }

        AIFace.Buffer facesData = aiMesh.mFaces();
        for (int i = 0; i < facesData.limit(); i++) {
            AIFace face = facesData.get(i);
            IntBuffer indicesData = face.mIndices();
            for (int idi = 0; idi < indicesData.limit(); idi++) {
                indicesList.add(indicesData.get(idi));
            }
        }

        float[] vertices = new float[verticesList.size()]; for (int i = 0; i < verticesList.size(); i++) {vertices[i] = verticesList.get(i);}
        float[] normals = new float[normalsList.size()]; for (int i = 0; i < normalsList.size(); i++) {normals[i] = normalsList.get(i);}
        float[] tangents = new float[tangentsList.size()]; for (int i = 0; i < tangentsList.size(); i++) {tangents[i] = tangentsList.get(i);}
        float[] textureCoords = new float[textureCoordsList.size()]; for (int i = 0; i < textureCoordsList.size(); i++) {textureCoords[i] = textureCoordsList.get(i);}
        int[] indices = new int[indicesList.size()]; for (int i = 0; i < indicesList.size(); i++) {indices[i] = indicesList.get(i);}

        AIMaterial aiMaterial = null;
        PointerBuffer materialData = aiScene.mMaterials();
        for (int i = 0; i < aiScene.mNumMaterials(); i++) {
            if(i == aiMesh.mMaterialIndex()) {
                assert materialData != null;
                aiMaterial = AIMaterial.create(materialData.get(i));
            }
        }

        Material material = new Material(engineWindow).create();
        if(aiMaterial != null) {
            material = new Material(engineWindow);

            int textureCount = Assimp.aiGetMaterialTextureCount(aiMaterial, Assimp.aiTextureType_DIFFUSE);
            if(textureCount > 1) {
                System.out.println("More than one texture per mesh is currently not supported. Mesh[" + aiMesh.mName() + "]");
            }

            int textureId = -1;
            if(textureCount > 0) {
                AIString pathString = AIString.create();
                Assimp.aiGetMaterialTexture(aiMaterial, Assimp.aiTextureType_DIFFUSE, 0, pathString, (IntBuffer) null, null, null, null, null, null);
                String path = pathString.dataString();

                AssetLocation assetLocation = AssetLocation.create(path, AssetLocation.DETECT);
                textureId = engineWindow.getUtils().rbLoadTexture(assetLocation);
            }

            AIColor4D color = AIColor4D.create();
            Color diffuseColor = new Color(0, 0, 0);
            int result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_DIFFUSE, Assimp.aiTextureType_NONE, 0, color);
            if (result == 0) {
                diffuseColor = new Color(color.r(), color.g(), color.b(), color.a());
            }

            DiffuseProperty diffuseProperty = new DiffuseProperty(material, diffuseColor);
            if(textureId >= 0) diffuseProperty.texture(textureId);
            material.diffuse(diffuseProperty);

            material.create();
        }

        return new Mesh(aiMesh.mName().dataString(), material, vertices, normals, tangents, textureCoords, indices);
    }

}
