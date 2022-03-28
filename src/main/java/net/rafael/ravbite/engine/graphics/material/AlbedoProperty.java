package net.rafael.ravbite.engine.graphics.material;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/28/2022 at 7:19 PM
// In the project Ravbite
//
//------------------------------

import java.awt.*;

public class AlbedoProperty {

    private final Material material;

    private Integer textureId;
    private Color color;

    public AlbedoProperty(Material material, Color color) {
        this.material = material;
        this.color = color;
    }

    public AlbedoProperty texture(int id) {
        this.textureId = id;
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
    public Integer getTextureId() {
        if(textureId == null) {
            return material.getEngineWindow().getDefaultTexture();
        }
        return textureId;
    }

    /**
     * @return Color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets textureId
     * @param textureId New textureId
     */
    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    /**
     * Sets color
     * @param color New color
     */
    public void setColor(Color color) {
        this.color = color;
    }

}
