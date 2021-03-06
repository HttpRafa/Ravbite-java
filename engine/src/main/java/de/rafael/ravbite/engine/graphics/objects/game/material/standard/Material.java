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
package de.rafael.ravbite.engine.graphics.objects.game.material.standard;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 7:06 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.objects.game.material.IMaterial;
import de.rafael.ravbite.engine.graphics.shader.standard.StandardShader;
import de.rafael.ravbite.engine.graphics.view.EngineView;

import java.awt.*;

public class Material implements IMaterial {

    private final EngineView engineView;

    private Integer shaderId;
    private DiffuseProperty diffuse;
    private float shineDamper = 10f; // TODO: Change to texture based
    private float reflectivity = 0f; // TODO: Change to texture based

    public Material(EngineView engineView) {
        this.engineView = engineView;
    }

    public Material shader(int id) {
        shaderId = id;
        return this;
    }

    public Material diffuse(DiffuseProperty albedo) {
        this.diffuse = albedo;
        return this;
    }

    public Material specular(float shineDamper, float reflectivity) {
        this.shineDamper = shineDamper;
        this.reflectivity = reflectivity;
        return this;
    }

    public Material create() {
        if(this.shaderId == null) {
            this.shaderId = engineView.getIdOfShader(StandardShader.class);
        }
        if(this.diffuse == null) {
            this.diffuse = new DiffuseProperty(this, new Color(0, 0, 0));
        }
        return this;
    }

    /**
     * @return EngineView
     */
    @Override
    public EngineView getEngineView() {
        return engineView;
    }

    /**
     * @return ID of the shader the material is using
     */
    @Override
    public int getShaderId() {
        return shaderId;
    }

    /**
     * @return DiffuseProperty
     */
    public DiffuseProperty getDiffuse() {
        return diffuse;
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
