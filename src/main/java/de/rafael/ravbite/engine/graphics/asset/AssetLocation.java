package de.rafael.ravbite.engine.graphics.asset;

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