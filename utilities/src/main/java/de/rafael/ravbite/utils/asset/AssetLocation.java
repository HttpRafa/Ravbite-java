/*
 * Copyright (c) 2022.
 * All rights reserved.
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
package de.rafael.ravbite.utils.asset;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/27/2022 at 1:40 PM
// In the project Ravbite
//
//------------------------------

import java.io.*;

public class AssetLocation {

    public static final int INTERNAL = 0;
    public static final int EXTERNAL = 1;

    /**
     * Creates an asset path
     * @param path Path to the asset
     * @param location Location were the asset is stored(INTERNAL or EXTERNAL)
     * @return AssetLocation object
     */
    public static AssetLocation create(String path, int location) {
        return new AssetLocation(path, location);
    }

    private final String path;
    private final int location;

    public AssetLocation(String path, int location) {
        this.path = path;
        this.location = location;
    }

    /**
     * Loads the data from the file as a string
     * @return Data as string
     * @throws IOException If the file doesn't exist or the bufferReader fails
     */
    public String loadString() throws IOException {
        InputStream inputStream = asInputStream();
        assert inputStream != null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append(System.lineSeparator());
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    /**
     * @return Creates a inputSteam from the given file location
     * @throws FileNotFoundException If the file doesn't exist
     */
    public InputStream asInputStream() throws FileNotFoundException {
        InputStream inputStream;
        if(location == INTERNAL) {
            inputStream = this.getClass().getResourceAsStream(path);
        } else {
            inputStream = new FileInputStream(path);
        }
        return inputStream;
    }

    /**
     * @return Path to the asset
     */
    public String getPath() {
        return path;
    }

    /**
     * @return Location were the asset is stored(INTERNAL or EXTERNAL)
     */
    public int getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return (location == INTERNAL ? "INTERNAL" : "EXTERNAL") + "#" + path;
    }

}
