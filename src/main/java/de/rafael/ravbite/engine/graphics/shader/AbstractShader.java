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
import de.rafael.ravbite.engine.graphics.window.EngineWindow;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
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

    public void load(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    public void load(int location, boolean value) {
        GL20.glUniform1f(location, value ? 1 : 0);
    }

    public void load(int location, Vector3f value) {
        GL20.glUniform3f(location, value.x, value.y, value.z);
    }

    public void load(int location, Matrix4f value) {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        floatBuffer.put(0, value.m00);
        floatBuffer.put(1, value.m01);
        floatBuffer.put(2, value.m02);
        floatBuffer.put(3, value.m03);
        floatBuffer.put(4, value.m10);
        floatBuffer.put(5, value.m11);
        floatBuffer.put(6, value.m12);
        floatBuffer.put(7, value.m13);
        floatBuffer.put(8, value.m20);
        floatBuffer.put(9, value.m21);
        floatBuffer.put(10, value.m22);
        floatBuffer.put(11, value.m23);
        floatBuffer.put(12, value.m30);
        floatBuffer.put(13, value.m31);
        floatBuffer.put(14, value.m32);
        floatBuffer.put(15, value.m33);
        floatBuffer.flip();
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
