import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.IntBuffer; 

public class FractalRender_GLEventListener implements GLEventListener {

    private FrameBuffer frameBuffer;
    private Shader      fractalShader;
    private Shader      drawShader;
    private QuadMesh    quadFractal;
    private QuadMesh    quadRender;

    private double      startTime;
    private boolean     updateScreen;

    private float       zoomLevel;
    private float[]     fractalOffset;
    private float[]     oldFractalOffset;

    private float[]     startMousePos;
    private float[]     endMousePos;
    private boolean     imageDrag;


    public FractalRender_GLEventListener() {
    }

    public void init(GLAutoDrawable drawable) {

        startTime = getSeconds();
        updateScreen = true;
        zoomLevel = 1.0f;
        fractalOffset = new float[] {0.5f, 0.5f};
        oldFractalOffset = new float[] {0.5f, 0.5f};

        GL3 gl = drawable.getGL().getGL3();
        
        frameBuffer = new FrameBuffer(gl, FractalRender.WIDTH, FractalRender.HEIGHT);
        fractalShader = new Shader(gl, "./shaders/fractalVertexShader.vs", "./shaders/fractalFragShader.fs");
        drawShader = new Shader(gl, "./shaders/drawVertexShader.vs", "./shaders/drawFragShader.fs");
        quadFractal = new QuadMesh(gl);
        quadRender = new QuadMesh(gl);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glViewport(x, y, width, height);
        updateScreen = true;
        //redraw fractal texture??
    }

    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        render(gl);
    }

    public void dispose(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        frameBuffer.dispose(gl);
        quadFractal.dispose(gl);
        quadRender.dispose(gl);
    }

    public void render(GL3 gl) {

        float fractalPower = 2.0f;// + 0.01f * (float) Math.sin(getSeconds() - startTime);

        if (true) {
            frameBuffer.bindFramebuffer(gl);

            fractalShader.use(gl);
            fractalShader.configureVec3(gl, "zoomInfo", fractalOffset[0], fractalOffset[1], zoomLevel);
            fractalShader.configureFloat(gl, "fractalPower", fractalPower);
            quadFractal.render(gl);
    
            frameBuffer.unBindFramebuffer(gl);
            updateScreen = false;
        }

        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        drawShader.use(gl);
        gl.glBindTexture(GL.GL_TEXTURE_2D, frameBuffer.getTextureId());
        quadRender.render(gl);
    }

    public void drag(int mX, int mY) {

        updateScreen = true;

        float mouseX = (( (float) mX ) / ( (float) FractalRender.WIDTH ));
        float mouseY = 1.0f - (( (float) mY ) / ( (float) FractalRender.HEIGHT ));

        if (!imageDrag) {
            startMousePos = new float[] {mouseX, mouseY};
        }
        imageDrag = true;
        endMousePos = new float[] {mouseX, mouseY};

        float[] dragVector = new float[] {endMousePos[0] - startMousePos[0], endMousePos[1] - startMousePos[1]};

        fractalOffset[0] = oldFractalOffset[0] + dragVector[0]*zoomLevel;
        fractalOffset[1] = oldFractalOffset[1] + dragVector[1]*zoomLevel;
    }

    public void dragRelease() {
        imageDrag = false;
        oldFractalOffset = new float[] {fractalOffset[0], fractalOffset[1]};
    }

    public void zoomOut() {
        zoomLevel *= 1.1f;
        updateScreen = true;
    }

    public void zoomIn() {
        zoomLevel *= 0.9f;
        updateScreen = true;
 
    }
    public double getSeconds() {
        return System.currentTimeMillis()/1000.0;
    }
}
