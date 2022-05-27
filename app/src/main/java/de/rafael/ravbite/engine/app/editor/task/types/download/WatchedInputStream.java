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

package de.rafael.ravbite.engine.app.editor.task.types.download;

//------------------------------
//
// This class was developed by Rafael K.
// On 05/26/2022 at 1:42 PM
// In the project Ravbite
//
//------------------------------

import java.io.IOException;
import java.io.InputStream;

public class WatchedInputStream extends InputStream {

    private final InputStream wrapped;

    private final long length;
    private long current = 0;

    /*

     */

    public WatchedInputStream(InputStream wrapped, long length) {
        this.wrapped = wrapped;
        this.length = length;
    }

    @Override
    public int read() throws IOException {
        int read = this.wrapped.read();
        current += 1;
        return read;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int read = this.wrapped.read(b);
        current += read;
        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = this.wrapped.read(b, off, len);
        current += read;
        return read;
    }

    @Override
    public long skip(long n) throws IOException {
        long length = this.wrapped.skip(n);
        current += length;
        return length;
    }

    @Override
    public void close() throws IOException {
        this.wrapped.close();
    }

    public InputStream getWrapped() {
        return this.wrapped;
    }

    public long getCurrent() {
        return current;
    }

    public long getLength() {
        return length;
    }


}
