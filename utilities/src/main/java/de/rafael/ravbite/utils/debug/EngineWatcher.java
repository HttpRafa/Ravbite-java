package de.rafael.ravbite.utils.debug;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/05/2022 at 1:51 PM
// In the project Ravbite
//
//------------------------------

import java.util.ArrayList;
import java.util.List;

public class EngineWatcher {

    private final List<ExecutedTask> tasks = new ArrayList<>();

    public EngineWatcher() {
        for (TasksType value : TasksType.values()) {
            tasks.add(new ExecutedTask(value));
        }
    }

    public void update(TasksType type, Runnable runnable) {
        long start = System.nanoTime();
        runnable.run();
        long took = System.nanoTime() - start;
        for (ExecutedTask task : tasks) {
            if(task.getTasks() == type) {
                task.setTimeTook(took / 1000000d);
                break;
            }
        }
    }

    public void clearMax() {
        for (ExecutedTask task : tasks) {
            task.setMaxTime(0);
        }
    }

    public List<ExecutedTask> getTasks() {
        return tasks;
    }

}
