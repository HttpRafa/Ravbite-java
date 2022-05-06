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
package de.rafael.ravbite.engine.app.project.generator.templates;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/04/2022 at 1:51 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.project.Project;
import de.rafael.ravbite.engine.app.project.generator.ProjectTemplate;
import de.rafael.ravbite.engine.app.project.gradle.GradleManager;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

public class StandardTemplate extends ProjectTemplate {

    @Override
    public void generate(Project project) {
        super.folder(project, ".");
        super.folder(project, "scenes");
        // Default Scene
        project.createScene("Scene 1");

        super.folder(project, "src");
        // Generate Gradle Project
        GradleManager.setup(new File(project.getPath(), "src/"));

        super.folder(project, "assets");

        super.projectFile(project, project.getName() + ".ravbite", project.getProjectStorage());
    }

}
