package de.rafael.ravbite.utils.performance;

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

    /**
     * @return Type of the task
     */
    public TasksType getTasks() {
        return tasks;
    }

    /**
     * Sets how long the task took
     * @param timeTook New time
     */
    public void setTimeTook(double timeTook) {
        this.timeTook = timeTook;
        if(this.timeTook > this.maxTime) {
            this.maxTime = this.timeTook;
        }
    }

    /**
     * @return How long a task takes
     */
    public double getTimeTook() {
        return timeTook;
    }

    /**
     * Sets the maxTime
     * @param maxTime New time
     */
    public void setMaxTime(double maxTime) {
        this.maxTime = maxTime;
    }

    /**
     * @return Time the task took max
     */
    public double getMaxTime() {
        return maxTime;
    }

}
