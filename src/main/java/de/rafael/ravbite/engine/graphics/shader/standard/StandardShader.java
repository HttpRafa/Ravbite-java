package de.rafael.ravbite.engine.graphics.shader.standard;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/27/2022 at 1:26 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.exception.ShaderCompilationException;
import de.rafael.ravbite.engine.graphics.asset.AssetLocation;
import de.rafael.ravbite.engine.graphics.shader.AbstractShader;
import de.rafael.ravbite.engine.graphics.window.EngineWindow;

import java.io.IOException;

public class StandardShader extends AbstractShader {

    public StandardShader(EngineWindow engineWindow) {
        super(engineWindow);
        try {
            vertexShader(AssetLocation.create("/shaders/standard/vertexShader.glsl", AssetLocation.INTERNAL));
            fragmentShader(AssetLocation.create("/shaders/standard/fragmentShader.glsl", AssetLocation.INTERNAL));
            createProgram();
        } catch (IOException | ShaderCompilationException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

}
