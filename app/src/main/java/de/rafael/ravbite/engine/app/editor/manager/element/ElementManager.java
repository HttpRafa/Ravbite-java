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

package de.rafael.ravbite.engine.app.editor.manager.element;

//------------------------------
//
// This class was developed by Rafael K.
// On 5/21/2022 at 8:21 PM
// In the project Ravbite
//
//------------------------------

import org.apache.commons.lang3.ArrayUtils;

public class ElementManager {

    private IGuiElement[] guiElements = new IGuiElement[0];

    /**
     * Adds element to the list
     * @param element Element to add
     */
    public IGuiElement startDrawing(IGuiElement element) {
        guiElements = ArrayUtils.add(guiElements, element);
        return element;
    }

    /**
     * Stops the drawing of an element
     * @param indices index of the elements
     */
    public void stopDrawing(int... indices) {
        guiElements = ArrayUtils.removeAll(guiElements, indices);
    }

    /**
     * Stops the drawing of an element
     * @param element Array of elements
     */
    public void stopDrawing(IGuiElement... element) {
        guiElements = ArrayUtils.removeElements(guiElements, element);
    }

    /**
     * @return List of guiElements to render in the window
     */
    public IGuiElement[] getElements() {
        return guiElements;
    }

}
