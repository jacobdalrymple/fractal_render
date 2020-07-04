package com.fractal_renderer;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

public class QuadMesh {

    private float[] vertices;
    private int[] vao;
    private int[] vbo;

    public QuadMesh(GL3 gl) {
        vertices = new float[] { -1.0f, -1.0f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f };
        vao = new int[1];
        vbo = new int[1];
        initBuffers(gl);
    }

    public void initBuffers(GL3 gl) {

        gl.glGenBuffers(1, vbo, 0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[0]);
        FloatBuffer fb = Buffers.newDirectFloatBuffer(vertices);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, Float.BYTES * vertices.length, fb, GL.GL_STATIC_DRAW);

        gl.glGenVertexArrays(1, vao, 0);
        gl.glBindVertexArray(vao[0]);
        gl.glEnableVertexAttribArray(0);

        gl.glVertexAttribPointer(0, 2, GL.GL_FLOAT, false, 4 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 2, GL.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        
        gl.glBindVertexArray(0);
    }

    public void render(GL3 gl) {
        gl.glBindVertexArray(vao[0]);
        gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, 4);
        gl.glBindVertexArray(0);
    }

    public void dispose(GL3 gl) {
        gl.glDeleteBuffers(1, vbo, 0);
        gl.glDeleteVertexArrays(1, vao, 0);
    }
}
