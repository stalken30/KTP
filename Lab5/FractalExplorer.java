package Lab5;


import java.awt.*;
import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;
import javax.swing.JFileChooser.*;
import javax.swing.filechooser.*;
import javax.imageio.ImageIO.*;
import java.awt.image.*;

/**
 * This class allows for examination of different parts of the fractal by
 * creating and showing a Swing GUI and handles events caused by various
 * user interactions.
 */
public class FractalExplorer
{
    /** Fields for save button, reset button, and combo box for enableUI. **/
    private JButton saveButton;
    private JButton resetButton;
    private JComboBox myComboBox;

    /** Number of rows remaining to be drawn. **/
    private int rowsRemaining;

    /** Integer display size is the width and height of display in pixels. **/
    private int displaySize;

    /**
     * JImageDisplay reference to update display from various methods as
     * the fractal is computed.
     */
    private JImageDisplay display;

    /** A FractalGenerator object for every type of fractal. **/
    private FractalGenerator fractal;

    /**
     * A Rectangle2D.Double object which specifies the range of the complex
     * that which we are currently displaying. 
     */
    private Rectangle2D.Double range;

    /**
     * A constructor that takes a display-size, stores it, and
     * initializes the range and fractal-generator objects.
     */
    public FractalExplorer(int size) {
        // Stores display-size
        displaySize = size;

        // Initializes the fractal-generator and range objects.
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
        display = new JImageDisplay(displaySize, displaySize);

    }

    /**
     * This method intializes the Swing GUI with a JFrame holding the 
     * JImageDisplay object and a button to reset the display, a button
     * to save the current fractal image, and a JComboBox to select the
     * type of fractal.  The JComboBox is held in a JPanel with a label.
     */
    public void createAndShowGUI()
    {
        display.setLayout(new BorderLayout());
        JFrame frame = new JFrame("Fractal Explorer");
        frame.add(display, BorderLayout.CENTER);
        resetButton = new JButton("Reset");
        ButtonHandler resetHandler = new ButtonHandler();
        resetButton.addActionListener(resetHandler);
        MouseHandler click = new MouseHandler();
        display.addMouseListener(click);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myComboBox = new JComboBox();

        FractalGenerator mandelbrotFractal = new Mandelbrot();
        myComboBox.addItem(mandelbrotFractal);
        FractalGenerator tricornFractal = new Tricorn();
        myComboBox.addItem(tricornFractal);
        FractalGenerator burningShipFractal = new BurningShip();
        myComboBox.addItem(burningShipFractal);

        ButtonHandler fractalChooser = new ButtonHandler();
        myComboBox.addActionListener(fractalChooser);

        JPanel myPanel = new JPanel();
        JLabel myLabel = new JLabel("Fractal:");
        myPanel.add(myLabel);
        myPanel.add(myComboBox);
        frame.add(myPanel, BorderLayout.NORTH);

        saveButton = new JButton("Save");
        JPanel myBottomPanel = new JPanel();
        myBottomPanel.add(saveButton);
        myBottomPanel.add(resetButton);
        frame.add(myBottomPanel, BorderLayout.SOUTH);

        ButtonHandler saveHandler = new ButtonHandler();
        saveButton.addActionListener(saveHandler);


        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

    }

    /** Private helper method to display the fractal.  **/
    private void drawFractal()
    {
        enableUI(false);
        
        rowsRemaining = displaySize;
        
        for (int x=0; x<displaySize; x++){
            FractalWorker drawRow = new FractalWorker(x);
            drawRow.execute();
        }

    }
    /**
     * Enables or disables the interface's buttons and combo-box based on the
     * specified value.  Updates the enabled-state of the save button, reset
     * button, and combo-box.
     */
    private void enableUI(boolean val) {
        myComboBox.setEnabled(val);
        resetButton.setEnabled(val);
        saveButton.setEnabled(val);
    }
    /**
     * An inner class to handle ActionListener events.
     */
    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            
            if (e.getSource() instanceof JComboBox) {
                JComboBox mySource = (JComboBox) e.getSource();
                fractal = (FractalGenerator) mySource.getSelectedItem();
                fractal.getInitialRange(range);
                drawFractal();

            }

            else if (command.equals("Reset")) {
                fractal.getInitialRange(range);
                drawFractal();
            }

            else if (command.equals("Save")) {
                
                JFileChooser myFileChooser = new JFileChooser();
                
                FileFilter extensionFilter =
                        new FileNameExtensionFilter("PNG Images", "png");
                myFileChooser.setFileFilter(extensionFilter);

                myFileChooser.setAcceptAllFileFilterUsed(false);
                
                int userSelection = myFileChooser.showSaveDialog(display);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    
                    java.io.File file = myFileChooser.getSelectedFile();
                    String file_name = file.toString();

                    try {
                        BufferedImage displayImage = display.getImage();
                        javax.imageio.ImageIO.write(displayImage, "png", file);
                    }

                    catch (Exception exception) {
                        JOptionPane.showMessageDialog(display,
                                exception.getMessage(), "Cannot Save Image",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                // If the file-save operation is not APPROVE_OPTION, return.
                else return;
            }
        }
    }


    private class MouseHandler extends MouseAdapter
    {

        @Override
        public void mouseClicked(MouseEvent e)
        {
            // Return immediately if rowsRemaining is nonzero.
            if (rowsRemaining != 0) {
                return;
            }
            // Get x coordinate of display area of mouse click.
            int x = e.getX();
            double xCoord = fractal.getCoord(range.x,
                    range.x + range.width, displaySize, x);

            // Get y coordinate of display area of mouse click.
            int y = e.getY();
            double yCoord = fractal.getCoord(range.y,
                    range.y + range.height, displaySize, y);

            // Call the generator's recenterAndZoomRange() method with
            // coordinates that were clicked and a 0.5 scale.
            fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);

            // Redraw the fractal after the area being displayed has changed.
            drawFractal();
        }
    }
    
    private class FractalWorker extends SwingWorker<Object, Object>
    {
        int yCoordinate;
        int[] computedRGBValues;
        
        private FractalWorker(int row) {
            yCoordinate = row;
        }
        
        protected Object doInBackground() {

            computedRGBValues = new int[displaySize];

            // Iterate through all pixels in the row.
            for (int i = 0; i < computedRGBValues.length; i++) {

                // Find the corresponding coordinates xCoord and yCoord
                // in the fractal's display area.
                double xCoord = fractal.getCoord(range.x,
                        range.x + range.width, displaySize, i);
                double yCoord = fractal.getCoord(range.y,
                        range.y + range.height, displaySize, yCoordinate);

                // Compute the number of iterations for the coordinates in
                // the fractal's display area.
                int iteration = fractal.numIterations(xCoord, yCoord);

                // If number of iterations is -1, set current int in the
                // computed RGB values int array to black.
                if (iteration == -1){
                    computedRGBValues[i] = 0;
                }

                else {
                    // Otherwise, choose a hue value based on the number
                    //of iterations.
                    float hue = 1.7f + (float) iteration / 20f;
                    int rgbColor = Color.HSBtoRGB(hue, 10f, 1f);

                    // Update the int array with the color for 
                    // the current pixel.
                    computedRGBValues[i] = rgbColor;
                }
            }
            return null;

        }
        protected void done() {
            // Iterate over the array of row-data, drawing in the pixels
            // that were computed in doInBackground().  Redraw the row
            // that was changed.
            for (int i = 0; i < computedRGBValues.length; i++) {
                display.drawPixel(i, yCoordinate, computedRGBValues[i]);
            }
            display.repaint(0, 0, yCoordinate, displaySize, 1);

            // Decrement rows remaining.  If 0, call enableUI(true)
            rowsRemaining--;
            if (rowsRemaining == 0) {
                enableUI(true);
            }
        }
    }
    
    public static void main(String[] args)
    {
        FractalExplorer displayExplorer = new FractalExplorer(600);
        displayExplorer.createAndShowGUI();
        displayExplorer.drawFractal();
    }

}
