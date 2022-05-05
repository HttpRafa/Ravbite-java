package de.rafael.ravbite.utils.debug;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/05/2022 at 1:52 PM
// In the project Ravbite
//
//------------------------------

public class ExecutedTask {

    private final TasksType tasks;
    private double timeTook = 0;
    private double maxTime = 0;

    public ExecutedTask(TasksType tasks) {
        this.tasks = tasks;
    }

    public TasksType getTasks() {
        return tasks;
    }

    public void setTimeTook(double timeTook) {
        this.timeTook = timeTook;
        if(this.timeTook > this.maxTime) {
            this.maxTime = this.timeTook;
        }
    }

    public double getTimeTook() {
        return timeTook;
    }

    public void setMaxTime(double maxTime) {
        this.maxTime = maxTime;
    }

    public double getMaxTime() {
        return maxTime;
    }

}
