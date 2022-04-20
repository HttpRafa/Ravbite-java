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
package de.rafael.ravbite.engine.app.project;

//------------------------------
//
// This class was developed by Rafael K.
// On 04/16/2022 at 7:36 PM
// In the project Ravbite
//
//------------------------------

import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectManager {

    public static final File projectsFile = new File("projects.json");

    private final List<EditorProjectDescription> projects = new ArrayList<>();

    public void loadProjects() throws IOException {
        Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();

        if(!projectsFile.exists()) {
            projectsFile.createNewFile();
            try(FileWriter fileWriter = new FileWriter(projectsFile)) {
                fileWriter.write("{}");
                fileWriter.flush();
            }
        }

        projects.clear();
        JsonObject jsonObject = new JsonParser().parse(new InputStreamReader(new FileInputStream(projectsFile))).getAsJsonObject();
        if(jsonObject.has("projects")) {
            JsonArray projectsArray = jsonObject.get("projects").getAsJsonArray();
            for (JsonElement jsonElement : projectsArray) {
                EditorProjectDescription editorDescription = gson.fromJson(jsonElement, EditorProjectDescription.class);
                projects.add(editorDescription);
            }
        }

    }

    public void storeProjects() throws IOException {
        Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();

        if(!projectsFile.exists()) {
            projectsFile.createNewFile();
            try (FileWriter fileWriter = new FileWriter(projectsFile)) {
                fileWriter.write("{}");
                fileWriter.flush();
            }
        }

        JsonObject jsonObject = new JsonObject();
        JsonArray projectsArray = new JsonArray();

        for (EditorProjectDescription project : projects) {
            projectsArray.add(new JsonParser().parse(gson.toJson(project)));
        }

        jsonObject.add("projects", projectsArray);

        try(FileWriter fileWriter = new FileWriter(projectsFile)) {
            fileWriter.write(gson.toJson(jsonObject));
            fileWriter.flush();
        }

    }

    public boolean createProject(String name, String path, int dimension) {
        projects.add(new EditorProjectDescription(name, path));

        return true;
    }

    public void deleteProject(String name) {

    }

    public boolean isNameFree(String name) {
        return projects.stream().noneMatch(item -> item.getName().equalsIgnoreCase(name));
    }

    public List<EditorProjectDescription> getProjects() {
        return projects;
    }

}
