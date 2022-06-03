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

package de.rafael.ravbite.engine.app.editor.element.start;

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
import de.rafael.ravbite.engine.app.editor.task.types.PrimaryEditorTask;
import imgui.flag.ImGuiWindowFlags;
import imgui.internal.ImGui;

import java.util.ArrayList;
import java.util.List;

public class TaskPopup implements IGuiElement {

    private final Editor editor;

    public TaskPopup(Editor editor) {
        this.editor = editor;
    }

    private final List<EditorTask> displayedTasks = new ArrayList<>();

    private void handle(EditorTask editorTask) {
        if(editorTask.toDo() > 0 || editorTask.getRunnable() != null) {
            displayedTasks.add(editorTask);
        }
        if(editorTask.getChildTasks().length > 0) {
            handle(editorTask.getChildTasks()[editorTask.getRunningTask()]);
        }
    }

    @Override
    public boolean render() {
        displayedTasks.clear();
        if(editor.getTaskExecutor().getRunningTask() != null) {
            EditorTask editorTask = editor.getTaskExecutor().getRunningTask();
            if(editorTask instanceof PrimaryEditorTask primaryEditorTask) {
                handle(primaryEditorTask);
            }
        }

        if(displayedTasks.size() > 0) {
            if(ImGui.beginPopupModal("Busy", ImGuiWindowFlags.NoSavedSettings | ImGuiWindowFlags.AlwaysAutoResize)) {
                for (int i = 0; i < displayedTasks.size(); i++) {
                    EditorTask displayedTask = displayedTasks.get(i);
                    if(i == 0 && displayedTask instanceof PrimaryEditorTask primaryEditorTask) {
                        ImGui.text(primaryEditorTask.getTaskGroup().getName() + " (busy for " + (primaryEditorTask.runningFor() / 1000) + "s)...");
                        ImGui.spacing();
                        ImGui.progressBar(primaryEditorTask.percentage(), 800, 20);
                        if(primaryEditorTask.getChildTasks().length > 0) {
                            ImGui.spacing();
                            ImGui.separator();
                            ImGui.spacing();
                        }
                    } else {
                        ImGui.spacing();
                        String finishedString = "";
                        if(displayedTask.toDo() > 0) {
                            int toDo = (int) displayedTask.toDo();
                            int done = (int) displayedTask.done();
                            finishedString = "[" + done + "/" + toDo + "]";
                        }
                        ImGui.text(displayedTask.getDescription() + " " + finishedString);
                        if(displayedTask.toDo() > 0) {
                            ImGui.progressBar(displayedTask.percentage(), 800, 20);
                        } else if(displayedTask.getRunnable() != null) {
                            ImGui.sameLine();
                            ImGui.text("/");
                            ImGui.sameLine();
                            ImGui.textColored(50, 140, 50, 255, "Running...");
                        }
                        ImGui.spacing();
                    }
                }
                ImGui.endPopup();
            }
            ImGui.openPopup("Busy");
        }

        return false;
    }

}
