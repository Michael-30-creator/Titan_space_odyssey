package SolarSystem;
import java.awt.geom.Point2D;

/**
 * This class represents the spaceship 
 */

public class Spaceship extends CelestialBody {

    CelestialBody celestialBody;
    public double velocity;
    public double acceleration;
    public double x1;
    public double x2;
    public double mass = 50000;


    public Spaceship(double mass, double radius, int rowInState, String name){
        super(mass, radius, rowInState, name);
    }

    public Point2D.Double setCoordinates(double x1, double x2) {
        double x = x1;
        double y = x2;
        return new Point2D.Double(x, y);
    }

    public void setPosition(double x1, double x2) {
        this.x1 = x1;
        this.x2 = x2;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }    

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration (double acceleration) {
        this.acceleration = acceleration;
    }
}
