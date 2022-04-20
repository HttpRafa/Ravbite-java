/*
 * Copyright (c) 2022.
 * All rights reserved.
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
// On 04/16/2022 at 8:19 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.EditorWindow;
import de.rafael.ravbite.utils.gui.IGui;
import imgui.ImGui;
import imgui.extension.imguifiledialog.ImGuiFileDialog;
import imgui.extension.imguifiledialog.flag.ImGuiFileDialogFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;

import java.io.File;

public class CreateProjectWindow implements IGui {

    private final EditorWindow editorWindow;

    private long userData = 0;

    public CreateProjectWindow(EditorWindow editorWindow) {
        this.editorWindow = editorWindow;
    }

    private String pathString = new File(".").getAbsolutePath();
    private ImString nameString = new ImString();

    private ImBoolean checkbox2D = new ImBoolean();
    private ImBoolean checkbox3D = new ImBoolean(true);

    private String errorString = null;


    @Override
    public void gui() {
        if(ImGui.beginPopupModal("Create Project")) {
            if (ImGui.inputText("Name", nameString)) {

            }
            ImGui.text(pathString);
            ImGui.sameLine();
            if (ImGui.button("Choose")) {
                ImGuiFileDialog.openDialog("browse-folder-key", "Choose Folder", null, ".", "", 1, 7, ImGuiFileDialogFlags.None);
            }
            if (ImGuiFileDialog.display("browse-folder-key", ImGuiFileDialogFlags.None, 200, 400, 800, 600)) {
                if (ImGuiFileDialog.isOk()) {
                    pathString = ImGuiFileDialog.getSelection().values().stream().findFirst().orElse(null);
                    userData = ImGuiFileDialog.getUserDatas();
                }
                ImGuiFileDialog.close();
            }

            ImGui.spacing();
            ImGui.separator();
            ImGui.spacing();
            ImGui.text("Engine Settings");

            if(ImGui.checkbox("2D", checkbox2D)) {
                if(checkbox2D.get() && checkbox3D.get()) {
                    checkbox3D.set(false);
                }
                if(!checkbox2D.get() && !checkbox3D.get()) {
                    checkbox3D.set(true);
                }
            }
            ImGui.sameLine();
            if(ImGui.checkbox("3D", checkbox3D)) {
                if(checkbox2D.get() && checkbox3D.get()) {
                    checkbox2D.set(false);
                }
                if(!checkbox2D.get() && !checkbox3D.get()) {
                    checkbox2D.set(true);
                }
            }

            ImGui.spacing();
            ImGui.separator();
            ImGui.spacing();
            if (ImGui.button("Create")) {
                String name = nameString.get();

                if(name.isBlank() || name.isEmpty()) {
                    errorString = "Name is invalid";
                } else if(checkbox2D.get()) {
                    errorString = "2D is not supported yet";
                } else if(!editorWindow.getProjectManager().isNameFree(name)) {
                    errorString = "Name is already in use";
                } else {
                    if(editorWindow.getProjectManager().createProject(name, pathString, checkbox2D.get() ? 2 : 3)) {
                        ImGui.closeCurrentPopup();
                    }
                }
            }
            ImGui.sameLine();
            if (ImGui.button("Cancel")) {
                ImGui.closeCurrentPopup();
            }
            if(errorString != null) ImGui.textColored(255, 50, 50, 255, errorString);
            ImGui.endPopup();
        }

    }

    public EditorWindow getEditorWindow() {
        return editorWindow;
    }

    public String getPathString() {
        return pathString;
    }

    public long getUserData() {
        return userData;
    }

}
