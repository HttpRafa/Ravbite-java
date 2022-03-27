package net.rafael.ravbite.engine.graphics.utils;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 11:47 AM
// In the project Ravbite
//
//------------------------------

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class DataWatcher {

    private final List<Integer> vaoList = new ArrayList<>();
    private final List<Integer> vboList = new ArrayList<>();

    /**
     * Delete all VAOs and VBOs stored in this dataWatcher
     */
    public void rbCleanUp() {
        for (Integer vao : vaoList) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (Integer vbo : vboList) {
            GL15.glDeleteBuffers(vbo);
        }
    }

    /**
     * Store vao for later cleanUp
     * @param vao Vao to store
     */
    public void glVao(int vao) {
        vaoList.add(vao);
    }

    /**
     * Store vbo for later cleanUp
     * @param vbo Vbo to store
     */
    public void glVbo(int vbo) {
        vboList.add(vbo);
    }

}
