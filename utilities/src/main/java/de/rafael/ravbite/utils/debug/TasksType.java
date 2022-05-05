package de.rafael.ravbite.utils.debug;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/05/2022 at 1:52 PM
// In the project Ravbite
//
//------------------------------

public enum TasksType {

    LOOP_CLEAR_BUFFERS(0),
    LOOP_INPUT_UPDATE(1),
    LOOP_SCENE_RENDER_ALL(2),
    LOOP_SWAP_BUFFERS(3),
    LOOP_POLL_EVENTS(4),
    LOOP_STACK_TASKS(5),
    LOOP_DEBUG_WINDOWS(6);

    private final int id;

    TasksType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
