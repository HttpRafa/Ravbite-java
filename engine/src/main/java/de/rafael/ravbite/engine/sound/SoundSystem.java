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

package de.rafael.ravbite.engine.sound;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/08/2022 at 12:18 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.window.EngineView;
import org.lwjgl.openal.AL10;

public class SoundSystem {

    private final EngineView engineView;

    private long device;
    private long context;

    public SoundSystem(EngineView engineView) {
        this.engineView = engineView;
    }

    public SoundSystem distanceModel(int model) {
        AL10.alDistanceModel(model);
        return this;
    }

    /**
     * Initializes the soundEngine
     * @return SoundSystem
     */
    public SoundSystem initialize() {
        String defaultDevice = engineView.getUtils().alGetDefaultAudioDevice();
        device = engineView.getUtils().alOpenDevice(defaultDevice);

        context = engineView.getUtils().alInit(device);
        return this;
    }

    /**
     * Destroys the soundEngine
     */
    public void destroy() {
        engineView.getUtils().alDestroy(context, device);
    }

    /**
     * @return Device as OpenAL ID
     */
    public long getDevice() {
        return device;
    }

    /**
     * @return OpenAL context
     */
    public long getContext() {
        return context;
    }

    /**
     * @return EngineView
     */
    public EngineView getEngineView() {
        return engineView;
    }

}
