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

package de.rafael.ravbite.engine.app.editor.project.files;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/29/2022 at 7:36 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.Editor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;

public class SceneFile implements Serializable {

    @Serial
    private static final long serialVersionUID = 55L;

    private String name;

    public SceneFile(String name) {
        this.name = name;
    }

    /**
     * Reads the bytes from the file and returns the data as an instance of the SceneFile class
     * @param file File to read
     * @return Instance
     */
    public static SceneFile fromFile(File file) {
        try {
            byte[] data = FileUtils.readFileToByteArray(file);
            return SerializationUtils.deserialize(data);
        } catch (IOException exception) {
            Editor.getInstance().handleError(exception);
            return null;
        }
    }

    /**
     * Creates an empty scene
     * @param name Name of the scene
     * @return SceneFile
     */
    public static SceneFile createEmpty(String name) {
        return new SceneFile(name);
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
     * Sets the name of the scene
     * @param name New name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Name of the scene
     */
    public String getName() {
        return name;
    }

    /**
     * @return Name for the file
     */
    public String getNameAsFileName() {
        return name.trim() + ".ravscene";
    }

}
