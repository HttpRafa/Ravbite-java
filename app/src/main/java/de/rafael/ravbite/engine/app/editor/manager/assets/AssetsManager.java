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

package de.rafael.ravbite.engine.app.editor.manager.assets;

//------------------------------
//
// This class was developed by Rafael K.
// On 06/03/2022 at 5:33 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.Editor;
import de.rafael.ravbite.engine.utils.RavbiteUtils;
import de.rafael.ravbite.utils.asset.AssetLocation;

import java.io.File;

public class AssetsManager {

    private int TEXTURE_ICON_DIRECTORY;
    private int TEXTURE_ICON_FILE;

    private int TEXTURE_ICON_NEXT;
    private int TEXTURE_ICON_PREVIOUS;
    private int TEXTURE_ICON_PLAY;
    private int TEXTURE_ICON_STOP;

    public AssetsManager(Editor editor) {
        RavbiteUtils utils = editor.getEditorWindow().getUtils();

        try {
            this.TEXTURE_ICON_DIRECTORY = utils.rbLoadViewTexture(AssetLocation.create("/assets/icons/directory.png", AssetLocation.INTERNAL));
            this.TEXTURE_ICON_FILE = utils.rbLoadViewTexture(AssetLocation.create("/assets/icons/file.png", AssetLocation.INTERNAL));

            this.TEXTURE_ICON_NEXT = utils.rbLoadViewTexture(AssetLocation.create("/assets/icons/next.png", AssetLocation.INTERNAL));
            this.TEXTURE_ICON_PREVIOUS = utils.rbLoadViewTexture(AssetLocation.create("/assets/icons/previous.png", AssetLocation.INTERNAL));

            this.TEXTURE_ICON_PLAY = this.TEXTURE_ICON_NEXT;
            this.TEXTURE_ICON_STOP = utils.rbLoadViewTexture(AssetLocation.create("/assets/icons/stop.png", AssetLocation.INTERNAL));
        } catch(Exception exception) {
            editor.handleError(exception);
        }
    }

    public int getTextureForFile(File file) {
        return file.isDirectory() ? TEXTURE_ICON_DIRECTORY : TEXTURE_ICON_FILE;
    }

    public int TEXTURE_ICON_NEXT() {
        return TEXTURE_ICON_NEXT;
    }

    public int TEXTURE_ICON_PREVIOUS() {
        return TEXTURE_ICON_PREVIOUS;
    }

    public int TEXTURE_ICON_PLAY() {
        return TEXTURE_ICON_PLAY;
    }

    public int TEXTURE_ICON_STOP() {
        return TEXTURE_ICON_STOP;
    }

}
