package de.rafael.ravbite.engine.graphics.model;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/31/2022 at 1:34 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.asset.AssetLocation;
import de.rafael.ravbite.engine.graphics.mesh.Mesh;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModelUtils {

    public static Mesh[] rbLoadMeshesFromModel(AssetLocation assetLocation) throws IOException {
        AIScene aiScene = ModelUtils.rbLoadScene(assetLocation);
        PointerBuffer pointerBuffer = aiScene.mMeshes();
        List<Mesh> meshList = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(pointerBuffer).limit(); i++) {
            AIMesh aiMesh = AIMesh.create(pointerBuffer.get(i));
            Mesh mesh = ModelUtils.rbProcessMesh(aiMesh);
            meshList.add(mesh);
        }
        return meshList.toArray(new Mesh[0]);
    }

    public static AIScene rbLoadScene(AssetLocation assetLocation) throws IOException {
        InputStream inputStream = assetLocation.asInputStream();
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(inputStream.available());
        while (inputStream.available() > 0) {
            byteBuffer.put((byte) inputStream.read());
        }
        byteBuffer.flip();
        return Assimp.aiImportFileFromMemory(byteBuffer, Assimp.aiProcess_Triangulate | Assimp.aiProcess_OptimizeMeshes | Assimp.aiProcess_FlipUVs | Assimp.aiProcess_CalcTangentSpace, "");
    }

    public static Mesh rbProcessMesh(AIMesh aiMesh) {
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

        AIVector3D.Buffer tangentsData = aiMesh.mTangents();;
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

        return new Mesh(vertices, normals, tangents, textureCoords, indices);
    }

}
