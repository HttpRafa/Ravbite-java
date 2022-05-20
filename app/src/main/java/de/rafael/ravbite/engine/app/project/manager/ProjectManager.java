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
package de.rafael.ravbite.engine.app.project.manager;

//------------------------------
//
// This class was developed by Rafael K.
// On 04/16/2022 at 7:36 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.project.Project;
import de.rafael.ravbite.engine.app.project.SimpleProject;
import de.rafael.ravbite.engine.app.project.generator.ProjectGenerator;
import de.rafael.ravbite.engine.app.project.generator.ProjectTemplates;
import de.rafael.ravbite.engine.app.project.gradle.GradleDslType;
import de.rafael.ravbite.engine.app.project.gradle.GradleLanguageType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ProjectManager {

    public static final File projectsFile = new File("projects.ravbite");

    private SimpleProject[] projects = new SimpleProject[0];

    /**
     * Loads a projects
     * @throws IOException ?
     */
    public void loadProjects() throws IOException {
        if(projectsFile.exists()) {
            byte[] data = FileUtils.readFileToByteArray(projectsFile);
            projects = SerializationUtils.deserialize(data);
        }
    }

    /**
     * Stores all projects
     * @throws IOException ?
     */
    public void storeProjects() throws IOException {
        byte[] data = SerializationUtils.serialize(projects);
        FileUtils.writeByteArrayToFile(projectsFile, data);
    }

    /**
     * Creates a project
     * @param name Name of the project
     * @param path Path to the project
     * @param dimension Dimension
     * @param packageName Package Name
     * @param dslType Gradle DSL Type
     * @param languageType Gradle Language Type
     * @return If it was successful
     */
    public boolean createProject(String name, String path, int dimension, String packageName, GradleDslType dslType, GradleLanguageType languageType) {
        File projectFolder = new File(path, name);

        Project project = new Project(name, projectFolder.getAbsolutePath());
        ProjectGenerator.generate(ProjectTemplates.STANDARD, project);

        projects = ArrayUtils.add(projects, project.asSimple());

        return true;
    }

    /**
     * Opens the specified project
     * @param simpleProject Simple version of the project
     */
    public Project openProject(SimpleProject simpleProject) {
        Project project = new Project(simpleProject.getName(), simpleProject.getPath());
        project.load();
        return project;
    }

    /**
     * Saves the project
     */
    public void saveProject(Project project) {
        try {
            project.save();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Deletes a project
     * @param name Name of the project
     */
    public void deleteProject(String name) {
        for (int i = 0; i < projects.length; i++) {
            if(name.equalsIgnoreCase(projects[i].getName())) {
                deleteProject(i);
            }
        }
    }

    /**
     * Deletes a project
     * @param index Index of the project in the projects array
     */
    public void deleteProject(int index) {
        SimpleProject project = projects[index];
        projects = ArrayUtils.remove(projects, index);
    }

    /**
     * @param name Name
     * @return If the name is free
     */
    public boolean isNameFree(String name) {
        return Arrays.stream(projects).noneMatch(item -> item.getName().equalsIgnoreCase(name));
    }

    /**
     * @return All projects as simple version
     */
    public SimpleProject[] getProjects() {
        return projects;
    }

}
