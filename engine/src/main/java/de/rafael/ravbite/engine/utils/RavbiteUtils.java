package de.rafael.ravbite.engine.utils;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/07/2022 at 2:50 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.utils.WindowUtils;
import de.rafael.ravbite.engine.graphics.window.EngineWindow;
import de.rafael.ravbite.engine.sound.utils.SoundUtils;

/**
 *
 */
public class RavbiteUtils extends SoundUtils {

    private static final RavbiteUtils UTILS_INSTANCE = new RavbiteUtils();

    public RavbiteUtils() {
        super(null);
    }

    public RavbiteUtils(EngineWindow engineWindow) {
        super(engineWindow);
    }

    // Utils Methods


    /**
     * Returns an utils instance without a engineWindow !!No GLUtils usable!!
     * @return Utils instance
     */
    public static RavbiteUtils use() {
        return UTILS_INSTANCE;
    }

    /**
     * Returns an utils instance with a engineWindow !!GLUtils usable!!
     * @param engineWindow EngineWindow to use
     * @return Utils instance
     */
    public static RavbiteUtils use(EngineWindow engineWindow) {
        return new RavbiteUtils(engineWindow);
    }

}
