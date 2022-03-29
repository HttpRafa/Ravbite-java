package de.rafael.ravbite.engine.graphics.components.camera;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/25/2022 at 4:13 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.components.Component;
import de.rafael.ravbite.engine.graphics.object.game.GameObject;
import org.apache.commons.lang3.ArrayUtils;

public class CameraComponent extends Component {

    private int[] renderLayers;

    public CameraComponent() {
        this.renderLayers = new int[]{0};
    }

    public CameraComponent(int... renderLayers) {
        this.renderLayers = renderLayers;
    }

    /**
     * Called if a frame is render on this camera
     */
    public void startRendering() {
        // TODO: Render to Camera FBO
    }

    /**
     * Called if a frame is finished rendering on this camera
     */
    public void stopRendering() {

    }

    /**
     * Check if the camera is responsible to render this GameObject
     * @param gameObject GameObject
     * @return If the camera is responsible
     */
    public boolean isResponsableFor(GameObject gameObject) {
        return isResponsableFor(gameObject.getRenderLayer());
    }

    /**
     * Check if the camera is responsible to render this GameObject
     * @param layer LayerId
     * @return If the camera is responsible
     */
    public boolean isResponsableFor(int layer) {
        return ArrayUtils.contains(renderLayers, layer);
    }

    /**
     * @return Layers that the camera renders
     */
    public int[] getRenderLayers() {
        return renderLayers;
    }

    /**
     * Sets the renderLayers
     * @param renderLayers New renderLayers
     */
    public void setRenderLayers(int[] renderLayers) {
        this.renderLayers = renderLayers;
    }

}