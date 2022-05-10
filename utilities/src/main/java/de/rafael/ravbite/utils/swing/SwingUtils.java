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
package de.rafael.ravbite.utils.swing;

//------------------------------
//
// This class was developed by Rafael K.
// On 04/09/2022 at 11:58 PM
// In the project Ravbite
//
//------------------------------

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.IntelliJTheme;

import javax.swing.*;

public class SwingUtils {

    public static void initSwing(SwingThemes theme) {

        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException exception) {
            exception.printStackTrace();
        }
        IntelliJTheme.setup(SwingUtils.class.getResourceAsStream("/swing/themes/" + theme.fileName));

    }

    public static enum SwingThemes {

        ATOM_ONE_DARK("Atom One Dark", "Atom One Dark.theme.json"),
        LIGHT_FLAT("Light Flat", "LightFlatTheme.theme.json"),
        MATERIAL_DEEP_OCEAN("Material Deep Ocean", "Material Deep Ocean.theme.json"),
        NIGHT_OWL("Night Owl", "Night Owl.theme.json");

        private final String name;
        private final String fileName;

        SwingThemes(String name, String fileName) {
            this.name = name;
            this.fileName = fileName;
        }

        /**
         * @return Name of the theme
         */
        public String getName() {
            return name;
        }

        /**
         * @return Filename of the theme
         */
        public String getFileName() {
            return fileName;
        }

    }

}
