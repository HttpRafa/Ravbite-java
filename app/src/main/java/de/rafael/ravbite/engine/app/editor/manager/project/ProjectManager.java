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

package de.rafael.ravbite.engine.app.editor.manager.project;

//------------------------------
//
// This class was developed by Rafael K.
// On 5/21/2022 at 8:21 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.Editor;
import de.rafael.ravbite.engine.app.editor.element.OpenProjectElement;
import de.rafael.ravbite.engine.app.editor.project.ProjectFile;
import de.rafael.ravbite.engine.app.editor.project.SimpleProject;
import de.rafael.ravbite.engine.app.editor.project.settings.EditorSettings;
import de.rafael.ravbite.engine.app.editor.project.settings.EngineSettings;
import de.rafael.ravbite.engine.app.editor.project.settings.GradleSettings;
import de.rafael.ravbite.engine.app.editor.task.TaskGroup;
import de.rafael.ravbite.engine.app.editor.task.types.GroupTask;
import de.rafael.ravbite.engine.app.editor.task.types.PrimaryTask;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ProjectManager {

    public static final File projectsFile = new File("projects.ravbite");

    private final Editor editor;

    private SimpleProject[] projects;

    private SimpleProject openProject;

    public ProjectManager(Editor editor) {
        this.editor = editor;
    }

    /**
     * Updates the projectManager
     */
    private OpenProjectElement openProjectElement;
    public void update() {
        if(openProject == null) {
            // Check of openProject window is opened
            if(openProjectElement == null) {
                openProjectElement = (OpenProjectElement) editor.getElementManager().startDrawing(new OpenProjectElement(editor));
            }
        } else {
            // Close openProject window if opened
            if(openProjectElement != null) {
                editor.getElementManager().stopDrawing(openProjectElement);
                openProjectElement = null;
            }
        }
    }

    /**
     * Opens the project from the array
     * @param i Index of the project
     */
    public void openProject(int i) {
        editor.handleError(new RuntimeException("Project opened " + i));
    }

    /**
     * Creates a new project
     * @param simpleProject Simple project
     * @param editorSettings Editor settings
     * @param engineSettings Engine settings
     * @param gradleSettings Gradle settings
     */
    public void createProject(SimpleProject simpleProject, EditorSettings editorSettings, EngineSettings engineSettings, GradleSettings gradleSettings) {
        GroupTask writeProjectFile = GroupTask.create(TaskGroup.PROJECT_MANAGER, "Writing project files...", () -> {
                ProjectFile projectFile = new ProjectFile(simpleProject, editorSettings, engineSettings, gradleSettings);
                projectFile.writeToFile(simpleProject.getProjectFile());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
        });
        GroupTask createDirectories = GroupTask.create(TaskGroup.PROJECT_MANAGER, "Creating directories...", () -> {
            if (new File(simpleProject.getProjectDirectory(), "assets/").mkdirs())
                System.out.println("Assets directory created");
            if (new File(simpleProject.getProjectDirectory(), "scenes/").mkdirs())
                System.out.println("Scenes directory created");
            if (new File(simpleProject.getProjectDirectory(), "src/").mkdirs())
                System.out.println("Sources directory created");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        GroupTask registeringProject = GroupTask.create(TaskGroup.EDITOR, "Registering project...", () -> {
                projects = ArrayUtils.add(projects, simpleProject);
                storeProjects();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
        });

        PrimaryTask primaryTask = new PrimaryTask(writeProjectFile, createDirectories, registeringProject);
        editor.getTaskExecutor().execute(primaryTask);
    }

    /**
     * Deletes a project from the array
     * @param i Index of the project
     */
    public void deleteProject(int i) {
        projects = ArrayUtils.remove(projects, i);
        storeProjects();
    }

    /**
     * Checks if a name is already in use
     * @param name Name
     * @return If it is in use
     */
    public boolean isNameFree(String name) {
        return Arrays.stream(projects).noneMatch(item -> item.getName().equalsIgnoreCase(name));
    }

    /**
     * Loads all project as simple version
     */
    public void loadProjects() {
        try {
            if(projectsFile.exists()) {
                byte[] data = FileUtils.readFileToByteArray(projectsFile);
                projects = SerializationUtils.deserialize(data);
                int[] brokenProjects = new int[0];
                for (int i = 0; i < projects.length; i++) {
                    SimpleProject project = projects[i];
                    if(!project.getProjectFile().exists()) {
                        brokenProjects = ArrayUtils.add(brokenProjects, i);
                    } else if(!project.getProjectDirectory().exists()) {
                        brokenProjects = ArrayUtils.add(brokenProjects, i);
                    }
                }
                projects = ArrayUtils.removeAll(projects, brokenProjects);
            } else {
                projects = new SimpleProject[0];
            }
        } catch (Exception exception) {
            editor.handleError(exception);
        }
    }

    /**
     * Stores all projects as simple version
     */
    public void storeProjects() {
        try {
            byte[] data = SerializationUtils.serialize(projects);
            FileUtils.writeByteArrayToFile(projectsFile, data);
        } catch (IOException exception) {
            editor.handleError(exception);
        }
    }

    /**
     * @return Projects as simple version
     */
    public SimpleProject[] getProjects() {
        return projects;
    }

    /**
     * @return Project that is currently selected/opened
     */
    public SimpleProject getOpenProject() {
        return openProject;
    }

}
