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

package de.rafael.ravbite.engine.app.ui;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/06/2022 at 4:09 PM
// In the project Ravbite
//
//------------------------------

import imgui.ImGui;
import imgui.type.ImBoolean;

public class CheckBoxList {

    private boolean onlyOne = false;

    private final String[] options;
    private final ImBoolean[] optionsValues;

    public CheckBoxList(String[] options, boolean[] defaultValues) {
        this.options = options;
        this.optionsValues = new ImBoolean[this.options.length];
        for (int i = 0; i < this.optionsValues.length; i++) {
            this.optionsValues[i] = new ImBoolean(defaultValues[i]);
        }
    }

    public CheckBoxList(boolean onlyOne, String[] options, boolean[] defaultValues) {
        this.options = options;
        this.optionsValues = new ImBoolean[this.options.length];
        for (int i = 0; i < this.optionsValues.length; i++) {
            this.optionsValues[i] = new ImBoolean();
            this.optionsValues[i].set(defaultValues[i]);
        }

        this.onlyOne = onlyOne;
    }

    /**
     * Called every frame to render the GUI
     */
    public void render() {
        for (int i = 0; i < this.options.length; i++) {
            if(ImGui.checkbox(this.options[i], this.optionsValues[i])) {
                if(onlyOne && this.optionsValues[i].get()) {
                    for (int v = 0; v < this.optionsValues.length; v++) {
                        if(v != i) {
                            this.optionsValues[v].set(false);
                        }
                    }
                } else if(onlyOne && !this.optionsValues[i].get()) {
                    for (int v = 0; v < this.optionsValues.length; v++) {
                        if(v != i) {
                            this.optionsValues[v].set(true);
                            break;
                        }
                    }
                }
            }
            if(i != (this.options.length - 1)) {
                ImGui.sameLine();
            }
        }
    }

    /**
     * @return The index of the value that is true !! Only works with onlyOne !!
     */
    public int trueIndex() {
        for (int i = 0; i < optionsValues.length; i++) {
            if(optionsValues[i].get()) {
                return i;
            }
        }
        return 0;
    }

    /**
     * @return Array of options
     */
    public String[] getOptions() {
        return options;
    }

    /**
     * @return Array of the values of options
     */
    public ImBoolean[] getOptionsValues() {
        return optionsValues;
    }

}
