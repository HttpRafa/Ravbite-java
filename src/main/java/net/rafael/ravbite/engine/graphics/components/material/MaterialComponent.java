package net.rafael.ravbite.engine.graphics.components.material;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/26/2022 at 7:05 PM
// In the project Ravbite
//
//------------------------------

import net.rafael.ravbite.engine.graphics.components.Component;
import net.rafael.ravbite.engine.graphics.material.Material;
import net.rafael.ravbite.engine.graphics.object.game.GameObject;

public class MaterialComponent extends Component {

    private Material material;

    public MaterialComponent(GameObject gameObject, Material material) {
        super(gameObject);
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
