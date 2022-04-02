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
    private boolean global = false;

    public KeyCallback() {
        this.key = null;
    }

    /**
     * @param key GLFW.GLFW_KEY_***
     */
    public KeyCallback(Integer key) {
        this.key = key;
    }

    public abstract void pressed(int key, long window, int scancode, int action, int mods);
    public abstract void released(int key, long window, int scancode, int action, int mods);

    /**
     * @return Key to listen for
     */
    public Integer getKey() {
        return key;
    }

    /**
     * Sets the global value
     * @param global If true, this callback is not deleted when the scene is changed
     */
    public void setGlobal(boolean global) {
        this.global = global;
    }

    /**
     * @return If true, this callback is not deleted when the scene is changed
     */
    public boolean isGlobal() {
        return global;
    }

}
