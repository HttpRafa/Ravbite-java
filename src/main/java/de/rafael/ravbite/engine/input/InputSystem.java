package de.rafael.ravbite.engine.input;

//------------------------------
//
// This class was developed by Rafael K.
// On 4/2/2022 at 1:32 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.window.EngineWindow;
import de.rafael.ravbite.engine.input.callbacks.KeyCallback;

import java.util.ArrayList;
import java.util.List;

public class InputSystem {

    private EngineWindow engineWindow;
    private final List<KeyCallback> callbacks = new ArrayList<>();

    public void listen(KeyCallback keyCallback) {

    }

}
