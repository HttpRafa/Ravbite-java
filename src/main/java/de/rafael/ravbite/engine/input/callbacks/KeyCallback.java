package de.rafael.ravbite.engine.input.callbacks;

//------------------------------
//
// This class was developed by Rafael K.
// On 4/2/2022 at 1:32 PM
// In the project Ravbite
//
//------------------------------

public abstract class KeyCallback {

    private final Integer key;

    public KeyCallback(Integer key) {
        this.key = key;
    }

    public abstract void pressed(int key, long window, int scancode, int action, int mods);
    public abstract void released(int key, long window, int scancode, int action, int mods);

    public Integer getKey() {
        return key;
    }

}
