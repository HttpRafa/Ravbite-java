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
package de.rafael.ravbite.engine.app.project;

//------------------------------
//
// This class was developed by Rafael K.
// On 04/15/2022 at 7:13 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.EditorWindow;
import de.rafael.ravbite.engine.app.editor.scene.EditorScene;
import de.rafael.ravbite.engine.app.project.gradle.GradleManager;
import de.rafael.ravbite.engine.app.project.storage.ProjectStorage;
import de.rafael.ravbite.engine.app.project.storage.StoredScene;
import de.rafael.ravbite.engine.graphics.object.scene.Scene;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Project extends SimpleProject {

    private final EditorWindow editorWindow;

    private ProjectStorage projectStorage;
    private Scene[] scenes;

    public Project(EditorWindow editorWindow, String name, String path) {
        super(name, path);

        this.editorWindow = editorWindow;
        this.projectStorage = new ProjectStorage(name);
        this.scenes = new Scene[] {};
    }

    /**
     * Creates a new scene inside the project
     * @param name Name of the scene
     */
    public void createScene(String name) {
        StoredScene storedScene = new StoredScene(name);
        scenes = ArrayUtils.add(scenes, EditorScene.fromStoredScene(editorWindow, storedScene));
    }

    /**
     * Loads the project from the disc
     */
    public void load() throws IOException {
        byte[] projectStorageData = FileUtils.readFileToByteArray(new File(getProjectFilePath()));
        projectStorage = SerializationUtils.deserialize(projectStorageData);
        File scenesFolder = new File(getPath(), "scenes/");
        if(scenesFolder.exists()) {
            for (File file : Objects.requireNonNull(scenesFolder.listFiles())) {
                byte[] storedSceneData = FileUtils.readFileToByteArray(file);
                StoredScene storedScene = SerializationUtils.deserialize(storedSceneData);
                scenes = ArrayUtils.add(scenes, EditorScene.fromStoredScene(editorWindow, storedScene));
            }
        }

        if(projectStorage.getGradleProject() == null) {
            projectStorage.setGradleProject(GradleManager.setup(new File(getPath(), "src/")));
        }
    }

    /**
     * Saves the project
     * @throws IOException ?
     */
    public void save() throws IOException {
        FileUtils.writeByteArrayToFile(new File(getProjectFilePath()), SerializationUtils.serialize(projectStorage));
        for (Scene scene : scenes) {
            StoredScene storedScene = StoredScene.fromScene(scene);
            FileUtils.writeByteArrayToFile(new File(getPath(), "scenes/" + storedScene.getName() + ".ravbite"), SerializationUtils.serialize(storedScene));
        }
    }

    /**
     * @return Project in simple version
     */
    public SimpleProject asSimple() {
        return new SimpleProject(getName(), getPath());
    }

    /**
     * @return EditorWindow that is used
     */
    public EditorWindow getEditorWindow() {
        return editorWindow;
    }

    /**
     * @return ProjectStorage
     */
    public ProjectStorage getProjectStorage() {
        return projectStorage;
    }

    /**
     * Sets the project storage
     * @param projectStorage New project storage
     */
    public void setProjectStorage(ProjectStorage projectStorage) {
        this.projectStorage = projectStorage;
    }

    /**
     * @return Array of all loaded scenes
     */
    public Scene[] getScenes() {
        return scenes;
    }

}
