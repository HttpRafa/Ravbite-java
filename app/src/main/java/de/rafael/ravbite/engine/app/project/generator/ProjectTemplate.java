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
package de.rafael.ravbite.engine.app.project.generator;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/04/2022 at 1:51 PM
// In the project Ravbite
//
//------------------------------

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import de.rafael.ravbite.engine.app.project.Project;
import de.rafael.ravbite.engine.app.project.SimpleProject;
import de.rafael.ravbite.engine.app.project.storage.ProjectStorage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public abstract class ProjectTemplate {

    public abstract void generate(Project project);

    /**
     * Downloads a file and stores it
     * @param simpleProject Project
     * @param path Path to the file inside the project structure
     * @param url Url
     */
    public void download(SimpleProject simpleProject, String path, String url) {
        File file = new File(simpleProject.getPath(), path);
        try {
            InputStream inputStream = new URL(url).openStream();
            Files.copy(inputStream, Paths.get(file.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Creates a projectFile
     * @param simpleProject Project
     * @param path Path to the file inside the project structure
     * @param projectStorage Instance to save
     */
    public void projectFile(SimpleProject simpleProject, String path, ProjectStorage projectStorage) {
        File file = new File(simpleProject.getPath(), path);
        try {
            FileUtils.writeByteArrayToFile(file, SerializationUtils.serialize(projectStorage));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Creates a folder
     * @param simpleProject Project
     * @param path Path to the folder inside the project structure
     */
    public void folder(SimpleProject simpleProject, String path) {
        File folder = new File(simpleProject.getPath(), path);
        if(!folder.exists()) folder.mkdirs();
    }

    /**
     * Creates a jsonFile
     * @param simpleProject Project
     * @param path Path to the file inside the project structure
     * @param jsonElement JsonElement
     */
    public void jsonFile(SimpleProject simpleProject, String path, JsonElement jsonElement) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();

        try {
            File file = new File(simpleProject.getPath(), path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(jsonElement));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
