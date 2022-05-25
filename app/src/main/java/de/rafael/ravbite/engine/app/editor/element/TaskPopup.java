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

package de.rafael.ravbite.engine.app.editor.element;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/25/2022 at 2:43 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.Editor;
import de.rafael.ravbite.engine.app.editor.manager.element.IGuiElement;
import de.rafael.ravbite.engine.app.editor.task.EditorTask;
import de.rafael.ravbite.engine.app.editor.task.types.GroupTask;
import de.rafael.ravbite.engine.app.editor.task.types.PrimaryTask;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class TaskPopup implements IGuiElement {

    private final Editor editor;

    public TaskPopup(Editor editor) {
        this.editor = editor;
    }

    private float targetProtestantize = 0;
    private float displayProtestantize = 0;

    @Override
    public boolean render() {

        if(editor.getTaskExecutor().getRunningTask() != null) {
            EditorTask editorTask = editor.getTaskExecutor().getRunningTask();
            if(editorTask instanceof PrimaryTask primaryTask) {
                if(primaryTask.getRunningTask() != null) {
                    GroupTask runningTask = primaryTask.getRunningTask();
                    targetProtestantize = ((float) primaryTask.getRunningIndex()) / ((float) (primaryTask.getTasks().length));

                    if(targetProtestantize > displayProtestantize) {
                        float step = targetProtestantize - displayProtestantize;
                        if(step > 0.0065f) {
                            step = 0.0065f;
                        }
                        displayProtestantize += step;
                    } else if(targetProtestantize < displayProtestantize) {
                        displayProtestantize = 0f;
                    }

                    ImGui.setNextWindowSize(600, 100);
                    if(ImGui.beginPopupModal("Busy", ImGuiWindowFlags.NoSavedSettings | ImGuiWindowFlags.NoResize)) {
                        ImGui.text(runningTask.getTaskGroup().getName() + " (busy for " + (primaryTask.runningFor() / 1000) + "s)...");

                        ImGui.spacing();
                        ImGui.separator();
                        ImGui.spacing();

                        ImGui.progressBar(displayProtestantize);
                        ImGui.text(runningTask.getAction());

                        ImGui.endPopup();
                    }
                    ImGui.openPopup("Busy");
                }
            }
        }

        return false;
    }

}
