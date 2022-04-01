package de.rafael.ravbite.engine.graphics.utils;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 11:37 AM
// In the project Ravbite
//
//------------------------------

import de.matthiasmann.twl.utils.PNGDecoder;
import de.rafael.ravbite.engine.exception.ShaderCompilationException;
import de.rafael.ravbite.engine.graphics.asset.AssetLocation;
import de.rafael.ravbite.engine.graphics.mesh.Mesh;
import de.rafael.ravbite.engine.graphics.mesh.StoredMesh;
import de.rafael.ravbite.engine.graphics.window.EngineWindow;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public record GLUtils(EngineWindow engineWindow) {

    // Ravbite

    /**
     * Loads the texture from a file and stores it in OpenGL permanently. Till the program is stopped.
     * @param assetLocation Location were the texture is stored
     * @return TextureId generated by OpenGL
     * @throws IOException If the file doesn't exist
     */
    public int rbStaticLoadTexture(AssetLocation assetLocation) throws IOException {
        InputStream inputStream = assetLocation.asInputStream();

        PNGDecoder decoder = new PNGDecoder(inputStream);
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buffer.flip();

        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        return textureId;
    }

    /**
     * Loads the texture from a file and stores it in OpenGL
     * @param assetLocation Location were the texture is stored
     * @return TextureId generated by OpenGL
     * @throws IOException If the file doesn't exist
     */
    public int rbLoadTexture(AssetLocation assetLocation) throws IOException {
        int textureId = rbStaticLoadTexture(assetLocation);
        engineWindow.getDataWatcher().glTexture(textureId);
        return textureId;
    }

    /**
     * Loads the shader from a file and compiles it and then stores it into OpenGL
     * @param assetLocation Location were the shader is stored
     * @param type ShaderType(Vertex or Fragment Shader)
     * @return ShaderId generated by OpenGL
     * @throws IOException If the file doesn't exist
     * @throws ShaderCompilationException If the shader is not programmed right
     */
    public int rbLoadShader(AssetLocation assetLocation, int type) throws IOException, ShaderCompilationException {
        String source = assetLocation.loadString();
        int shaderId = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderId, source);
        GL20.glCompileShader(shaderId);
        if(GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            throw new ShaderCompilationException(GL20.glGetShaderInfoLog(shaderId, 500), assetLocation);
        }
        return shaderId;
    }

    /**
     * Convert Mesh to StoredMesh
     *
     * @param mesh Mesh
     * @return StoredMesh
     */
    public StoredMesh rbLoadToVAO(Mesh mesh) {
        int vao = glCreateVAO();
        rbBindIndicesBuffer(mesh.getIndices());
        rbStoreDataInAttributeList(0, 3, mesh.getVertices());
        rbStoreDataInAttributeList(1, 2, mesh.getTextureCoords());
        rbStoreDataInAttributeList(2, 3, mesh.getNormals());
        glUnbindVAO();
        return new StoredMesh(vao, mesh.getIndices().length);
    }

    /**
     * Store float[] into VBO of OpenGL
     *
     * @param number Index of the buffer
     * @param size Size of the data packs
     * @param data   Data to store
     */
    public void rbStoreDataInAttributeList(int number, int size, float[] data) {
        int vbo = GL15.glGenBuffers();
        engineWindow.getDataWatcher().glVbo(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer floatBuffer = rbStoreDataInBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(number, size, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Store int[] into VBO of OpenGL
     *
     * @param indices Indices to store
     */
    public void rbBindIndicesBuffer(int[] indices) {
        int vbo = GL15.glGenBuffers();
        engineWindow.getDataWatcher().glVbo(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer intBuffer = rbStoreDataInBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, intBuffer, GL15.GL_STATIC_DRAW);
    }

    /**
     * Store int[] to IntBuffer
     *
     * @param data Array to store
     * @return IntBuffer
     */
    public IntBuffer rbStoreDataInBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    /**
     * Store float[] to FloatBuffer
     *
     * @param data Array to store
     * @return FloatBuffer
     */
    public FloatBuffer rbStoreDataInBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    // OpenGL

    /**
     * GL: Create new VAO with OpenGL
     *
     * @return ID of the new VAO
     */
    public int glCreateVAO() {
        int vao = GL30.glGenVertexArrays();
        engineWindow.getDataWatcher().glVao(vao);
        GL30.glBindVertexArray(vao);
        return vao;
    }

    /**
     * Unbind current VAO
     */
    public void glUnbindVAO() {
        GL30.glBindVertexArray(0);
    }

}
