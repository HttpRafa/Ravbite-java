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

package de.rafael.ravbite.utils.performance;

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

    /**
     * Logs how long a task takes
     * @param type Type of the task
     * @param runnable Task
     */
    // TODO: Optimize
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

    /**
     * Clears the max value
     */
    public void clearMax() {
        for (ExecutedTask task : tasks) {
            task.setMaxTime(0);
        }
    }

    /**
     * @return All tasks
     */
    public List<ExecutedTask> getTasks() {
        return tasks;
    }

}
