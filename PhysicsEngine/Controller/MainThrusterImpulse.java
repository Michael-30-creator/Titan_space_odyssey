package PhysicsEngine.Controller;

import java.util.Arrays;

import PhysicsEngine.Operations.VectorOperations;

/**
 * This class handles the rocket's main thrusts during landing
 */

public class MainThrusterImpulse {

    private double thrust;
    private double startTimeOfImpulse;
    private double endTimeOfPulse;
    double[] currentVelocity;

    public MainThrusterImpulse(double thrust, double[] currentVelocity,double startTimeOfImpulse, double endTimeOfPulse )
    {
        this.thrust = thrust;
        this.currentVelocity = currentVelocity;
        this.startTimeOfImpulse = startTimeOfImpulse;
        this.endTimeOfPulse = endTimeOfPulse;
    }

    /**
     * Calculates the change in velocity when applying the thrusters.
     * Adds acceleration to the same direction
     * @return the change in velocity 
     */
    public double[] changeInVelocity()
    {
        double h = Math.sqrt(Math.pow(currentVelocity[0], 2) + Math.pow(currentVelocity[1], 2));

        h += thrust;

        double a = Math.cos(VectorOperations.calculateAngle(currentVelocity, new double[]{10,0})) * h;

        double b = Math.sin(VectorOperations.calculateAngle(currentVelocity, new double[]{10,0})) * h;

        double[] velocity = new double[2];
        velocity[0] = a;
        velocity[1] = b;

        return velocity;
    }

    /**
     * @return the end time of the impulse
     */
    public double getEndTimeOfPulse() {
        return endTimeOfPulse;
    }

    /**
     * @return the start time of the impulse
     */
    public double getStartTimeOfImpulse()
    {
        return startTimeOfImpulse;
    }

    /**
     * @return main thrust
     */
    public double getThrust() {
        return thrust;
    }
}
