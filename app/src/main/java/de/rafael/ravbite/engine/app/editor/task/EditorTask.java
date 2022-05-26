/*
 * Copyright (c) 2022. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *         this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *     * Neither the name of the developer nor the names of its contributors
 *         may be used to endorse or promote products derived from this software
 *         without specific prior written permission.
 *     * Redistributions in source or binary form must keep the original package
 *         and class name.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.rafael.ravbite.engine.app.editor.task;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/25/2022 at 2:39 PM
// In the project Ravbite
//
//------------------------------

import org.apache.commons.lang3.ArrayUtils;

public class EditorTask {

    private Runnable runnable;

    private final String description;

    private EditorTask parentTask;

    private EditorTask[] childTasks = new EditorTask[0];
    private int runningTask;

    private long startTime;

    public EditorTask(String description, Runnable runnable) {
        this.runnable = runnable;
        this.description = description;
    }

    public EditorTask(String description) {
        this.description = description;
    }

    public EditorTask add(EditorTask editorTask) {
        childTasks = ArrayUtils.add(childTasks, editorTask);
        editorTask.setParentTask(this);
        return this;
    }

    public void execute() {
        this.startTime = System.currentTimeMillis();

        if(runnable != null) runnable.run();
        for (int i = 0; i < childTasks.length; i++) {
            runningTask = i;
            childTasks[i].execute();
        }
    }

    public float toDo() {
        return childTasks.length;
    }

    public float done() {
        if(childTasks.length > 0 && childTasks[runningTask].toDo() > 0) {
            return runningTask + childTasks[runningTask].percentage();
        }
        return runningTask;
    }

    public float percentage() {
        if(childTasks.length == 1) {
            return childTasks[0].percentage();
        }
        return done() / toDo();
    }

    public long runningFor() {
        return System.currentTimeMillis() - startTime;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public String getDescription() {
        return description;
    }

    public void setParentTask(EditorTask parentTask) {
        this.parentTask = parentTask;
    }

    public EditorTask getParentTask() {
        return parentTask;
    }

    public EditorTask[] getChildTasks() {
        return childTasks;
    }

    public int getRunningTask() {
        return runningTask;
    }

}
