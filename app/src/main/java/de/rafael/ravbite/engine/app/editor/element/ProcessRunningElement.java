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

package de.rafael.ravbite.engine.app.editor.element;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/24/2022 at 2:41 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.manager.element.IGuiElement;
import imgui.ImGui;

public class ProcessRunningElement implements IGuiElement {

    private final String name;

    private final long startTime;

    public ProcessRunningElement(String name) {
        this.name = name;

        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean render() {

        ImGui.setNextWindowSize(500, 100);
        ImGui.begin("Busy | " + name);
        ImGui.text("An action is being carried out that may take little or a lot of time.");
        ImGui.text("Get a glass of water.");

        ImGui.spacing();
        ImGui.separator();
        ImGui.spacing();

        ImGui.text("Busy for " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds.");
        ImGui.end();

        return false;
    }

}
