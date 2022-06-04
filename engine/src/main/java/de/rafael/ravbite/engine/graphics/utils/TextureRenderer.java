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
// On 06/04/2022 at 5:36 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.view.EngineView;
import de.rafael.ravbite.engine.utils.RavbiteUtils;
import de.rafael.ravbite.engine.utils.exception.ShaderCompilationException;
import org.lwjgl.opengl.*;

import java.io.IOException;
import java.nio.FloatBuffer;

public class TextureRenderer {

    private final RavbiteUtils utils;

    private int quadVao;
    private int quadVbo;

    private int vertexShader;
    private int fragmentShader;

    private int program;

    public TextureRenderer(EngineView engineView) {
        this.utils = engineView.getUtils();

        createQuad();
        loadShaders();
    }

    public void glRenderTexture(int bufferTexture) {
        GL20.glUseProgram(program);

        GL30.glBindVertexArray(quadVao);
        GL20.glEnableVertexAttribArray(0);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, bufferTexture);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);

        GL20.glUseProgram(0);
    }

    private void loadShaders() {
        try {
            vertexShader = utils.rbLoadShader(
                    "#version 140\n" +
                            "\n" +
                            "in vec2 position;\n" +
                            "\n" +
                            "out vec2 textureCoords;\n" +
                            "\n" +
                            "uniform mat4 transformationMatrix;\n" +
                            "\n" +
                            "void main(void){\n" +
                            "\n" +
                            "\tgl_Position = vec4(position, 0.0, 1.0);\n" +
                            "\ttextureCoords = vec2((position.x+1.0)/2.0, (position.y+1.0)/2.0);\n" +
                            "}", GL20.GL_VERTEX_SHADER);
            fragmentShader = utils.rbLoadShader("#version 140\n" +
                    "\n" +
                    "in vec2 textureCoords;\n" +
                    "\n" +
                    "out vec4 out_Color;\n" +
                    "\n" +
                    "uniform sampler2D guiTexture;\n" +
                    "\n" +
                    "void main(void){\n" +
                    "\n" +
                    "\tout_Color = vec4(1,1,1,1) - texture(guiTexture, textureCoords);\n" +
                    "\n" +
                    "}", GL20.GL_FRAGMENT_SHADER);
        } catch (IOException | ShaderCompilationException exception) {
            exception.printStackTrace();
        }
        program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertexShader);
        GL20.glAttachShader(program, fragmentShader);
        GL20.glBindAttribLocation(program, 0, "position");
        GL20.glLinkProgram(program);
        GL20.glValidateProgram(program);
    }

    private void createQuad() {
        quadVao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(quadVao);
        quadVbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, quadVbo);
        FloatBuffer floatBuffer = utils.rbStoreDataInBuffer(new float[] {-1, 1, -1, -1, 1, 1, 1, -1});
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        utils.glUnbindVAO();
    }

    public void destroy() {
        GL20.glDetachShader(program, vertexShader);
        GL20.glDetachShader(program, fragmentShader);
        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);
        GL20.glDeleteProgram(program);

        GL30.glDeleteBuffers(quadVbo);
        GL30.glDeleteVertexArrays(quadVao);
    }

}
