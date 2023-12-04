package PhysicsEngine.Controller;

public interface iController {
    /**
     * Calculates the torque and acceleration (main thrust) scheduled in a second
     * @param state the current position, velocity and angle of the spaceship 
     * @param time time in seconds
     * @return the main thrust and torque at that point in time
     */
    public double[] getUV(double[][] state, int time);
}
