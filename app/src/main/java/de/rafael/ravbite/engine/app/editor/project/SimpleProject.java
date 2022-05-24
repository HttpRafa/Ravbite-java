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

package de.rafael.ravbite.engine.app.editor.project;

//------------------------------
//
// This class was developed by Rafael K.
// On 5/21/2022 at 8:29 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.Editor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

public class SimpleProject implements Serializable {

    @Serial
    private static final long serialVersionUID = 55L;

    private String name;

    private File projectDirectory;
    private File projectFile;

    public SimpleProject(String name, File projectDirectory, File projectFile) {
        this.name = name;
        this.projectDirectory = projectDirectory;
        this.projectFile = projectFile;
    }

    /**
     * Sets the name of the project
     * @param name New name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the name of the project
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the directory of the project
     * @param projectDirectory New directory
     */
    public void updateDirectory(File projectDirectory) {
        try {
            FileUtils.copyDirectory(this.projectDirectory, projectDirectory);
        } catch (IOException exception) {
            Editor.getInstance().handleError(exception);
        }

        this.projectDirectory = projectDirectory;
        this.projectFile = new File(projectDirectory, projectFile.getName());
    }

    /**
     * @return Directory of the project
     */
    public File getProjectDirectory() {
        return projectDirectory;
    }

    /**
     * @return Project file of the project
     */
    public File getProjectFile() {
        return projectFile;
    }

}
