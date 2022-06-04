/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-", "$today.year")2022. All rights reserved.
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

package de.rafael.ravbite.engine.app.editor.window;

//------------------------------
//
// This class was developed by Rafael K.
// On 5/21/2022 at 8:10 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.Editor;
import de.rafael.ravbite.engine.app.editor.manager.element.IGuiElement;
import de.rafael.ravbite.engine.app.editor.manager.theme.style.Theme;
import de.rafael.ravbite.engine.graphics.objects.scene.Scene;
import de.rafael.ravbite.engine.graphics.window.Window;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.apache.commons.lang3.ArrayUtils;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class EditorWindow extends Window {

    private final Editor editor;

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private boolean exit = false;

    public EditorWindow(Editor editor) {
        super(1900, 1000);

        this.editor = editor;
    }

    @Override
    public void destroy() {
        super.destroy();

        imGuiGl3.dispose();
        imGuiGlfw.dispose();
    }

    @Override
    public void prepare() {
        addScene(new Scene(this, "~") {
            @Override
            public void prepare() {

            }

            @Override
            public void dispose() {

            }
        });
    }

    @Override
    public void initialize() {
        String glslVersion = "#version 130";

        super.initialize();
        glfwMaximizeWindow(getWindow());
        super.setTitle("Ravbite Editor <OpenGL " + super.getGlVersion() + ">");

        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);

        imGuiGlfw.init(getWindow(), true);
        imGuiGl3.init(glslVersion);
    }

    @Override
    public void loop() {
        glClearColor(0.1f, 0.09f, 0.1f, 1.0f);

        while (!exit) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            imGuiGlfw.newFrame();
            ImGui.newFrame();
            ImGui.dockSpaceOverViewport(ImGui.getMainViewport());

            if(glfwWindowShouldClose(getWindow())) {
                glfwSetWindowShouldClose(getWindow(), false);
                ImGui.openPopup("Exit");
            }
            exitPopup();

            IGuiElement[] elements = editor.getElementManager().getElements();
            int[] toRemove = new int[0];
            for (int i = 0; i < elements.length; i++) {
                Theme theme = editor.getThemeManager().getCurrentTheme();
                if(elements[i].render()) {
                    toRemove = ArrayUtils.add(toRemove, i);
                }
            }
            for (int i : toRemove) {
                editor.getElementManager().stopDrawing(i);
            }

            super.getThreadExecutor().executeAllTasksInStack();
            editor.getTaskExecutor().executeNextTask();

            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                glfwMakeContextCurrent(backupWindowPtr);
            }

            glfwSwapBuffers(getWindow());
            glfwPollEvents();
        }
    }

    private void exitPopup() {
        if(ImGui.beginPopupModal("Exit", ImGuiWindowFlags.AlwaysAutoResize)) {
            ImGui.text("Do you want to close the editor?");
            ImGui.spacing();
            ImGui.separator();
            ImGui.spacing();
            if(ImGui.button("Yes")) {
                exit = true;
            }
            ImGui.sameLine();
            if(ImGui.button("No")) {
                ImGui.closeCurrentPopup();
            }
            ImGui.endPopup();
        }
    }

    /**
     * @return Editor
     */
    public Editor getEditor() {
        return editor;
    }

}
