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

public class CheckBoxDecider {

    private boolean onlyOne = false;

    private final String[] options;
    private final ImBoolean[] optionsValues;

    public CheckBoxDecider(String[] options, boolean[] defaultValues) {
        this.options = options;
        this.optionsValues = new ImBoolean[this.options.length];
        for (int i = 0; i < this.optionsValues.length; i++) {
            this.optionsValues[i] = new ImBoolean(defaultValues[i]);
        }
    }

    public CheckBoxDecider(boolean onlyOne, String[] options, boolean[] defaultValues) {
        this.options = options;
        this.optionsValues = new ImBoolean[this.options.length];
        for (int i = 0; i < this.optionsValues.length; i++) {
            this.optionsValues[i] = new ImBoolean();
            this.optionsValues[i].set(defaultValues[i]);
        }

        this.onlyOne = onlyOne;
    }

    public void imGui() {
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

    public String[] getOptions() {
        return options;
    }

    public ImBoolean[] getOptionsValues() {
        return optionsValues;
    }

}
