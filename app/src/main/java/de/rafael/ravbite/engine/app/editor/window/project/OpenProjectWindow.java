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
package de.rafael.ravbite.engine.app.editor.window.project;

//------------------------------
//
// This class was developed by Rafael K.
// On 04/16/2022 at 2:12 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.EditorWindow;
import de.rafael.ravbite.engine.app.project.SimpleProject;
import de.rafael.ravbite.utils.gui.IGui;
import imgui.ImGui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OpenProjectWindow implements IGui {

    private final EditorWindow editorWindow;
    private final CreateProjectWindow createProjectWindow;

    public OpenProjectWindow(EditorWindow editorWindow) {
        this.editorWindow = editorWindow;
        createProjectWindow = new CreateProjectWindow(editorWindow);
    }

    @Override
    public void gui() {
        ImGui.begin("Open Project");

        for (int i = 0; i < editorWindow.getProjectManager().getProjects().length; i++) {
            SimpleProject project = editorWindow.getProjectManager().getProjects()[i];
            boolean deleteProject = false;
            if (ImGui.collapsingHeader(project.getName())) {
                ImGui.text("Path: " + project.getPath());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
                ImGui.text("Last used: " + simpleDateFormat.format(new Date(project.getLastUsed())));
                ImGui.separator();

                if (ImGui.button("Open" + "\n".repeat(i))) {
                    try {
                        editorWindow.open(project);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
                ImGui.sameLine();
                if (ImGui.button("Delete" + "\n".repeat(i))) {
                    ImGui.openPopup("Delete Project" + "\n".repeat(i));
                }
            }
            if(ImGui.beginPopupModal("Delete Project" + "\n".repeat(i))) {
                ImGui.text("Path: " + project.getPath());

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
                ImGui.text("Last used: " + simpleDateFormat.format(new Date(project.getLastUsed())));
                ImGui.separator();
                ImGui.text("Do you want to delete this project?");
                ImGui.spacing();
                if(ImGui.button("Yes")) {
                    deleteProject = true;
                }
                ImGui.sameLine();
                if(ImGui.button("No")) {
                    ImGui.closeCurrentPopup();
                }
                ImGui.endPopup();
            }
            if(deleteProject) editorWindow.getProjectManager().deleteProject(project.getName());
        }

        ImGui.spacing();
        if (ImGui.button("Create Project")) {
            ImGui.openPopup("Create Project");
        }
        createProjectWindow.gui();

        ImGui.end();

    }

}
