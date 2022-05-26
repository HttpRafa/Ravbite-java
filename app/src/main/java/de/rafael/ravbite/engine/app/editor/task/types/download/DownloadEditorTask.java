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
// On 05/26/2022 at 6:49 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.app.editor.Editor;
import de.rafael.ravbite.engine.app.editor.task.EditorTask;
import de.rafael.ravbite.utils.method.MethodCallback;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadEditorTask extends EditorTask {

    private URL url;

    private final MethodCallback<WatchedInputStream> callback;

    private WatchedInputStream watchedInputStream;

    public DownloadEditorTask(String urlString, MethodCallback<WatchedInputStream> callback) {
        super("Downloading file from " + urlString + "...");
        this.callback = callback;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException exception) {
            Editor.getInstance().handleError(exception);
        }
    }

    @Override
    public void execute() {
        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            long contentLength = urlConnection.getHeaderFieldLong("Content-Length", inputStream.available());

            watchedInputStream = new WatchedInputStream(inputStream, contentLength);
                callback.provide(watchedInputStream);
        } catch (Exception exception) {
            Editor.getInstance().handleError(exception);
        }
    }

    @Override
    public float done() {
        return watchedInputStream == null ? 0 : watchedInputStream.getCurrent();
    }

    @Override
    public float toDo() {
        return watchedInputStream == null ? 0 : watchedInputStream.getLength();
    }

    @Override
    public float percentage() {
        return done() / toDo();
    }

    public URL getUrl() {
        return url;
    }

    public WatchedInputStream getWatchedInputStream() {
        return watchedInputStream;
    }

}
