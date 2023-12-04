package PhysicsEngine.Solvers;


public interface iSolver {

    /**
     * A general method that calculates the next state of a celestial body 
     * @param timestep the timestep used in vector calculations
     * @param oldState the previous state of the celestial body
     * @return the next state (position and velocity) of the celestial body
     */
    public double[][][] solve(double timestep, double[][][] oldState);
}
