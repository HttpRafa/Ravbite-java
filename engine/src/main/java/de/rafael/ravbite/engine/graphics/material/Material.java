/*
 * Copyright (c) 2022.
 * All rights reserved.
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
package de.rafael.ravbite.engine.graphics.material;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 7:06 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.shader.standard.StandardShader;
import de.rafael.ravbite.engine.graphics.window.EngineWindow;

import java.awt.*;

public class Material {

    private final EngineWindow engineWindow;

    private Integer shaderId;
    private AlbedoProperty albedo;
    private float shineDamper = 10f; // TODO: Change to texture based
    private float reflectivity = 0f; // TODO: Change to texture based

    public Material(EngineWindow engineWindow) {
        this.engineWindow = engineWindow;
    }

    public Material shader(int id) {
        shaderId = id;
        return this;
    }

    public Material albedo(AlbedoProperty albedo) {
        this.albedo = albedo;
        return this;
    }

    public Material specular(float shineDamper, float reflectivity) {
        this.shineDamper = shineDamper;
        this.reflectivity = reflectivity;
        return this;
    }

    public Material create() {
        if(this.shaderId == null) {
            this.shaderId = engineWindow.getIdOfShader(StandardShader.class);
        }
        if(this.albedo == null) {
            this.albedo = new AlbedoProperty(this, new Color(0, 0, 0));
        }
        return this;
    }

    /**
     * @return EngineWindow
     */
    public EngineWindow getEngineWindow() {
        return engineWindow;
    }

    /**
     * @return ID of the shader the material is using
     */
    public int getShaderId() {
        return shaderId;
    }

    /**
     * @return AlbedoProperty
     */
    public AlbedoProperty getAlbedo() {
        return albedo;
    }

    /**
     * @return ShineDamper value
     */
    public float getShineDamper() {
        return shineDamper;
    }

    /**
     * @return Reflectivity value
     */
    public float getReflectivity() {
        return reflectivity;
    }

}
