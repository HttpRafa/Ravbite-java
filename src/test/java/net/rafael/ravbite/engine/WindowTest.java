package net.rafael.ravbite.engine;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/24/2022 at 9:42 PM
// In the project Ravbite
//
//------------------------------

import net.rafael.ravbite.engine.window.TestWindow;
import org.junit.jupiter.api.Test;

public class WindowTest {

    @Test
    public void windowTest() {
        Ravbite ravbite = new Ravbite();
        ravbite.addWindow(new TestWindow());
        ravbite.run();
    }

}
