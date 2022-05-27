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

package de.rafael.ravbite.engine.app.editor.task.types.zip;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/27/2022 at 2:13 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.Editor;
import de.rafael.ravbite.engine.app.editor.task.EditorTask;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipEditorTask extends EditorTask {

    private long elementAmount = 0;
    private long currentElement = 0;

    private final int operation;

    private final File input;
    private final File output;

    public ZipEditorTask(String description, int operation, File input, File output) {
        super(description);

        this.operation = operation;

        this.input = input;
        this.output = output;
    }

    @Override
    public void execute() {
        if(operation == 0) {
            try (ZipFile zipFile = new ZipFile(input)) {
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                elementAmount = zipFile.size();
                int i = 0;
                while (entries.hasMoreElements()) {
                    currentElement = i;
                    ZipEntry entry = entries.nextElement();
                    File entryDestination = new File(output,  entry.getName());
                    if (entry.isDirectory()) {
                        entryDestination.mkdirs();
                    } else {
                        entryDestination.getParentFile().mkdirs();
                        try (InputStream inputStream = zipFile.getInputStream(entry);
                             OutputStream fileOutputStream = new FileOutputStream(entryDestination)) {
                            IOUtils.copy(inputStream, fileOutputStream);
                        }
                    }
                    i++;
                }
            } catch (IOException exception) {
                Editor.getInstance().handleError(exception);
            }
        }
    }

    @Override
    public float done() {
        return currentElement;
    }

    @Override
    public float toDo() {
        return elementAmount;
    }

    @Override
    public float percentage() {
        return done() / toDo();
    }

}
