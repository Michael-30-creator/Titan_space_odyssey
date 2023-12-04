package PhysicsEngine.Controller;

/**
 * This class handles the rotation of the spaceship for the Open-Loop Controller during the landing
 */

public class RotationImpulseOLC {

    private double targetAngle;
    private double torqueAcceleration;
    private double torqueDeceleration;
    private int startTimeTorqueAcceleration;
    private int startTimeTorqueDeceleration;
    private int endTimeTorqueDeceleration;
    private int time;
    public final double VMAX = 1;
    public final int maxTimeRotation = 4;


    /**
     * Used in Open Loop Controller. Stores the wanted change in angle and when it should happen
     * @param changeInAngle change in angle
     * @param startTimeTorqueAcceleration start time of the acceleration applied
     */
    public RotationImpulseOLC(double changeInAngle, int startTimeTorqueAcceleration)
    {
        this.startTimeTorqueAcceleration = startTimeTorqueAcceleration;
        this.targetAngle = -changeInAngle; 
        changeAngle();
    }

    public RotationImpulseOLC(double changeInAngle, int startTimeTorqueAcceleration, int timeOfRotation)
    {
        this.startTimeTorqueAcceleration = startTimeTorqueAcceleration;
        this.endTimeTorqueDeceleration = startTimeTorqueAcceleration + timeOfRotation;
        this.targetAngle = -changeInAngle; 
        this.torqueAcceleration = - calculateTorqueNeeded(changeInAngle, timeOfRotation);
        this.torqueDeceleration = - torqueAcceleration;
    }

    //Setter for target angle
    public void setTargetAngle(double angle){
        targetAngle = angle;
    }

    /**
     * Calculates the amount of torque needed to do a rotation
     * @param changeInAngle rotation of the spaceship
     * @param timeOfRotation the time of the rotation
     * @return torque needed to do the rotation
     */
    private double calculateTorqueNeeded(double changeInAngle, double timeOfRotation)
    {
        if(changeInAngle/timeOfRotation <= 1)
        return changeInAngle/timeOfRotation;

        else
        System.out.println("Error: maximum Torque is surpassed");
        return 0;
    }


    /**
     Calculates the time which is needed to do the acceleration period to change the rocket for a given angle. This acceleration will also be
     needed to slow to rotation down again to reach the desired angle.So in conclusion we first accelerate and then decelerate again to reach the desired angle
     we would like the rocket to have.
     * @param targetAngle the new angle that you want the rocket to face
     * @return the time needed to do one of the two operations
     */
    private int calculateTimeNeeded(double targetAngle){

        double intermediateAngle = targetAngle/2;

        for (int t = 0; t <= maxTimeRotation; t++) {
            if(Math.abs(intermediateAngle/t) <= VMAX){
                return t;
            }
        }
        return 0;
    }


    /**
     Calculates acceleration that is needed to rotate the rocket in a specific time to a given angle.
     * @param targetAngle the new angle that you want the rocket to face
     * @param t the amount of time that the rocket has to turn in
     * @return acceleration needed to rotate
     */
    private double calculateVMax(double targetAngle, double t){
        double intermediateAngle = targetAngle/2;
        return intermediateAngle/Math.pow(t,2);
    }


    /**
     Calculates acceleration and deceleration to rotate the rocket to the given angle.
     */
    public void changeAngle(){

        time = calculateTimeNeeded(targetAngle);
        startTimeTorqueDeceleration = time + startTimeTorqueAcceleration;
        endTimeTorqueDeceleration = startTimeTorqueDeceleration + time;
        torqueAcceleration = calculateVMax(targetAngle,time);
        torqueDeceleration = - torqueAcceleration;
    }

   
    public double getTargetAngle() {
        return targetAngle;
    }

    public int getEndTimeTorqueDeceleration() {
        return endTimeTorqueDeceleration;
    }

    public double getTorqueAcceleration() {
        return torqueAcceleration;
    }

    public void setTorqueAcceleration(double torqueAcceleration) {
        this.torqueAcceleration = torqueAcceleration;
    }

    public double getTorqueDeceleration() {
        return torqueDeceleration;
    }

    public void setTorqueDeceleration(double torqueDeceleration) {
        this.torqueDeceleration = torqueDeceleration;
    }

    public int getStartTimeTorqueAcceleration() {
        return startTimeTorqueAcceleration;
    }

    public void setStartTimeTorqueAcceleration(int startTimeTorqueAcceleration) {
        this.startTimeTorqueAcceleration = startTimeTorqueAcceleration;
    }

    public int getStartTimeTorqueDeceleration() {
        return startTimeTorqueDeceleration;
    }

    public void setStartTimeTorqueDeceleration(int startTimeTorqueDeceleration) {
        this.startTimeTorqueDeceleration = startTimeTorqueDeceleration;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
