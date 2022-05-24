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

package de.rafael.ravbite.engine.app.editor.element;

//------------------------------
//
// This class was developed by Rafael K.
// On 5/21/2022 at 8:35 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.Editor;
import de.rafael.ravbite.engine.app.editor.manager.element.IGuiElement;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import org.apache.commons.lang3.ArrayUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

public record ErrorElement(Editor editor) implements IGuiElement {

    @Override
    public boolean render() {
        if(ImGui.beginPopupModal("Reported errors", ImGuiWindowFlags.AlwaysAutoResize)) {
            Throwable[] reportedErrors = editor.getReportedErrors();
            ImGui.text("The editor found " + reportedErrors.length + " errors! / " + (reportedErrors.length == 0 ? "Everything is OK!" : "Report them or ignore them"));
            int[] solvedErrors = new int[0];
            for (int i = 0; i < reportedErrors.length; i++) {
                Throwable error = reportedErrors[i];
                if(ImGui.collapsingHeader("Error " + (i + 1) + "[" + i + "] (" + error.getClass().getName() + ")")) {
                    StringWriter stringWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(stringWriter);
                    error.printStackTrace(printWriter);
                    for (String line : stringWriter.toString().split("\n")) {
                        ImGui.text(line);
                    }

                    ImGui.spacing();

                    if (ImGui.button("Ignore")) {
                        solvedErrors = ArrayUtils.add(solvedErrors, i);
                    }

                    ImGui.sameLine();

                    if(ImGui.button("Send errorLog")) {
                        solvedErrors = ArrayUtils.add(solvedErrors, i);

                        // TODO: Send errorLog to server
                    }
                }
            }
            for (int solvedError : solvedErrors) {
                editor.solveError(solvedError);
            }

            ImGui.spacing();
            ImGui.separator();
            ImGui.spacing();

            if(ImGui.button("Close")) {
                ImGui.closeCurrentPopup();
            }

            ImGui.sameLine();

            if(ImGui.button("Ignore all")) {
                editor.solveAllErrors();
            }

            ImGui.endPopup();
        }
        return false;
    }

}
