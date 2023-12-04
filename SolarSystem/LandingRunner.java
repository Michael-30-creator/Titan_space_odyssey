package SolarSystem;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This class represents the landing of the spaceship on Titan
 */

public class LandingRunner {

    public static LandingDraw landing = new LandingDraw();

    public static void run() {

        // main frame
        JFrame mainFrame = new JFrame("Landing");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 600);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.add(landing);

        // Timer to move celestial bodies
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            int a = 0;
            @Override
            public void run() {
                landing.repaint();
                if (a == 99) {
                    t.cancel();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            // Animation finished
                        }
                    });
                }
                a++;
            }
        };
        t.schedule(tt, 0, 100);

        mainFrame.setVisible(true);
    }
}