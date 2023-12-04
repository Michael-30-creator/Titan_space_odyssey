package PhysicsEngine.Controller;

import PhysicsEngine.Operations.VectorOperations;
import SolarSystem.CelestialBody;

import java.util.LinkedList;
import java.util.Queue;

import javax.xml.crypto.Data;

/**
 * This class implements an Open-Loop Controller 
 * implements the iController interface
 */

public class OpenLoopController implements iController{
    
    //Final Values needed
    //NOTE: Values are in km, not m. (Apart from angle)
    final double xFINAL = 0.0001;
    final double xVelocityFINAL = 0.0001;
    final double yVelocityFINAL = 0.0001;
    final double thetaFINAL = 0.02;
    final double angularVelocityFINAL = 0.01;

    //Max constraints for some values
    final double g = 0.001352;
    final double maxThrust = 10*g;
    final double SIZE_OF_SPACESHIP = 0.1; //100 meters

    static public double[] LANDING_POSITION;
    private double[] currentVelocity;
    double[] UV = new double[2];

    RotationImpulseOLC currentRotationImpulse;
    MainThrusterImpulse currentMainThrustImpulse;

    //stores precalculated rotation operations which are needed during the landing process
    private Queue<RotationImpulseOLC> DataStorageRotationImpulse = new LinkedList();
    private Queue<MainThrusterImpulse> DataStorageMainThrustImpulse = new LinkedList();

    /**
     * The Open Loop Controller uses a set of anticipated actions that will lead us to a successful completion of the landing
     * under normal condition (e.g. no wind). This controller is simple and easy to understand. 
     * 
     * We selected main thrusts that will lead us to have the anticipated velocity (<0.1m/s) at the wanted final distance (<0.1m)
     * to the landing spot. This was done by calculating our most optimal path by hand.
     * Additionally, we also correct the initial position of the spaceship, so that it is in the correct angle for landing (<0.02radians)
     * with a zero angular velocity.
     * @param landingPosition
     * @param currentVelocity
     */
    public OpenLoopController(double[] landingPosition, double[] currentVelocity)
    {
        double[] velocities = new double[2];
        velocities[0] = currentVelocity[0];
        velocities[1] = currentVelocity[1];
        this.currentVelocity = velocities;
        this.LANDING_POSITION = landingPosition;
        initialDataStorageRotationImpulse();
        initialDataStorageMainThrustImpulse();

    }

    /**
     * Initializes the pre-calculated rotation thrusts and stores them in the queue to be executed
     */
    public void initialDataStorageRotationImpulse(){
        RotationImpulseOLC rotation1 = new RotationImpulseOLC(VectorOperations.calculateAngle(currentVelocity, new double[] {10,0}),0,20);
        DataStorageRotationImpulse.add(rotation1);
        RotationImpulseOLC rotation2 = new RotationImpulseOLC(2*Math.PI,200,100);
        DataStorageRotationImpulse.add(rotation2);
    }

    /**
     * Initializes the pre-calculated main thrusts and stores them in the queue to be executed
     */
    public void initialDataStorageMainThrustImpulse(){
        currentVelocity = new double[] {0,0};
        //Big Deceleration thrust
        MainThrusterImpulse impulse1 = new MainThrusterImpulse(maxThrust, currentVelocity, 357, 395);
        DataStorageMainThrustImpulse.add(impulse1);

        //Smaller deceleration thrusts to reach wanted velocity while still descending
        MainThrusterImpulse impulse2 = new MainThrusterImpulse(0.00215, currentVelocity, 397, 407);
        DataStorageMainThrustImpulse.add(impulse2);
        MainThrusterImpulse impulse3 = new MainThrusterImpulse(g, currentVelocity, 408, 424);
        DataStorageMainThrustImpulse.add(impulse3);
        MainThrusterImpulse impulse4 = new MainThrusterImpulse(g+3E-4, currentVelocity, 425, 425);
        DataStorageMainThrustImpulse.add(impulse4);
        MainThrusterImpulse impulse5 = new MainThrusterImpulse(g+5E-5, currentVelocity, 426, 428);
        DataStorageMainThrustImpulse.add(impulse5);
        MainThrusterImpulse impulse6 = new MainThrusterImpulse(g+2.3600000000258766E-4-9E-5, currentVelocity, 429, 430);
        DataStorageMainThrustImpulse.add(impulse6);
        MainThrusterImpulse impulse7 = new MainThrusterImpulse(1.442E-3, currentVelocity, 430, 430);
        DataStorageMainThrustImpulse.add(impulse7);

        //Final impulse to stop
        MainThrusterImpulse impulse8 = new MainThrusterImpulse(g, currentVelocity, 431, 500);
        DataStorageMainThrustImpulse.add(impulse8);
    }


    @Override
    /**
     * Based on the time, this method gives the respective main thrust or torque that is scheduled at that second
     * @param state the state of the spaceship with position, velocity and angle
     * @param time in seconds
     * @return an array, UV[0] = u and UV[1] = v
     */
    public double[] getUV(double[][] state, int time) {

    checkRotationQueue(time);

    checkImpulseQueue(time);

        return UV;
    }

    /**
     * checks if queue needs to be dequeued and if so updates currentRotationImpulse
     * @param time in seconds
     */
    public void  checkRotationQueue(int time){
        if(!DataStorageRotationImpulse.isEmpty()) {

            if(DataStorageRotationImpulse.peek().getStartTimeTorqueAcceleration() == time){
                currentRotationImpulse = DataStorageRotationImpulse.peek();
                DataStorageRotationImpulse.remove();
            }
        }
        if(currentRotationImpulse != null){
            handleCurrentRotation((int)time);
        }

    }

    /**
     * checks if queue needs to be dequeued and if so updates currentMainThrustImpulse
     * @param time in seconds
     */
    public void checkImpulseQueue(int time){
        if(!DataStorageMainThrustImpulse.isEmpty()){

            if(DataStorageMainThrustImpulse.peek().getStartTimeOfImpulse() == time){
                currentMainThrustImpulse = DataStorageMainThrustImpulse.peek();
                DataStorageMainThrustImpulse.remove();
            }
        }
        if(currentMainThrustImpulse != null){
            handleCurrentMainThrust((int)time);
        }
    }


    /**
     * calculates the time needed to appraoch the landing position based on the starting velocity and the alowed thrust
     * @param distanceVector distance from spaceship to titan
     * @param velocity beginning velocity
     * @return time in seconds needed
     */
    private static double calculateTimeNeededToApproach(double[] distanceVector, double[] velocity)
    {
        double distance = Math.sqrt(Math.pow(distanceVector[0], 2) + Math.pow(distanceVector[1], 2));
        double velocityReach = Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));

        return distance/velocityReach;
    }

    /**
     * Methods sets the appropriate torque if a change is required given by time
     * @param time in seconds
     */
    public void handleCurrentRotation(int time){

        if(time == currentRotationImpulse.getStartTimeTorqueAcceleration())
        {
                UV[1]= currentRotationImpulse.getTorqueAcceleration();
            }
        else if (time == currentRotationImpulse.getEndTimeTorqueDeceleration())
        {
            UV[1]= currentRotationImpulse.getTorqueDeceleration();
        }
        else{
            UV[1] = 0;
        }

    }

    /**
     * Methods sets the appropriate thrust if a change is required given by time
     * @param time in seconds
     */
    public void handleCurrentMainThrust(int time){
        if(time>= currentMainThrustImpulse.getStartTimeOfImpulse() && time<= currentMainThrustImpulse.getEndTimeOfPulse()){
            UV[0] = currentMainThrustImpulse.getThrust();
        }
        else{
            UV[0] = 0;
        }

    }

    /**
     * Returns the difference in positions of the landing position on Titan and a celestial body's position
     * @param subject a celestial body's position
     * @return position of the celestial body relative to the landing position
     */
    private double[] getPositionRelativeToTitan(double[] subject)
    {
        return VectorOperations.vectorSubtraction(subject, LANDING_POSITION);
    }

    /**
     * Returns the difference in positions of the spaceship's current position and  a celestial body's position
     * @param subject a celestial body's position
     * @return position of celestial body relative to spaceship's position
     */
    private double[] getPositionRelativeToSpaceship(double[] subject)
    {
        return VectorOperations.vectorSubtraction(LANDING_POSITION,subject);
    }

    

}
