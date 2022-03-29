package de.rafael.ravbite.engine.exception;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/27/2022 at 1:55 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.graphics.asset.AssetLocation;

public class ShaderCompilationException extends Exception {

    @java.io.Serial
    private static final long serialVersionUID = -997853943823790472L;

    public ShaderCompilationException(String glError, AssetLocation assetLocation) {
        super("Failed to compile shader[" + assetLocation.toString() + "]: " + glError);
    }

}
