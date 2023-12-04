package SolarSystem;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import PhysicsEngine.Simulations.Simulation;
import PhysicsEngine.Solvers.iSolver;

/**
 * This class represents the trajectory of the spaceship from Earth to Titan
 */

public class Runner {

    public static CelestialBody body = new CelestialBody();
    public static Draw drawPanel = new Draw();
    public static LandingDraw landing = new LandingDraw();
  
    public static void run(iSolver solver){

        //Enter time step in seconds here:
        double timeStep = 50;

        //new simulation running on the solver chosen in the Main class
        Simulation simulation = new Simulation(timeStep, solver);
        simulation.planetarySetUp();
       
        //main frame
        JFrame mainFrame = new JFrame("Solar System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 600);
        mainFrame.setLocationRelativeTo(null);
        //mainFrame.setUndecorated(true);
        mainFrame.add(drawPanel);
        ImageIcon icon = new ImageIcon("icon.jpg");
        mainFrame.setIconImage(icon.getImage());
        
        // timer to move celestial bodies
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            int a = 0;
            @Override
            public void run() {
                drawPanel.repaint();
                if (a == 99) {
                    t.cancel();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            
                            //when the first step of the simulation ends, we open up the landing frame
                            mainFrame.dispose();
                            LandingRunner.run();
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
