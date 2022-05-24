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
// On 05/24/2022 at 1:06 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.Editor;
import de.rafael.ravbite.engine.app.editor.manager.element.IGuiElement;
import de.rafael.ravbite.engine.app.editor.project.SimpleProject;
import de.rafael.ravbite.engine.app.editor.project.settings.EditorSettings;
import de.rafael.ravbite.engine.app.editor.project.settings.EngineSettings;
import de.rafael.ravbite.engine.app.editor.project.settings.GradleSettings;
import de.rafael.ravbite.engine.app.ui.CheckBoxList;
import imgui.ImGui;
import imgui.extension.imguifiledialog.ImGuiFileDialog;
import imgui.extension.imguifiledialog.flag.ImGuiFileDialogFlags;
import imgui.type.ImString;

import java.io.File;

public record OpenProjectElement(Editor editor) implements IGuiElement {

    public static ImString[] STRINGS = new ImString[] {
            new ImString(),
            new ImString(),
            new ImString(new File(".").getAbsolutePath()),
            new ImString()
    };
    public static CheckBoxList[] CHECKBOX_LIST = new CheckBoxList[] {
            new CheckBoxList(true, new String[]{"2D", "3D"}, new boolean[] {false, true}),
            new CheckBoxList(true, new String[]{"Groovy DSL", "Kotlin DSL"}, new boolean[] {true, false}),
            new CheckBoxList(true, new String[]{"Java", "Kotlin", "Groovy"}, new boolean[] {true, false, false})
    };

    public static float WIDTH = 1000F;
    public static float HEIGHT = 600F;

    @Override
    public boolean render() {
        ImGui.setNextWindowSize(WIDTH, HEIGHT);
        ImGui.begin("Open Project");

        SimpleProject[] simpleProjects = editor.getProjectManager().getProjects();
        if(simpleProjects != null) {
            for (int i = 0; i < simpleProjects.length; i++) {
                SimpleProject project = simpleProjects[i];
                if (ImGui.collapsingHeader(project.getName())) {
                    ImGui.text("Directory: " + project.getProjectDirectory().getAbsolutePath());
                    ImGui.text("Project File: " + project.getProjectFile().getAbsolutePath());
                    ImGui.separator();

                    if (ImGui.button("Open" + "\n".repeat(i))) {
                        editor.getProjectManager().openProject(i);
                    }

                    ImGui.sameLine();

                    if (ImGui.button("Delete" + "\n".repeat(i))) {
                        ImGui.openPopup("Delete project" + "\n".repeat(i));
                    }

                    ImGui.separator();
                }

                // Delete Popup
                if (ImGui.beginPopupModal("Delete project" + "\n".repeat(i))) {
                    ImGui.text("Name: " + project.getName());
                    ImGui.text("Directory: " + project.getProjectDirectory().getAbsolutePath());
                    ImGui.text("Project File: " + project.getProjectFile().getAbsolutePath());
                    ImGui.separator();
                    ImGui.text("Do you want to delete this project?");
                    ImGui.spacing();

                    if (ImGui.button("Yes")) {
                        editor.getProjectManager().deleteProject(i);
                    }

                    ImGui.sameLine();

                    if (ImGui.button("No")) {
                        ImGui.closeCurrentPopup();
                    }

                    ImGui.endPopup();
                }
            }
        }

        ImGui.spacing();

        if(ImGui.button("Create project")) {
            ImGui.openPopup("Create project");
        }

        if(ImGui.beginPopupModal("Create project")) {
            if(ImGui.inputText("Name", STRINGS[0])) {
                STRINGS[1].set("de.ravbite." + STRINGS[0].get().toLowerCase().trim().replaceAll("\\.", "").replaceAll(" ", "."));
            }

            ImGui.text(STRINGS[2].get());
            ImGui.sameLine();

            if(ImGui.button("Choose")) {
                ImGuiFileDialog.openDialog("browse-folder-key", "Choose Folder", null, ".", "", 1, 7, ImGuiFileDialogFlags.None);
            }
            if (ImGuiFileDialog.display("browse-folder-key", ImGuiFileDialogFlags.None, 200, 400, 800, 600)) {
                if (ImGuiFileDialog.isOk()) {
                    STRINGS[2].set(ImGuiFileDialog.getSelection().values().stream().findFirst().orElse(null));
                }
                ImGuiFileDialog.close();
            }

            ImGui.spacing();
            ImGui.separator();
            ImGui.spacing();

            ImGui.text("Engine Settings");
            CHECKBOX_LIST[0].render();

            ImGui.spacing();
            ImGui.separator();
            ImGui.spacing();

            ImGui.text("Gradle Settings");
            if (ImGui.inputText("Package", STRINGS[1])) {

            }
            ImGui.spacing();
            ImGui.text("Gradle DSL");
            CHECKBOX_LIST[1].render();

            ImGui.spacing();
            ImGui.text("Language");
            CHECKBOX_LIST[2].render();

            ImGui.spacing();
            ImGui.separator();
            ImGui.spacing();

            if (ImGui.button("Create")) {
                String name = STRINGS[0].get();
                String packageName = STRINGS[1].get();

                if(name.isBlank() || name.isEmpty()) {
                    STRINGS[3].set("Name is invalid");
                    ImGui.openPopup("Error");
                } else if(packageName.isBlank() || packageName.isEmpty()) {
                    STRINGS[3].set("Package is invalid");
                    ImGui.openPopup("Error");
                } else if(CHECKBOX_LIST[0].getOptionsValues()[0].get()) {
                    STRINGS[3].set("2D is not supported yet");
                    ImGui.openPopup("Error");
                } else if(!editor.getProjectManager().isNameFree(name)) {
                    STRINGS[3].set("Name is already in use");
                    ImGui.openPopup("Error");
                } else {
                    SimpleProject simpleProject = new SimpleProject(name, new File(STRINGS[2].get()), new File(STRINGS[2].get(), name + ".ravproject"));

                    EditorSettings editorSettings = new EditorSettings(null);
                    EngineSettings engineSettings = new EngineSettings(CHECKBOX_LIST[0].getOptionsValues()[0].get() ? 2 : 3);
                    GradleSettings gradleSettings = new GradleSettings(packageName, CHECKBOX_LIST[1].trueIndex(), CHECKBOX_LIST[2].trueIndex());

                    editor.getProjectManager().createProject(simpleProject, editorSettings, engineSettings, gradleSettings);

                    ImGui.closeCurrentPopup();
                }
            }

            ImGui.sameLine();

            if (ImGui.button("Cancel")) {
                ImGui.closeCurrentPopup();
            }

            if(ImGui.beginPopupModal("Error")) {
                ImGui.textColored(255, 50, 50, 255, STRINGS[3].get());

                ImGui.spacing();
                ImGui.separator();
                ImGui.spacing();

                if(ImGui.button("Close error")) {
                    ImGui.closeCurrentPopup();
                }

                ImGui.endPopup();
            }

            ImGui.endPopup();
        }

        ImGui.end();
        return false;
    }

}
