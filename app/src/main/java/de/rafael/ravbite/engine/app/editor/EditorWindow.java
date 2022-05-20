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
package de.rafael.ravbite.engine.app.editor;

//------------------------------
//
// This class was developed by Rafael K.
// On 04/15/2022 at 12:57 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.Ravbite;
import de.rafael.ravbite.engine.app.editor.scene.EditorScene;
import de.rafael.ravbite.engine.app.editor.window.LeaveWindow;
import de.rafael.ravbite.engine.app.editor.window.edit.EngineView;
import de.rafael.ravbite.engine.app.editor.window.edit.InspectorWindow;
import de.rafael.ravbite.engine.app.editor.window.edit.MenuBar;
import de.rafael.ravbite.engine.app.editor.window.project.OpenProjectWindow;
import de.rafael.ravbite.engine.app.project.Project;
import de.rafael.ravbite.engine.app.project.SimpleProject;
import de.rafael.ravbite.engine.app.project.manager.ProjectManager;
import de.rafael.ravbite.engine.graphics.window.Window;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class EditorWindow extends Window {

    private final ProjectManager projectManager;
    private Project project;

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private LeaveWindow leaveWindow = null;

    public EditorWindow() throws IOException {
        super(1900, 1000);

        this.projectManager = new ProjectManager();
        this.projectManager.loadProjects();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                projectManager.storeProjects();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }));

    }

    @Override
    public void destroy() {
        super.destroy();

        imGuiGl3.dispose();
        imGuiGlfw.dispose();
    }

    @Override
    public void prepare() {
        addScene(new EditorScene(this));
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
        MenuBar menuBar = new MenuBar(this);
        InspectorWindow inspector = new InspectorWindow();
        EngineView engineView = new EngineView(this);
        OpenProjectWindow openProjectWindow = new OpenProjectWindow(this);

        while (true) {
            if(glfwWindowShouldClose(getWindow())) {
                if(project == null) break;
                glfwSetWindowShouldClose(getWindow(), false);
                leaveWindow = new LeaveWindow(this);
            }

            glClearColor(0.1f, 0.09f, 0.1f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            imGuiGlfw.newFrame();
            ImGui.newFrame();

            if(project != null) {
                menuBar.gui();
                inspector.gui();
                engineView.gui();
                if(leaveWindow != null) leaveWindow.gui();
            } else {
                openProjectWindow.gui();
            }

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
     * Opens the project
     * @param project Project to open
     */
    public void open(SimpleProject project) {
        this.project = projectManager.openProject(project);
        super.setTitle("Ravbite Editor / " + project.getName() + " # using Ravbite Engine v" + Ravbite.VERSION);
    }

    /**
     * Closes the project
     */
    public void closeProject() {
        projectManager.saveProject(project);
        project = null;
        super.setTitle("Ravbite Editor");
    }

    /**
     * @return ProjectManager for the editorInstance
     */
    public ProjectManager getProjectManager() {
        return projectManager;
    }

    /**
     * @return Currently opened window
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the leaveWindow | null = closed
     * @param leaveWindow LeaveWindow
     */
    public void setLeaveWindow(LeaveWindow leaveWindow) {
        this.leaveWindow = leaveWindow;
    }

}
