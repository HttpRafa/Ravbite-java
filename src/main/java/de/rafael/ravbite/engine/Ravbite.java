package de.rafael.ravbite.engine;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/22/2022 at 5:12 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.window.EngineWindow;

import java.util.ArrayList;
import java.util.List;

public class Ravbite {

    private final List<EngineWindow> windows = new ArrayList<>();

    /**
     * Runs every window
     */
    public void run() {
        for (int i = 0; i < windows.size(); i++) {
            if(i == (windows.size() - 1)) {
                windows.get(i).run();
            } else {
                windows.get(i).runThreaded();
            }
        }
    }

    /**
     * Adds a window to the engine manager
     * @param window Window to add
     */
    public void addWindow(EngineWindow window) {
        windows.add(window);
    }

    /**
     *
     * @return All registered windows
     */
    public List<EngineWindow> getWindows() {
        return windows;
    }

}
