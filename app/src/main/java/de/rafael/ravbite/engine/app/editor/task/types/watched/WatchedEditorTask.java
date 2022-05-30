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

package de.rafael.ravbite.engine.app.editor.task.types.watched;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/30/2022 at 4:33 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.Editor;
import de.rafael.ravbite.engine.app.editor.task.EditorTask;
import de.rafael.ravbite.utils.method.MethodCallback;

public class WatchedEditorTask extends EditorTask {

    private final MethodCallback<TaskWatcher> watchedRunnable;
    private final TaskWatcher taskWatcher = new TaskWatcher();

    public WatchedEditorTask(String description, MethodCallback<TaskWatcher> watchedRunnable) {
        super(description);

        this.watchedRunnable = watchedRunnable;
    }

    @Override
    public void execute() {
        try {
            watchedRunnable.provide(taskWatcher);
        } catch (Exception exception) {
            Editor.getInstance().handleError(exception);
        }

        super.execute();
    }

    @Override
    public float done() {
        return taskWatcher.done;
    }

    @Override
    public float toDo() {
        return taskWatcher.toDo;
    }

    public static class TaskWatcher {

        private long toDo = 0;
        private long done = 0;

        public void done(long value) {
            done = value;
        }

        public void toDo(long value) {
            toDo = value;
        }

    }

}
