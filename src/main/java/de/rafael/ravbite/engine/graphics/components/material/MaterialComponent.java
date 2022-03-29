package de.rafael.ravbite.engine.graphics.components.material;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 7:05 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.Component;
import de.rafael.ravbite.engine.graphics.material.Material;

public class MaterialComponent extends Component {

    private Material material;

    public MaterialComponent(Material material) {
        this.material = material;
    }

    /**
     * @return Material of the GameObject
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets to material of the GameObject
     * @param material New Material
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

}
