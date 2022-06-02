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

package de.rafael.ravbite.engine.app.editor.element.opened;

//------------------------------
//
// This class was developed by Rafael K.
// On 06/02/2022 at 4:12 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.Editor;
import de.rafael.ravbite.engine.app.editor.manager.element.IGuiElement;
import imgui.flag.ImGuiMouseButton;
import imgui.internal.ImGui;

import java.io.File;
import java.util.Objects;

public class AssetExplorer implements IGuiElement {

    private final Editor editor;

    private File currentDirectory;
    private final File assetsDirectory;

    public AssetExplorer(Editor editor) {
        this.editor = editor;

        this.assetsDirectory = new File(editor.getProjectManager().getOpenProject().getProject().getProjectDirectory(), "assets/");
        this.currentDirectory = assetsDirectory;
    }

    @Override
    public boolean render() {

        ImGui.begin("Content Browser");

        if(currentDirectory.compareTo(assetsDirectory) > 0) {
            if (ImGui.button("<-")) {
                currentDirectory = currentDirectory.getParentFile();
            }
        }

        File[] files = this.currentDirectory.listFiles();
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            File file = files[i];

            ImGui.button(file.getName());
            if(ImGui.isItemHovered() && ImGui.isMouseDoubleClicked(ImGuiMouseButton.Left)) {
                if(file.isDirectory()) {
                    currentDirectory = file;
                }
            }
        }

        ImGui.end();

        return false;
    }

}
