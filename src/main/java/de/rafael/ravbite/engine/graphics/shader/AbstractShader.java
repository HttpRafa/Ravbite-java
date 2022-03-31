package de.rafael.ravbite.engine.graphics.shader;

//------------------------------
//
// This class was developed by Rafael K.
// On 3/27/2022 at 1:26 PM
// In the project Ravbite
//
//------------------------------

import de.rafael.ravbite.engine.exception.ShaderCompilationException;
import de.rafael.ravbite.engine.graphics.asset.AssetLocation;
import de.rafael.ravbite.engine.graphics.components.RenderComponent;
import de.rafael.ravbite.engine.graphics.components.camera.CameraComponent;
import de.rafael.ravbite.engine.graphics.object.game.GameObject;
import de.rafael.ravbite.engine.graphics.window.EngineWindow;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.nio.FloatBuffer;

public abstract class AbstractShader {

    private final EngineWindow engineWindow;

    private Integer shaderId;

    private Integer programId;
    private Integer vertexShaderId;
    private Integer fragmentShaderId;

    /**
     * @param engineWindow EngineWindow
     */
    public AbstractShader(EngineWindow engineWindow) {
        this.engineWindow = engineWindow;
    }

    public abstract void prepareObject(GameObject gameObject, CameraComponent cameraComponent, RenderComponent renderer);

    public abstract void bindAttributes();
    public abstract void updateUniformLocations();

    /**
     * Bind the attribute index to a variable name
     * @param attribute Attribute Index
     * @param variableName Variable Name
     */
    public void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programId, attribute, variableName);
    }

    /**
     * @param uniformName Name of the uniform variable
     * @return ID of the uniform variable
     */
    public int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programId, uniformName);
    }

    /**
     * Loads a float onto a uniform variable
     * @param location Location of the uniform variable
     * @param value Value to load into the uniform variable
     */
    public void load(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    /**
     * Loads a boolean onto a uniform variable
     * @param location Location of the uniform variable
     * @param value Value to load into the uniform variable
     */
    public void load(int location, boolean value) {
        GL20.glUniform1f(location, value ? 1 : 0);
    }

    /**
     * Loads a vector with 3 values onto a uniform variable
     * @param location Location of the uniform variable
     * @param value Value to load into the uniform variable
     */
    public void load(int location, Vector3f value) {
        GL20.glUniform3f(location, value.x, value.y, value.z);
    }

    /**
     * Loads a matrix with 4x4 values onto a uniform variable
     * @param location Location of the uniform variable
     * @param value Value to load into the uniform variable
     */
    public void load(int location, Matrix4f value) {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        value.get(floatBuffer);
        GL20.glUniformMatrix4fv(location, false, floatBuffer);
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
        this.bindAttributes();
        GL20.glLinkProgram(programId);
        GL20.glValidateProgram(programId);
        this.updateUniformLocations();
    }

    /**
     * Starts using the shader
     */
    public void bind() {
        GL20.glUseProgram(programId);
    }

    /**
     * Stops using the shader
     */
    public void unbind() {
        GL20.glUseProgram(0);
    }

    /**
     * Disposes/Deletes everything from OpenGL
     */
    public void dispose() {
        unbind();
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
