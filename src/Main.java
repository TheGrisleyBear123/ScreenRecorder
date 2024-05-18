import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws AWTException, IOException {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI(){
        JFrame frame = new JFrame("Screen");
        frame.setUndecorated(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setAlwaysOnTop(true);
        frame.setBackground(new Color(0,0,0,50));

        CapturePane capturePane = new CapturePane();
        frame.add(capturePane);
        frame.setVisible(true);

    }

     static class CapturePane extends JComponent{
        private Point start;
        private Point end;

        public CapturePane(){
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    //start point for e gets point
                    start = e.getPoint();
                    end = start;
                    repaint();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    //end point for e gets point
                    end = e.getPoint();
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    try {
                        takeCapture();
                    } catch (AWTException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    System.exit(0);
                }


            };

            addMouseListener(mouseAdapter);
            addMouseMotionListener(mouseAdapter);
        }

         @Override
         protected void paintComponent(Graphics g) {
             super.paintComponent(g);

             if (start != null && end != null) {
                 Graphics2D g2d = (Graphics2D) g.create();
                 g2d.setColor(Color.BLACK);
                 g2d.setStroke(new BasicStroke(2));
                 Rectangle rect = new Rectangle(start);
                 rect.add(end);
                 g2d.draw(rect);
                 g2d.dispose();
             }
         }
         public void takeCapture() throws AWTException, IOException {
             Rectangle captureRect = new Rectangle(start);
             captureRect.add(end);
             Robot robot = new Robot();
             BufferedImage img = robot.createScreenCapture(captureRect);
             ImageIO.write(img, "png", new File(System.currentTimeMillis() + ".png"));
             System.out.println("SCREENS");
         }
     }


}