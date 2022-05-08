package de.rafael.ravbite.engine.sound;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/08/2022 at 12:18 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.window.EngineWindow;
import org.lwjgl.openal.AL10;

public class SoundSystem {

    private final EngineWindow engineWindow;

    private long device;
    private long context;

    public SoundSystem(EngineWindow engineWindow) {
        this.engineWindow = engineWindow;
    }

    public SoundSystem distanceModel(int model) {
        AL10.alDistanceModel(model);
        return this;
    }

    /**
     * Initializes the soundEngine
     * @return SoundSystem
     */
    public SoundSystem initialize() {
        String defaultDevice = engineWindow.getUtils().alGetDefaultAudioDevice();
        device = engineWindow.getUtils().alOpenDevice(defaultDevice);

        context = engineWindow.getUtils().alInit(device);
        return this;
    }

    /**
     * Destroys the soundEngine
     */
    public void destroy() {
        engineWindow.getUtils().alDestroy(context, device);
    }

    /**
     * @return Device as OpenAL ID
     */
    public long getDevice() {
        return device;
    }

    /**
     * @return OpenAL context
     */
    public long getContext() {
        return context;
    }

    /**
     * @return EngineWindow
     */
    public EngineWindow getEngineWindow() {
        return engineWindow;
    }

}
