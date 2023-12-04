package PhysicsEngine.Controller;

import PhysicsEngine.States.RocketState;

/**
 * This class handles the rotation of the spaceship during the landing
 */

public class RotationImpulse {

    private double angleOfRotation;
    private double angularVelocity;
    private double rotationTime;
    private double torque;

    /**
     * A Rotation Impulse can be used to store the combination of an angle with a given torque
     * This allows the calculation of how long the rotation will take, how far it will overshoot,
     * and how to correct the overshoot.
     * @param angle
     * @param torque
     */
    public RotationImpulse(double angle, double torque)
    {
        angleOfRotation = angle;
        this.torque = torque;
    }
    
    //Getter for the angle
    public double getAngle(){
        return angleOfRotation;
    }

    //Getter for the torque
    public double getTorque(){
        return torque;
    }

    //Getter for the rotation time
    public double getRotationTime(){
        return rotationTime;
    }

    //Setter for the angle
    public void setAngle(double newAngle){
        angleOfRotation = newAngle;
    }

    //Getter for the time needed for the rotation
    public double getTimeNeededForRotation()
    {
        return angleOfRotation/torque;
    }

    /**
     * calculates the angle that is overshot based on the time needed to rotate, since we can only
     * check the angle every timestep we do calculations, if we use the maxTorque, the
     * rotation will never be perfect
     * @return angle that is overshot
     */
    private double getOvershoot()
    {
        return torque - angleOfRotation%torque;
    }

    /**
     * rotates the overshoot back
     * @return torque needed to correct the overshoot
     */
    public double correctOverShoot()
    {
        return getOvershoot()*-1.0;
    }

    /**
     * stops correction of overshoot
     * @return torque needed to stand still in wanted angle
     */
    public double standStill()
    {
        return correctOverShoot()*-1.0;
    }


    /**
     * Plans the rotation of the spacecraft
     * Will give the amount of seconds needed with a specific torque
     * (The switch to deceleration will have to be done externally)
     * @param newAngle angle to rotate the spaceship
     */
    //CALL THIS TO DO THE ROTATION
    public void xRotationPlan(double newAngle){

        //calculates half the newAngle since the rotation will have 2 phases (acceleration and deceleration)
        double halfAngle = newAngle/2;
    
        //calculates the full amount of acceleration time needed
        double averageAccelerationAngle = halfAngle;
        double accelerationTime = calculateAccelerationTime(averageAccelerationAngle);

        if(accelerationTime % 1 != 0){
            accelerationTime = Math.ceil(accelerationTime);
        }
        
        double decelerationTime = accelerationTime;

        //calculates the average acceleration that will be used in the rotation
        double acceleration = calculateAcceleration(halfAngle, accelerationTime);
    
        torque = acceleration;
        rotationTime = accelerationTime + decelerationTime;

    }

    /**
     * Calculates the necessary amount of time to rotate the spacecraft at full acceleration
     * @param newAngle the new angle that you want the rocket to face 
     * @return the amount of time that the rocket has to accelerate to achieve the necessary velocity
     */
    public double calculateAccelerationTime(double newAngle){
        double time = Math.sqrt(newAngle*2);
        return time;
    }

    /**
     * Calculates the necessary acceleration to rotate the rocket at for it to reach the necessary angle in the specified amount of time
     * @param newAngle the new angle that you want the rocket to face 
     * @param time the amount of time that the rocket has to turn in
     * @return the acceleration needed 
     */
    public double calculateAcceleration(double newAngle, double time){
        double acceleration = (newAngle*2) / (time*time);
        return acceleration;
    }

}
