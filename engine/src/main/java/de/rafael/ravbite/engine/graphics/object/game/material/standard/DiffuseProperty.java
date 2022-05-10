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
package de.rafael.ravbite.engine.graphics.object.game.material.standard;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/28/2022 at 7:19 PM
// In the project Ravbite
//
//------------------------------

import org.joml.Vector4f;

import java.awt.*;

public class DiffuseProperty {

    private final Material material;

    private Integer diffuseTextureId;
    private Color color;

    public DiffuseProperty(Material material, Color color) {
        this.material = material;
        this.color = color;
    }

    public DiffuseProperty texture(int id) {
        this.diffuseTextureId = id;
        return this;
    }

    /**
     * @return Material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * @return ID of the texture
     */
    public Integer getDiffuseTextureId() {
        if(diffuseTextureId == null) {
            return material.getEngineWindow().getDefaultTexture();
        }
        return diffuseTextureId;
    }

    /**
     * @return Color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return Color as Vector
     */
    public Vector4f getColorAsVector() {
        return new Vector4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    /**
     * Sets textureId
     * @param diffuseTextureId New textureId
     */
    public void setDiffuseTextureId(int diffuseTextureId) {
        this.diffuseTextureId = diffuseTextureId;
    }

    /**
     * Sets color
     * @param color New color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "DiffuseProperty{" +
                "material=" + material +
                ", diffuseTextureId=" + diffuseTextureId +
                ", color=" + color +
                '}';
    }

}
