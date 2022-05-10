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
package de.rafael.ravbite.engine.app.editor.window;

//------------------------------
//
// This class was developed by Rafael K.
// On 04/15/2022 at 6:36 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.utils.gui.IGui;
import imgui.ImGui;

public class MenuBar implements IGui {

    @Override
    public void gui() {
        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu("File")) {
            if (ImGui.menuItem("New Scene", "Ctrl+N")) {

            }
            if (ImGui.menuItem("Open Scene", "Ctrl+O")) {

            }
            ImGui.separator();
            if(ImGui.menuItem("Save", "Ctrl+S")) {

            }
            if(ImGui.menuItem("Save As", "Ctrl+Shift+S")) {

            }
            ImGui.separator();
            if(ImGui.menuItem("New Project")) {

            }
            if(ImGui.menuItem("Open Project")) {

            }
            ImGui.endMenu();
        }
        if (ImGui.beginMenu("Edit")) {
            if (ImGui.menuItem("Undo", "Ctrl+Z")) {

            }
            if (ImGui.menuItem("Redo", "Ctrl+Y")) {

            }
            ImGui.separator();
            if(ImGui.menuItem("Cut", "Ctrl+X")) {

            }
            if(ImGui.menuItem("Copy", "Ctrl+C")) {

            }
            if(ImGui.menuItem("Paste", "Ctrl+V")) {

            }
            ImGui.endMenu();
        }
        if (ImGui.beginMenu("Project")) {
            if (ImGui.menuItem("Project Settings")) {

            }
            ImGui.endMenu();
        }
        if (ImGui.beginMenu("Run")) {
            if(ImGui.menuItem("Play", "Ctrl+P")) {

            }
            if(ImGui.menuItem("Pause", "Ctrl+Shift+P")) {

            }
            if(ImGui.menuItem("Stop", "Ctrl+Alt+P")) {

            }
            ImGui.endMenu();
        }
        if (ImGui.beginMenu("Build")) {
            if (ImGui.menuItem("Build Settings")) {

            }
            ImGui.separator();
            if (ImGui.menuItem("Build", "Ctrl+B")) {

            }
            if (ImGui.menuItem("Build And Run", "Ctrl+Shift+B")) {

            }
            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();

    }

}
