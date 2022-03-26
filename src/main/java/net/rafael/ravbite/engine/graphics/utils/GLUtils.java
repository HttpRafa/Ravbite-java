package net.rafael.ravbite.engine.graphics.utils;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 11:37 AM
// In the project Ravbite
//
//------------------------------

import net.rafael.ravbite.engine.graphics.mesh.Mesh;
import net.rafael.ravbite.engine.graphics.mesh.StoredMesh;
import net.rafael.ravbite.engine.graphics.window.EngineWindow;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public record GLUtils(EngineWindow engineWindow) {

    // Ravbite

    /**
     * Convert Mesh to Stored
     *
     * @param mesh Mesh
     * @return StoredMesh
     */
    public StoredMesh rbLoadToVAO(Mesh mesh) {
        int vao = glCreateVAO();
        rbBindIndicesBuffer(mesh.getIndices());
        rbStoreDataInAttributeList(0, mesh.getVertices());
        glUnbindVAO();
        return new StoredMesh(vao, mesh.getIndices().length);
    }

    /**
     * Store float[] into VBO of OpenGL
     *
     * @param number Index of the buffer
     * @param data   Data to store
     */
    public void rbStoreDataInAttributeList(int number, float[] data) {
        int vbo = GL15.glGenBuffers();
        engineWindow.getDataWatcher().glVbo(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer floatBuffer = rbStoreDataInBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(number, 3, GL11.GL_FLOAT, false, 0, 0);
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
