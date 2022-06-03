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

package de.rafael.ravbite.engine.app.editor.manager.theme.standard;

//------------------------------
//
// This class was developed by Rafael K.
// On 06/03/2022 at 8:08 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.element.opened.bottom.AssetsBrowserElement;
import de.rafael.ravbite.engine.app.editor.element.opened.top.ToolBarElement;
import de.rafael.ravbite.engine.app.editor.manager.element.IGuiElement;
import de.rafael.ravbite.engine.app.editor.manager.theme.Theme;
import imgui.flag.ImGuiCol;
import imgui.internal.ImGui;

public class StandardTheme implements Theme {

    @Override
    public void styleButton(Class<? extends IGuiElement> element) {
        if(element == AssetsBrowserElement.class) {
            ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 0, 0);

            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.305f, 0.31f, 0.5f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.15f, 0.1505f, 00.151f, 0.5f);
        } else if(element == ToolBarElement.class) {
            ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 0, 0);

            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.305f, 0.31f, 0.5f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.15f, 0.1505f, 00.151f, 0.5f);
        }
    }

    @Override
    public void cleanButton(Class<? extends IGuiElement> element) {
        if(element == AssetsBrowserElement.class) {
            ImGui.popStyleColor();

            ImGui.popStyleColor(2);
        } else if(element == ToolBarElement.class) {
            ImGui.popStyleColor();

            ImGui.popStyleColor(2);
        }
    }

}
