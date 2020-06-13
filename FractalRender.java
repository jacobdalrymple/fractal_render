/*
  Code has been adapted from code provide from exercises.
  I declare that this code is my own work and code provided from the exercises.
  Author Jacob Dalrymple  jdalrymple1@sheffield.ac.uk 
*/

import javax.swing.JFrame;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;


/*
  Main class sets up the display and interface of the application.
*/
public class FractalRender extends JFrame {

    public static final int WIDTH = 384*2;//512*2;
    public static final int HEIGHT = 384*2;
    private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
    private GLCanvas canvas;
    private FractalRender_GLEventListener glEventListener;
    private final FPSAnimator animator;

    public FractalRender(String titleBarText) {
        super(titleBarText);
        GLCapabilities glCapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
        canvas = new GLCanvas(glCapabilities);
        glEventListener = new FractalRender_GLEventListener();
        canvas.addGLEventListener(glEventListener);

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point mousePoint = e.getPoint();
                glEventListener.drag(mousePoint.x, mousePoint.y);
            }
        });
        canvas.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                glEventListener.dragRelease();
            }
        });
        canvas.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                char key = e.getKeyChar();
                if (key == 'a' || key == 'A')
                    glEventListener.zoomIn();
                else if (key == 'z' || key == 'Z')
                    glEventListener.zoomOut();
            }
        });

        getContentPane().add(canvas, BorderLayout.CENTER);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                animator.stop();
                remove(canvas);
                dispose();
                System.exit(0);
            }
        });
        animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    public static void main(String[] args) {
        FractalRender f = new FractalRender("Fractal Render");
        f.getContentPane().setPreferredSize(dimension);
        f.pack();
        f.setVisible(true);
    }
}