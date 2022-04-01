package de.rafael.ravbite.engine.graphics.components.light;

//------------------------------
//
// This class was developed by Rafael K.
// On 4/1/2022 at 3:43 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.Component;
import org.joml.Vector3f;

import java.awt.*;

public class LightComponent extends Component {

    private Color color;

    public LightComponent(Color color) {
        this.color = color;
    }

    /**
     * Sets the color of the light
     * @param color New light
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return Color of the light
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return Color as Vector3f
     */
    public Vector3f getColorAsVector() {
        return new Vector3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
    }

}
