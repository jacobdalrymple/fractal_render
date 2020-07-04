package com.fractal_renderer;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.glsl.*;  

/**
Purpose of this class is to abstract the initialisation and methods of shaders
from the rest of the project.

@author Jacob Dalrymple
 */
public class Shader {

    private int    shaderID;
    private String[] vertexShaderString;
    private String[] fragmentShaderString;

    /**
    Initialisation method of the Shader class.
    @param gl           Opengl object.
    @param vertexPath   File directory path to the vertex shader.
    @param fragmentPath File directory path to the fragment shader.
     */
    public Shader(GL3 gl, String vertexPath, String fragmentPath) {
        try {

            //vertexPath = getShaderPath(vertexPath);
            //fragmentPath = getShaderPath(fragmentPath);

            vertexShaderString   = new String[] { new String(Files.readAllBytes(Paths.get(vertexPath)),   Charset.defaultCharset()) };
            fragmentShaderString = new String[] { new String(Files.readAllBytes(Paths.get(fragmentPath)), Charset.defaultCharset()) };
        } 
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR : Error when reading in vertex/shader sources!");
        }
        compileAndLink(gl);
    }

    private String[] getShaderCode(String shaderPath) {

        InputStream in = this.getClass().getResourceAsStream(shaderPath);
        Scanner s = new Scanner(in).useDelimiter("\\A");
        return new String[] {s.hasNext() ? s.next() : ""};
    }

    /**
    Initialises shader and sets the shaderID of the class.
    @param gl Opengl object.
     */
    private void compileAndLink(GL3 gl) {
        shaderID = gl.glCreateProgram();

        int vertShader = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
        gl.glShaderSource(vertShader, 1, vertexShaderString, null);
        gl.glCompileShader(vertShader);
        gl.glAttachShader(shaderID, vertShader);

        int fragShader = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);
        gl.glShaderSource(fragShader, 1, fragmentShaderString, null);
        gl.glCompileShader(fragShader);
        gl.glAttachShader(shaderID, fragShader);

        gl.glLinkProgram(shaderID);
    }

    /**
    Runs the shader.
    @param gl Opengl object.
     */
    public void use(GL3 gl) {
        gl.glUseProgram(shaderID);
    }

    /**
    Pass a float to shader.
    @param gl           Opengl object.
    @param variableName Name of variable in shader code.
    @param value        Float to pass to shader.
     */
    public void configureFloat(GL3 gl, String variableName, float value) {
        int location = gl.glGetUniformLocation(shaderID, variableName);
        gl.glUniform1f(location, value);
    }

    /**
     * Send vector of 2 floats to shader.
     * 
     * @param gl           Opengl object.
     * @param variableName Name of variable in shader code.
     * @param f1           Float in posistion 0 of vector (zero index).
     * @param f2           Float in posistion 1 of vector.
     */
    public void configureVec2(GL3 gl, String variableName, float f1, float f2) {
        int location = gl.glGetUniformLocation(shaderID, variableName);
        gl.glUniform2f(location, f1, f2);
    }

    /**
     * Send vector of 2 floats to shader.
     * 
     * @param gl           Opengl object.
     * @param variableName Name of variable in shader code.
     * @param vec2         Vector of floats.
     */
    public void configureVec2(GL3 gl, String variableName, float[] vec2) {
        int location = gl.glGetUniformLocation(shaderID, variableName);
        gl.glUniform2f(location, vec2[0], vec2[1]);
    }

    /**
    Send vector of 3 floats to shader.
    @param gl           Opengl object.
    @param variableName Name of variable in shader code.
    @param f1           Float in posistion 0 of vector (zero index).
    @param f2           Float in posistion 1 of vector.
    @param f3           Float in posistion 2 of vector.
     */
    public void configureVec3(GL3 gl, String variableName, float f1, float f2, float f3) {
        int location = gl.glGetUniformLocation(shaderID, variableName);
        gl.glUniform3f(location, f1, f2, f3);
    }

    /**
    Send vector of 3 floats to shader.
    @param gl           Opengl object.
    @param variableName Name of variable in shader code.
    @param vec3         Vector of floats.
     */
    public void configureVec3(GL3 gl, String variableName, float[] vec3) {
        int location = gl.glGetUniformLocation(shaderID, variableName);
        gl.glUniform3f(location, vec3[0], vec3[1], vec3[2]);
    }

    /**
    Returns the id of the shader.
    @return shaderID.
     */
    public int getShaderID() {
        return shaderID;
    }
}