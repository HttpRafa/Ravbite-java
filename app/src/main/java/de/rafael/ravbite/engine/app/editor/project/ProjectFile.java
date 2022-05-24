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

package de.rafael.ravbite.engine.app.editor.project;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/24/2022 at 2:05 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.Editor;
import de.rafael.ravbite.engine.app.editor.project.settings.EditorSettings;
import de.rafael.ravbite.engine.app.editor.project.settings.EngineSettings;
import de.rafael.ravbite.engine.app.editor.project.settings.GradleSettings;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

public class ProjectFile extends SimpleProject implements Serializable {

    @Serial
    private static final long serialVersionUID = 55L;

    private final EditorSettings editorSettings;

    private final EngineSettings engineSettings;
    private final GradleSettings gradleSettings;

    public ProjectFile(SimpleProject simpleProject, EditorSettings editorSettings, EngineSettings engineSettings, GradleSettings gradleSettings) {
        super(simpleProject.getName(), simpleProject.getProjectDirectory(), simpleProject.getProjectFile());

        this.editorSettings = editorSettings;
        this.engineSettings = engineSettings;
        this.gradleSettings = gradleSettings;
    }

    /**
     * Writes the data to a file
     * @param file File to write in
     */
    public void writeToFile(File file) {
        byte[] data = SerializationUtils.serialize(this);
        try {
            FileUtils.writeByteArrayToFile(file, data);
        } catch (IOException exception) {
            Editor.getInstance().handleError(exception);
        }
    }

    /**
     * @return Settings for the editor
     */
    public EditorSettings getEditorSettings() {
        return editorSettings;
    }

    /**
     * @return Settings for the engine
     */
    public EngineSettings getEngineSettings() {
        return engineSettings;
    }

    /**
     * @return Settings for the gradle project setup
     */
    public GradleSettings getGradleSettings() {
        return gradleSettings;
    }

}
