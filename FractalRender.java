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
        
        JLayeredPane layeredPane = new JLayeredPane();
        configFractalOptionToolbar();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        fractalOptions.setBounds(WIDTH - 193, 0, 189, 198);

        layeredPane.add(fractalOptions);
        layeredPane.add(canvas);
        getContentPane().add(layeredPane);
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


    /**
     * Method configures the UI elements of the toolbar used to changed the options
     * for rendering the fractal.
     */
    public void configFractalOptionToolbar() {

        fractalOptions           = new JPanel();
        fractalColourLabel       = new JLabel();
        fractalColouringComboBox = new JComboBox<>();
        iterationNumLabel        = new JLabel();
        iterationTextField       = new JTextField();
        iterationSlider          = new JSlider();
        fractalPowerTextField    = new JTextField();
        fractalPowerSlider       = new JSlider();
        fractalPowerLabel        = new JLabel();
        
        fractalOptions.setBorder(javax.swing.BorderFactory.createTitledBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)),
                "Options for rendering fractal", 
                javax.swing.border.TitledBorder.CENTER, 
                javax.swing.border.TitledBorder.CENTER, new java.awt.Font("Segoe UI", 0, 12))); // NOI18N

        fractalColourLabel.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        fractalColourLabel.setText("Fractal colouring");

        fractalColouringComboBox.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        fractalColouringComboBox.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        iterationNumLabel.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        iterationNumLabel.setText("Number of iterations");

        iterationTextField.setText("500");

        fractalPowerTextField.setText("500");

        fractalPowerLabel.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        fractalPowerLabel.setText("Power of fractal");

        javax.swing.GroupLayout fractalOptionsLayout = new javax.swing.GroupLayout(fractalOptions);
        fractalOptions.setLayout(fractalOptionsLayout);
        fractalOptionsLayout.setHorizontalGroup(fractalOptionsLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(fractalOptionsLayout.createSequentialGroup().addContainerGap().addGroup(fractalOptionsLayout
                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(fractalOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(fractalColouringComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 157,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fractalColourLabel, javax.swing.GroupLayout.Alignment.LEADING))
                        .addComponent(iterationNumLabel)
                        .addGroup(fractalOptionsLayout.createSequentialGroup()
                                .addComponent(iterationTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(iterationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 126,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(fractalOptionsLayout.createSequentialGroup()
                                .addComponent(fractalPowerTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fractalPowerSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 126,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(fractalPowerLabel))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        fractalOptionsLayout.setVerticalGroup(fractalOptionsLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(fractalOptionsLayout.createSequentialGroup().addContainerGap()
                        .addComponent(fractalColourLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fractalColouringComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(iterationNumLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(fractalOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(iterationTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(iterationSlider, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fractalPowerLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(fractalOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(fractalPowerTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(fractalPowerSlider, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    public static void main(String[] args) {
        FractalRender f = new FractalRender("Fractal Render");
        f.getContentPane().setPreferredSize(dimension);
        f.pack();
        f.setVisible(true);
    }

    private JPanel fractalOptions;
    private JLabel fractalColourLabel;
    private JComboBox<String> fractalColouringComboBox;
    private JLabel fractalPowerLabel;
    private JSlider fractalPowerSlider;
    private JTextField fractalPowerTextField;
    private JLabel iterationNumLabel;
    private JSlider iterationSlider;
    private JTextField iterationTextField;

}