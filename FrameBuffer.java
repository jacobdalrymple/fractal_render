import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

/**
This class handles the operations on the frameBuffer.
@author Jacob Dalrymple
 */
public class FrameBuffer {

    private int[] frameBufferId;
    private int[] textureId;
    private int[] textureDimesions; //[x, y]

    /**
    Setups the framebuffer.
    @param gl   Opengl object.
    @param texX Width of the texture to be writen to the frame buffer.
    @param texY Height of the texture to be writen to the frame buffer.
     */
    public FrameBuffer(GL3 gl, int texX, int texY) {

        textureId = new int[1];
        frameBufferId = new int[1];
        textureDimesions = new int[] {texX, texY};

        gl.glGenFramebuffers(1, frameBufferId, 0);
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, frameBufferId[0]);

        gl.glGenTextures(1, textureId, 0);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textureId[0]);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, texX, texY, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, null);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

        gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, GL.GL_TEXTURE_2D, textureId[0], 0);

        if(gl.glCheckFramebufferStatus(GL.GL_FRAMEBUFFER) != GL.GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("ERROR FRAMEBUFFER NOT COMPLETE");
        }

        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
    }

    /**
    Binds the frame buffer so that is ready to be written to.
    @param gl Opengl object.
     */
    public void bindFramebuffer(GL3 gl) {
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, frameBufferId[0]);
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    }

    /**
    Un-binds the frame buffer so that is ready to be written to.
    @param gl Opengl object.
     */
    public void unBindFramebuffer(GL3 gl) {
        gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
    }

    /**
    Deletes framebuffer from memory.
    @param gl Opengl object.
     */
    public void dispose(GL3 gl) {
        gl.glDeleteFramebuffers(1, frameBufferId, 0);
    }

    public int getTextureId() {
        return textureId[0];
    }
}