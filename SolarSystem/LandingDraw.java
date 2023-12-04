package SolarSystem;

import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;

import PhysicsEngine.States.State;
import PhysicsEngine.Wind.Wind;

public class LandingDraw extends JPanel {
    //Iteration index
    static int i = 0;
    
    // Titan 
    int titanX = 0;
    int titanY = 0;
    int titanCenterX;
    int titanCenterY;
    
    //Spaceship
    int spaceshipCenterX;
    int spaceshipCenterY; 
    int spaceshipX; 
    static int spaceshipY; 
   

    //Angle rotation for every image used
    double titanRotationAngle = 0.0;
    double rotationAngle = 0.0;

    //Images
    Image titan;
    Image spaceShip;
    Image background;

    public LandingDraw() {
        // Scaling Titan and spaceship to appropriate sizes
        ImageIcon temp = new ImageIcon("titan.png");
        Image edit = temp.getImage();
        Image finalImg = edit.getScaledInstance(1600, 1600, java.awt.Image.SCALE_SMOOTH);
        titan = new ImageIcon(finalImg).getImage();

        temp = new ImageIcon("spaceship.png");
        Image edit1 = temp.getImage();
        Image finalImg1 = edit1.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
        spaceShip = new ImageIcon(finalImg1).getImage();

        temp = new ImageIcon("space.png");
        Image edit2 = temp.getImage();
        Image finalImg2 = edit2.getScaledInstance(1000, 600, java.awt.Image.SCALE_SMOOTH);
        background = new ImageIcon(finalImg2).getImage();

        setFocusable(true);
    }

    /**
     * Displays one frame for the given coordinates which are retrieved from the allPositions array in the State class
     * @param Graphics
     * @return
     */
    public void paintComponent(Graphics g) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // For higher resolution
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(background, 0, 0, null);



        // Draw Titan
        titanX = -300; // x-coordinate for Titan
        titanY = 400; // y-coordinate for Titan
        
        //if statement checking what iteration we are on 
        if(i < 100){

        titanCenterX = titanX + titan.getWidth(null) / 2;
        titanCenterY = titanY + titan.getHeight(null) / 2;


        //rotates Titan in the GUI
        AffineTransform titanTransform = g2.getTransform();
        g2.rotate(Math.toRadians(titanRotationAngle+(i/10)), titanCenterX, titanCenterY);
        g2.drawImage(titan, titanX, titanY, null);
        g2.setTransform(titanTransform);

        rotateSpaceship(-135);
        spaceshipCenterX = spaceshipX + spaceShip.getWidth(null) / 2;
        spaceshipCenterY = spaceshipY + spaceShip.getHeight(null) / 2;

        //rotates Spaceship in the GUI
        AffineTransform oldTransform = g2.getTransform();
        g2.rotate(Math.toRadians(rotationAngle)+State.landingPositionsAngle[i][2], spaceshipCenterX, spaceshipCenterY);
        spaceshipX =(int)(CelestialBody.scaleDownLanding(State.landingPositionsAngle[i][0])) + 485;
        spaceshipY =-(int)(CelestialBody.scaleDownLanding(State.landingPositionsAngle[i][1])) + 400;
        g2.drawImage(spaceShip, spaceshipX, spaceshipY, null);
        g2.setTransform(oldTransform);
        }

        else {
       
        g2.drawImage(titan, -300, 400, null);
        
        rotateSpaceship(-135);
        spaceshipCenterX = spaceshipX + spaceShip.getWidth(null) / 2;
        spaceshipCenterY = spaceshipY + spaceShip.getHeight(null) / 2;
        AffineTransform oldTransform = g2.getTransform();
        g2.rotate(Math.toRadians(rotationAngle), spaceshipCenterX, spaceshipCenterY);
        g2.drawImage(spaceShip , spaceshipX, spaceshipY, null);
        g2.setTransform(oldTransform);
        }    


        i++;
    }
    
    /**
     * rotates the spaceship
     * 
     * @param double
     * @return
     */
    public void rotateSpaceship(double degrees) {
        rotationAngle = degrees;
        repaint();
    }

    /**
     * rotates Titan
     * 
     * @param double
     * @return
     */
    public void rotateTitan(double degrees) {
        titanRotationAngle = degrees;
        repaint();
    }



}
