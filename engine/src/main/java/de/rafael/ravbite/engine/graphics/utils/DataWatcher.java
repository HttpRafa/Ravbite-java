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
package de.rafael.ravbite.engine.graphics.utils;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 11:47 AM
// In the project Ravbite
//
//------------------------------

import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataWatcher {

    private final HashMap<DataScope, List<Integer>> vaoList = new HashMap<>();
    private final HashMap<DataScope, List<Integer>> vboList = new HashMap<>();
    private final HashMap<DataScope, List<Integer>> textureList = new HashMap<>();
    
    private final HashMap<DataScope, List<Integer>> soundBuffersList = new HashMap<>();

    public DataWatcher() {
        vaoList.put(DataScope.SCENE, new ArrayList<>());
        vaoList.put(DataScope.VIEW, new ArrayList<>());

        vboList.put(DataScope.SCENE, new ArrayList<>());
        vboList.put(DataScope.VIEW, new ArrayList<>());

        textureList.put(DataScope.SCENE, new ArrayList<>());
        textureList.put(DataScope.VIEW, new ArrayList<>());

        soundBuffersList.put(DataScope.SCENE, new ArrayList<>());
        soundBuffersList.put(DataScope.VIEW, new ArrayList<>());
    }

    /**
     * Delete all VAOs and VBOs stored in this dataWatcher
     */
    public void rbCleanUp(DataScope dataScope) {
        for (Integer vao : vaoList.get(dataScope)) {
            GL30.glDeleteVertexArrays(vao);
        }
        vaoList.get(dataScope).clear();
        for (Integer vbo : vboList.get(dataScope)) {
            GL15.glDeleteBuffers(vbo);
        }
        vboList.get(dataScope).clear();
        for (Integer texture : textureList.get(dataScope)) {
            GL11.glDeleteTextures(texture);
        }
        textureList.get(dataScope).clear();
        for (Integer buffer : soundBuffersList.get(dataScope)) {
            AL10.alDeleteBuffers(buffer);
        }
        soundBuffersList.get(dataScope).clear();
    }

    /**
     * Store vao for later cleanUp
     * @param vao Vao to store
     */
    public void glVao(DataScope dataScope, int vao) {
        vaoList.get(dataScope).add(vao);
    }

    /**
     * Store vbo for later cleanUp
     * @param vbo Vbo to store
     */
    public void glVbo(DataScope dataScope, int vbo) {
        vboList.get(dataScope).add(vbo);
    }

    /**
     * Store texture for later cleanUp
     * @param texture Texture to store
     */
    public void glTexture(DataScope dataScope, int texture) {
        textureList.get(dataScope).add(texture);
    }

    /**
     * Store buffers for later cleanUp
     * @param buffer Buffer to store
     */
    public void alBuffer(DataScope dataScope, int buffer) {
        soundBuffersList.get(dataScope).add(buffer);
    }

    public static enum DataScope {

        SCENE,
        VIEW

    }

}
