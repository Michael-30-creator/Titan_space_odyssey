package SolarSystem;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;
import PhysicsEngine.States.State;

public class Draw extends JPanel implements KeyListener {

    CelestialBody[] celestialBodies;
    Spaceship spaceship;
    int radius;
    static int index = 0;
    int x = 0;
    int y = 0;
    
    //images for planets and background
    Image finalIm;
    Image sun;
    Image venus;
    Image earth;
    Image moon;
    Image mars;
    Image jupiter;
    Image saturn;
    Image titan;
    Image spaceShip;
    Image background;
   
    //scaling and zooming in
    AffineTransform transform = new AffineTransform();
    int zoomOffsetX;
    int zoomOffsetY; 
    double zoomFactor = 1.0;   
    boolean zoomFlag = false;  
    int zoomedInX;
    int zoomedInY;
    int howZoomed = 0;

    public Draw() {
        //scaling each planet to its appropriate size
        //adding the planets as images

        //adding and scaling earth
        ImageIcon temp = new ImageIcon("earth.png");
        Image edit = temp.getImage();
        Image finalImg = edit.getScaledInstance(25,25,java.awt.Image.SCALE_SMOOTH);
        earth = new ImageIcon(finalImg).getImage(); 
        
        //adding and scaling sun
        temp = new ImageIcon("sun.png");
        Image edit1 = temp.getImage();
        Image finalImg1 = edit1.getScaledInstance(30,30,java.awt.Image.SCALE_SMOOTH);
        sun = new ImageIcon(finalImg1).getImage(); 

        //adding and scaling venus
        temp = new ImageIcon("venus.png");
        Image edit2 = temp.getImage();
        Image finalImg2 = edit2.getScaledInstance(20,20,java.awt.Image.SCALE_SMOOTH);
        venus = new ImageIcon(finalImg2).getImage(); 

        //adding and scaling moon
        temp = new ImageIcon("moon.png");
        Image edit3 = temp.getImage();
        Image finalImg3 = edit3.getScaledInstance(10,10,java.awt.Image.SCALE_SMOOTH);
        moon = new ImageIcon(finalImg3).getImage(); 

        //adding and scaling mars
        temp = new ImageIcon("mars.png");
        Image edit4 = temp.getImage();
        Image finalImg4 = edit4.getScaledInstance(30,30,java.awt.Image.SCALE_SMOOTH);
        mars = new ImageIcon(finalImg4).getImage(); 

        //adding and scaling jupiter
        temp = new ImageIcon("jupiter.png");
        Image edit5 = temp.getImage();
        Image finalImg5 = edit5.getScaledInstance(60,60,java.awt.Image.SCALE_SMOOTH);
        jupiter = new ImageIcon(finalImg5).getImage(); 

        //adding and scaling saturn
        temp = new ImageIcon("saturn.png");
        Image edit6 = temp.getImage();
        Image finalImg6 = edit6.getScaledInstance(130,130,java.awt.Image.SCALE_SMOOTH);
        saturn = new ImageIcon(finalImg6).getImage(); 

        //adding and scaling titan
        temp = new ImageIcon("titan.png");
        Image edit7 = temp.getImage();
        Image finalImg7 = edit7.getScaledInstance(10,10,java.awt.Image.SCALE_SMOOTH);
        titan = new ImageIcon(finalImg7).getImage();
         
        //adding and scaling the spaceship
        temp = new ImageIcon("spaceship.png");
        Image edit8 = temp.getImage();
        Image finalImg8 = edit8.getScaledInstance(10,10,java.awt.Image.SCALE_SMOOTH);
        spaceShip = new ImageIcon(finalImg8).getImage();

        //adding and scaling the background image
        temp = new ImageIcon("space.png");
        Image edit9 = temp.getImage();
        Image finalImg9 = edit9.getScaledInstance(1000,600,java.awt.Image.SCALE_SMOOTH);
        background = new ImageIcon(finalImg9).getImage();
        
        setFocusable(true);
        addKeyListener(this);
        requestFocus();
    }

    /**
     * Displays one frame for the given coordinates which are retrieved from the allPositions array in the State class
     * @param Graphics 
     * @return 
     */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        //for higher resolution
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);       
        
        //drawing the background
        g2.drawImage(background, 0, 0, null);
        
        //algorithm for zooming in on the planets 
        double scaleFactor = zoomFactor;

        int offsetX = (int) (getWidth() - getWidth() * scaleFactor);
        int offsetY = (int) (getHeight() - getHeight() * scaleFactor);
        
        g2.translate(offsetX, offsetY);
        g2.translate(zoomOffsetX,zoomOffsetY);
        g2.scale(scaleFactor, scaleFactor);
        
        //each celestial objects gets drawn into the Image
        for (int i = 0; i < 9 ; i++) {
            //stores the scaled down and casted x and y coordinates of the given celestial body, which is given by the index
             x = (int)Math.round(CelestialBody.scaleDownPosition(State.allPositions[i][index][0],i));
             y =-(int)Math.round(CelestialBody.scaleDownPosition(State.allPositions[i][index][1],i));
            
            switch(i){
                //sun
                case 0: 
                radius = 30;
                finalIm = sun;
                break;
                //venus
                case 1:
                radius = 15;
                finalIm = venus;
                break;
                //earth
                case 2:
                radius = 25;
                finalIm = earth;
                break;
                //moon
                case 3: 
                radius = 10;
                finalIm = moon;
                break;
                //mars
                case 4:
                radius = 30;
                finalIm = mars;
                break;
                //jupiter
                case 5: 
                radius = 60;
                finalIm = jupiter;
                break;
                //saturn
                case 6:
                radius = 60;
                finalIm = saturn;
                break;
                //titan
                case 7: 
                radius = 10;
                finalIm = titan;
                break;
                //spaceship
                case 8:
                radius = 5;
                finalIm = spaceShip;
                break;
            }

                //responsiple for leaving a line behind the space probe to track the trajectory of the probe
                if(i==8){ //spaceship
                    for (int j = 0; j < index; j++) {
                        g2.setColor(Color.WHITE);
                        //casting to an integer to draw a dot in the image later
                        int x2 = (int)Math.round(CelestialBody.scaleDownPosition(State.allPositions[i][j][0],i));
                        int y2 =  -(int)Math.round(CelestialBody.scaleDownPosition(State.allPositions[i][j][1],i));
                        int index1 = x2+450;
                        int index2 = y2+250;
                            
                        g2.drawLine(index1, index2, index1, index2);       
                    }
                } 
            //necesary shiftings to display the celestial body properly
            // index 6 = saturn
            if(i==6){
                g2.drawImage(finalIm, x+400, y+200, null);
            }
            else{
                g2.drawImage(finalIm, x+450, y+250, null);
            } 
        }
        //for each call of the method the index gets incremented to get new coordinates from the allPositions array for the next call
        if(!zoomFlag) //check if the repaint method is called by the zoomIn function.
        index++;
        else
        zoomFlag = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (howZoomed < 3) //ensures no more than 3 zoom ins
            {
                zoomFactor *= 1.5; // Increase zoom factor by 50%
                zoomFlag = true;
                zoomOffsetX = 20;
                zoomOffsetY = 250;
                howZoomed++;
                repaint();
                
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (howZoomed > 0) // ensures no zoom outs before a zoom in
            {
                zoomFactor /= 1.5; // Decrease zoom factor by 50%
                zoomFlag = true;
                zoomOffsetX = -150;
                zoomOffsetY = -20;
                howZoomed--;
                repaint();
            }
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
    }
}