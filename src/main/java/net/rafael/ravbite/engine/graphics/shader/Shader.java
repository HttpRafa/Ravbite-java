package net.rafael.ravbite.engine.graphics.shader;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/27/2022 at 1:26 PM
// In the project Ravbite
//
//------------------------------

import net.rafael.ravbite.engine.exception.ShaderCompilationException;
import net.rafael.ravbite.engine.graphics.asset.AssetLocation;
import net.rafael.ravbite.engine.graphics.window.EngineWindow;
import org.lwjgl.opengl.GL20;

import java.io.IOException;

public abstract class Shader {

    private final EngineWindow engineWindow;

    private Integer shaderId;

    private Integer programId;
    private Integer vertexShaderId;
    private Integer fragmentShaderId;

    /**
     * @param engineWindow EngineWindow
     */
    public Shader(EngineWindow engineWindow) {
        this.engineWindow = engineWindow;
    }

    public abstract void bindAttributes();

    /**
     * Bind the attribute index to a variable name
     * @param attribute Attribute Index
     * @param variableName Variable Name
     */
    public void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programId, attribute, variableName);
    }

    /**
     * Loads the fragmentShader and registers it in OpenGL
     * @param assetLocation Path to the shader file
     * @throws IOException If the file doesn't exist
     * @throws ShaderCompilationException If the shader is not programmed right
     */
    public void fragmentShader(AssetLocation assetLocation) throws IOException, ShaderCompilationException {
        fragmentShaderId = engineWindow.getGLUtils().rbLoadShader(assetLocation, GL20.GL_FRAGMENT_SHADER);
    }

    /**
     * Loads the vertexShader and registers it in OpenGL
     * @param assetLocation Path to the shader file
     * @throws IOException If the file doesn't exist
     * @throws ShaderCompilationException If the shader is not programmed right
     */
    public void vertexShader(AssetLocation assetLocation) throws IOException, ShaderCompilationException {
        vertexShaderId = engineWindow.getGLUtils().rbLoadShader(assetLocation, GL20.GL_VERTEX_SHADER);
    }

    /**
     * Creates the program in OpenGL
     */
    public void createProgram() {
        programId = GL20.glCreateProgram();
        if(vertexShaderId != null) GL20.glAttachShader(programId, vertexShaderId);
        if(fragmentShaderId != null) GL20.glAttachShader(programId, fragmentShaderId);
        GL20.glLinkProgram(programId);
        GL20.glValidateProgram(programId);
    }

    /**
     * Starts using the shader
     */
    public void start() {
        GL20.glUseProgram(programId);
    }

    /**
     * Stops using the shader
     */
    public void stop() {
        GL20.glUseProgram(0);
    }

    /**
     * Disposes/Deletes everything from OpenGL
     */
    public void dispose() {
        stop();
        GL20.glDetachShader(programId, vertexShaderId);
        GL20.glDetachShader(programId, fragmentShaderId);
        GL20.glDeleteShader(vertexShaderId);
        GL20.glDeleteShader(fragmentShaderId);
        GL20.glDeleteProgram(programId);
    }

    /**
     * @return Window handling this shader
     */
    public EngineWindow getEngineWindow() {
        return engineWindow;
    }

    /**
     * @return ID of this shader
     */
    public Integer getShaderId() {
        return shaderId;
    }

    /**
     * Sets the shaderId
     * @param shaderId New ShaderId
     */
    public void setShaderId(Integer shaderId) {
        this.shaderId = shaderId;
    }

    /**
     * @return ProgramId used by OpenGL
     */
    public int getProgramId() {
        return programId;
    }

    /**
     * @return VertexShaderId used by OpenGL
     */
    public int getVertexShaderId() {
        return vertexShaderId;
    }

    /**
     * @return FragmentShaderId used by OpenGL
     */
    public int getFragmentShaderId() {
        return fragmentShaderId;
    }

}
