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
