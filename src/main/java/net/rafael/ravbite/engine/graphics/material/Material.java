package net.rafael.ravbite.engine.graphics.material;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 7:06 PM
// In the project Ravbite
//
//------------------------------

import net.rafael.ravbite.engine.graphics.shader.standard.StandardShader;
import net.rafael.ravbite.engine.graphics.window.EngineWindow;

public class Material {

    private final int shaderId;

    public Material(int shaderId) {
        this.shaderId = shaderId;
    }

    public Material(EngineWindow engineWindow) {
        this.shaderId = engineWindow.getIdOfShader(StandardShader.class);
    }

    /**
     * @return ID of the shader the material is using
     */
    public int getShaderId() {
        return shaderId;
    }

}
