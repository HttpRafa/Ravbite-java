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

package de.rafael.ravbite.engine.sound.utils;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/07/2022 at 3:03 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.utils.DataWatcher;
import de.rafael.ravbite.engine.graphics.utils.ImageUtils;
import de.rafael.ravbite.engine.graphics.view.EngineView;
import de.rafael.ravbite.utils.asset.AssetLocation;
import de.rafael.ravbite.utils.io.IOUtils;
import org.lwjgl.openal.*;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class SoundUtils extends ImageUtils {

    public SoundUtils(EngineView engineView) {
        super(engineView);
    }

    /**
     * @return Returns the default sound device
     */
    public String alGetDefaultAudioDevice() {
        return ALC11.alcGetString(0, ALC11.ALC_DEFAULT_DEVICE_SPECIFIER);
    }

    /**
     * Opens a device
     * @param deviceName Device name
     * @return Id of the opened device
     */
    public long alOpenDevice(String deviceName) {
        return ALC11.alcOpenDevice(deviceName);
    }

    /**
     * Initializes OpenAL
     * @param device Device to use
     * @return ContextId
     */
    public long alInit(long device) {
        return alInit(device, new int[] {0});
    }

    /**
     * Initializes OpenAL
     * @param device Device to use
     * @param attributes Attributes
     * @return ContextId
     */
    public long alInit(long device, int[] attributes) {
        long context = ALC11.alcCreateContext(device, attributes);
        ALC11.alcMakeContextCurrent(context);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
        return context;
    }

    /**
     * Destroys OpenAL
     * @param context Context to destroy
     * @param device Device to close
     */
    public void alDestroy(long context, long device) {
        ALC11.alcDestroyContext(context);
        ALC11.alcCloseDevice(device);
    }

    /**
     * Loads a sound from a file into memory
     * @param location Location of the file
     * @return Loaded sound
     */
    public Sound alLoadSound(AssetLocation location) {
        int buffer = AL10.alGenBuffers();
        try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
            ShortBuffer pcm = readVorbis(location.getPath(true), 64 * 1024, info);

            // Copy to buffer
            AL10.alBufferData(buffer, info.channels() == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16, pcm, info.sample_rate());
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        getEngineView().getDataWatcher().alBuffer(DataWatcher.DataScope.SCENE, buffer);
        return new Sound(buffer, location);
    }

    /**
     * Reads the file
     * @param resource Path to the file
     * @param bufferSize Size of the buffer
     * @param info Info
     * @return Raw audio data
     * @throws Exception ?
     */
    private ShortBuffer readVorbis(String resource, int bufferSize, STBVorbisInfo info) throws Exception {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer vorbis = IOUtils.ioResourceToByteBuffer(resource, bufferSize);
            IntBuffer error = stack.mallocInt(1);
            long decoder = stb_vorbis_open_memory(vorbis, error, null);
            if (decoder == NULL) {
                throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
            }

            stb_vorbis_get_info(decoder, info);

            int channels = info.channels();

            int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

            ShortBuffer pcm = MemoryUtil.memAllocShort(lengthSamples);

            pcm.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
            stb_vorbis_close(decoder);

            return pcm;
        }
    }

}
