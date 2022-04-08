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
package de.rafael.ravbite.engine.graphics.object.game.terrain;

//------------------------------
//
// This class was developed by Rafael K.
// On 04/08/2022 at 7:09 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.terrain.TerrainGridComponent;
import de.rafael.ravbite.engine.graphics.object.game.material.Material;
import de.rafael.ravbite.engine.graphics.object.game.mesh.Mesh;
import de.rafael.ravbite.engine.graphics.object.game.mesh.MeshGenerator;

public class Terrain {

    private float x;
    private float y;

    private final Mesh mesh;
    private Material material;

    public Terrain(float gridX, float gridY, Material material, TerrainGridComponent terrainGridComponent) {
        this.x = x * terrainGridComponent.getTerrainSize();
        this.y = y * terrainGridComponent.getTerrainSize();
        this.mesh = MeshGenerator.generateSimpleTerrainMesh(terrainGridComponent.getVertexCount(), terrainGridComponent.getTerrainSize());
    }

    /**
     * @return X coords of the terrain
     */
    public float getX() {
        return x;
    }

    /**
     * @return Y coords of the terrain
     */
    public float getY() {
        return y;
    }

    /**
     * @return Material of the terrain
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * @return Mesh of the terrain
     */
    public Mesh getMesh() {
        return mesh;
    }

}
