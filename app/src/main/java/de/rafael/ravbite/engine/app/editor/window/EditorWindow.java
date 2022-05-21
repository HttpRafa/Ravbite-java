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
import de.rafael.ravbite.engine.graphics.object.scene.Scene;
import de.rafael.ravbite.engine.graphics.window.Window;
import de.rafael.ravbite.utils.gui.IGui;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class EditorWindow extends Window {

    private final Editor editor;

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

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
        super.setTitle("Ravbite Editor");

        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);

        imGuiGlfw.init(getWindow(), true);
        imGuiGl3.init(glslVersion);
    }

    @Override
    public void loop() {
        glClearColor(0.1f, 0.09f, 0.1f, 1.0f);

        while (!glfwWindowShouldClose(getWindow())) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            imGuiGlfw.newFrame();
            ImGui.newFrame();

            List<IGuiElement> elements = editor.getElementManager().getElements();
            elements.removeIf(IGuiElement::render);

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

    /**
     * @return Editor
     */
    public Editor getEditor() {
        return editor;
    }

}