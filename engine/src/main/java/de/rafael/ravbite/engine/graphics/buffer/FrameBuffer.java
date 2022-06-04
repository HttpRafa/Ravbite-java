/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-", "$today.year")2022. All rights reserved.
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

package de.rafael.ravbite.engine.graphics.buffer;

//------------------------------
//
// This class was developed by Rafael K.
// On 5/20/2022 at 2:36 PM
// In the project Ravbite
//
//------------------------------

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import java.nio.ByteBuffer;

public class FrameBuffer {

    private int frameBuffer;
    private int width, height;

    private int depthBuffer;

    private int colorTexture;
    private int depthTexture;

    public FrameBuffer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a new frameBuffer
     */
    public void create() {
        this.frameBuffer = GL30.glGenFramebuffers();

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        GL30.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);

        textureAttachment();

        depthBufferAttachment();
        depthTextureAttachment();
    }

    /**
     * Disposes of the framebuffer
     */
    public void dispose() {
        GL30.glDeleteFramebuffers(frameBuffer);
        GL11.glDeleteTextures(colorTexture);
        GL11.glDeleteTextures(depthTexture);
        GL30.glDeleteRenderbuffers(depthBuffer);
    }

    /**
     * Resizes the framebuffer to the specified size
     * @param width Width
     * @param height Height
     */
    public void resize(int width, int height) {
        dispose();

        this.width = width;
        this.height = height;

        create();
    }

    /**
     * Atomically binds the framebuffer and unbinds it at the end
     * @param runnable Task to run
     */
    public void use(Runnable runnable) {
        bind();
        runnable.run();
        unbind();
    }

    /**
     * Clear the framebuffer
     */
    public void clear() {
        GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Binds the framebuffer
     */
    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        GL11.glViewport(0, 0, width, height);
    }

    /**
     * Unbinds the framebuffer
     */
    public void unbind() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    /**
     * Creates the depthBugger attachment
     */
    private void depthBufferAttachment() {
        depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width,
                height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
                GL30.GL_RENDERBUFFER, depthBuffer);
    }

    /**
     * Creates the texture attachment
     */
    private void textureAttachment() {
        colorTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height,
                0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
                colorTexture, 0);
    }

    /**
     * Creates the depthTexture attachment
     */
    private void depthTextureAttachment() {
        depthTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height,
                0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
                depthTexture, 0);
    }

    /**
     * @return OpenGL framebuffer
     */
    public int getFrameBuffer() {
        return frameBuffer;
    }

    /**
     * @return Width of the framebuffer
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return Height of the framebuffer
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return OpenGL depthBuffer
     */
    public int getDepthBuffer() {
        return depthBuffer;
    }

    /**
     * @return OpenGL colorTexture
     */
    public int getColorTexture() {
        return colorTexture;
    }

    /**
     * @return OpenGL depthTexture
     */
    public int getDepthTexture() {
        return depthTexture;
    }

}
