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
  Main class sets up the display and event handling of the application.
*/
public class FractalRender extends JFrame {

    public static final int WIDTH = 384*2;//512*2;
    public static final int HEIGHT = 384*2;
    private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
    private FractalRender_GLEventListener glEventListener;
    private final FPSAnimator animator;

    // GUI Elements
    private JLayeredPane layeredPane;
    private GLCanvas canvas;
    private JPanel fractalOptions;
    private JLabel fractalColourLabel;
    private JComboBox fractalColouringComboBox;
    private JLabel fractalPowerLabel;
    private SliderGUI fractalPowerSlider;
    private JTextField fractalPowerTextField;
    private JLabel iterationNumLabel;
    private SliderGUI iterationSlider;
    private JTextField iterationTextField;

    // variables for the GUI of the fractal iteration slider
    private int iterationSliderMinValue;
    private int iterationSliderMaxValue;
    // variables for the GUI of the fractal power slider
    private float fractalPowerRange;
    private int fractalSliderMinValue;
    private int fractalSliderMaxValue;

    public FractalRender(String titleBarText) {
        super(titleBarText);

        glEventListener = new FractalRender_GLEventListener(WIDTH, HEIGHT);
        
        // initialising the GUI components
        guiInitialisation();
        // layout all the GUI components
        configToolbarLayout();
        //configuring event listeners for all GUI components
        setEventListeners();

        animator = new FPSAnimator(canvas, 60);
        animator.start();
    }
    
    /**
     * Method initialises of all UI elements.
     */
    private void guiInitialisation() {
        GLCapabilities glCapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
        canvas = new GLCanvas(glCapabilities);

        layeredPane = new JLayeredPane();

        fractalOptions = new JPanel();
        fractalOptions.setBorder(BorderFactory.createTitledBorder(null, "Options for rendering fractal",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.CENTER,
                new java.awt.Font("Segoe UI", 0, 12)));

        fractalColourLabel = new JLabel();
        fractalColourLabel.setFont(new Font("Segoe UI", 0, 11));
        fractalColourLabel.setText("Fractal colouring");

        fractalColouringComboBox = new JComboBox(
                new String[] { "Black and White via modulo 2", "Item 2", "Item 3", "Item 4" });
        fractalColouringComboBox.setFont(new Font("Segoe UI", 0, 11));

        iterationNumLabel = new JLabel();
        iterationNumLabel.setFont(new Font("Segoe UI", 0, 11));
        iterationNumLabel.setText("Number of iterations");

        iterationTextField = new JTextField();
        iterationTextField.setText(" " + Integer.toString(glEventListener.getIterationNum()));

        iterationSliderMinValue = 0;
        iterationSliderMaxValue = 1000;
        iterationSlider = new SliderGUI(JSlider.HORIZONTAL, iterationSliderMinValue, iterationSliderMaxValue,
                glEventListener.getIterationNum());
        
        fractalPowerLabel = new JLabel();
        fractalPowerLabel.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        fractalPowerLabel.setText("Power of fractal");

        fractalPowerTextField = new JTextField();
        fractalPowerTextField.setText(" " + Float.toString(glEventListener.getFractalPower()));

        fractalPowerRange = 10f;
        fractalSliderMinValue = 0;
        fractalSliderMaxValue = 100;
        int fractalSliderAvg = (int) 0.5 * (fractalSliderMinValue + fractalSliderMaxValue);
        fractalPowerSlider = new SliderGUI(JSlider.HORIZONTAL, fractalSliderMinValue, fractalSliderMaxValue,
                fractalSliderAvg);
        fractalPowerSlider.setTrueToSliderValFunc(
                (float trueValue) -> (int) ((trueValue + fractalPowerRange) / 2 * fractalPowerRange));
        fractalPowerSlider.setSliderToTrueValFunc((int sliderValue) -> 2 * fractalPowerRange * sliderValue
                / (float) (fractalSliderMaxValue - fractalSliderMinValue) - fractalPowerRange);
    }

    /**
     * Method configures the placement of all UI elements.
     * (With the help of the Netbeans GUI builder)
     */
    public void configToolbarLayout() {
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

        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        fractalOptions.setBounds(WIDTH - 193, 0, 189, 198);
        layeredPane.add(fractalOptions);
        layeredPane.add(canvas);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(layeredPane, BorderLayout.CENTER);
    }
    
    /**
     * Method configures all the event handing of all UI elements.
     */
    private void setEventListeners() {

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

        //event listener for the fractal colouring drop down menu
        fractalColouringComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource();
                String colouringString = (String) comboBox.getSelectedItem();
                glEventListener.changeColouring(colouringString);
            }
        });

        //event listener for the fractal iteration slider
        iterationSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                int iterationNum = (int) iterationSlider.getTrueValue();
                iterationTextField.setText(Integer.toString(iterationNum));
                glEventListener.setIterationNum(iterationNum);
            }
        });

        //event listener for the fractal power slider
        fractalPowerSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                float fractalPower = fractalPowerSlider.getTrueValue();
                fractalPowerTextField.setText(String.format("%.1f", fractalPower));
                glEventListener.setFractalPower(fractalPower);
            }
        });

        //event listener for the fractal iteration text input
        iterationTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String textInput = iterationTextField.getText();
                int iterationNum;
                boolean validInput = false;
                try {
                    iterationNum = Integer.parseInt(textInput.trim());
                    if (iterationSlider.inSliderRange(iterationNum)) {
                        validInput = true;
                    } else {
                        iterationNum = glEventListener.getIterationNum();
                    }
                } catch (NumberFormatException e) {
                    iterationNum = glEventListener.getIterationNum();
                }
                if (!validInput) {
                    JOptionPane.showMessageDialog(getContentPane(),
                            "ERROR : Iteration number must be an integer and within the acceptable range (0 to 1000)");
                }
                iterationSlider.setValue(iterationNum);
                glEventListener.setIterationNum(iterationNum);
            }
        });

        //event listener for the fractal power text input
        fractalPowerTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String textInput = fractalPowerTextField.getText();
                float fractalPower;
                boolean validInput = false;
                try {
                    fractalPower = Float.parseFloat(textInput.trim());
                    if (fractalPower >= -fractalPowerRange && fractalPower <= fractalPowerRange) {
                        validInput = true;
                    } else {
                        fractalPower = glEventListener.getFractalPower();
                    }
                } catch (NumberFormatException e) {
                    fractalPower = glEventListener.getFractalPower();
                }
                if (!validInput) {
                    JOptionPane.showMessageDialog(getContentPane(),
                            "ERROR : Fractal power must be numeric and in the range (-10.0 to 10.0)");
                }
                fractalPowerSlider.setSliderPosition(fractalPower);
                glEventListener.setFractalPower(fractalPower);
            }
        });

        //event listener of the resizing of the application window
        getContentPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {

                Dimension newSize = componentEvent.getComponent().getBounds().getSize();
                int newWidth = (int) newSize.getWidth();
                int newHeight = (int) newSize.getHeight();

                layeredPane.removeAll();

                glEventListener.setFractalSize(newWidth, newHeight);
                canvas.setBounds(0, 0, newWidth, newHeight);
                fractalOptions.setBounds(newWidth - 193, 0, 189, 198);

                layeredPane.add(fractalOptions);
                layeredPane.add(canvas);
                getContentPane().add(layeredPane, BorderLayout.CENTER);
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                animator.stop();
                remove(canvas);
                dispose();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        FractalRender f = new FractalRender("Fractal Render");
        f.getContentPane().setPreferredSize(dimension);
        f.pack();
        f.setVisible(true);
    }

}