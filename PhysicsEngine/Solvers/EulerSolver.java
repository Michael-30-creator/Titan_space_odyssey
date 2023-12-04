package PhysicsEngine.Solvers;
import java.util.Arrays;

import PhysicsEngine.Functions;
import PhysicsEngine.Operations.MatrixOperations;
import PhysicsEngine.States.State;
import PhysicsEngine.Operations.VectorOperations;
import SolarSystem.CelestialBody;


/**
 * This class contains the implementation of Euler's method
 */

public class EulerSolver implements iSolver {
    
    // need initial values for x and y
    public static double[] nextVelocity = new double[3];

    /**
     * calculates the acceleration of a particular celestial body
     * @param mass the mass of the celestial body
     * @param force the force of the celestial body
     * @return a vector array containing the acceleration
     */
    public double[] accelerationCalculation(double mass, double[] force) {
        double[] acceleration = new double[3];
        acceleration = VectorOperations.vectorScalarDivision(force, mass);
        //System.out.println(acceleration);
        return acceleration;
    
    }

    /**
     * calculates the next position of a celestial body
     * @param currentPosition a vector array of the current position of the celestial body
     * @param currentVelocity a vector array of the current velocity of the celestial body
     * @param timestep the timestep used in the vector calculation
     * @return a vector array containing the new position of the body
     */
    public double[] nextPosition(double[] currentPosition, double[] currentVelocity, double timestep) {
        double[] nextPosition = new double[3];
        nextPosition = VectorOperations.vectorAddition(currentPosition, VectorOperations.vectorScalarMultiplication(currentVelocity, timestep));
        currentPosition = nextPosition;
        //System.out.println(currentPosition);
        return currentPosition;
    }

    /**
     * calculates a new velocity of a celestial body
     * @param currentVelocity a vector array of the current velocity of the celestial body
     * @param acceleration a vector array of the current acceleration of the celestial body
     * @param timestep the timestep used for the vector calculation
     * @return a vector array containing the new velocity of the body
     */
    public double[] nextVelocity(double[] currentVelocity, double[] acceleration, double timestep){
        double[] nextVelocity = new double[3];
        nextVelocity = VectorOperations.vectorAddition(currentVelocity, VectorOperations.vectorScalarMultiplication(acceleration, timestep)); 
        currentVelocity = nextVelocity;
        //System.out.println(currentVelocity)
        return currentVelocity; 
    }

    /**
     * an implementation of Euler's solver that calculates a new position of a celestial body
     * @param body a celestial body
     * @param timestep the timestep used in vector calculations
     * @return a vector array containing a new position of the body
     */
    public double[][] solve(CelestialBody body, double timestep){
        double[][] nextState = new double[2][3];
        nextState[1] = accelerationCalculation(body.getMass(), State.getForce(body.rowInState));
        nextState[0] = nextVelocity(State.getVelocity(body.rowInState), nextState[1], timestep);
        System.out.println(Arrays.deepToString(nextState));

        State.setPosition(body.rowInState, nextState[0]);
        State.setVelocity(body.rowInState, nextState[1]);

        return nextState;
    }

    /**
     * A different implementation of Euler's method with an addition parameter
     * @param body a celestial body
     * @param timestep the timestep used in vector calculations
     * @param stateMatrix a 2D array containing the state (position and velocity) of the celestial body
     * @return the next state (position and velocity) of the celestial body
     */
    public double[][] solve(CelestialBody body, double timestep, double[][] stateMatrix)
    {
        double[][] newState = new double[2][3];

        newState[0] = VectorOperations.vectorAddition(stateMatrix[0], VectorOperations.vectorScalarMultiplication(stateMatrix[1], timestep));

        newState[1] = VectorOperations.vectorAddition(stateMatrix[1], VectorOperations.vectorScalarMultiplication(accelerationCalculation(body.getMass(), State.getForce(body.rowInState)), timestep));

        return newState;
    }

    @Override
    public double[][][] solve(double timestep, double[][][] oldState) 
    {
        //aids to understand what is being calculated
        int position = 0;
        int velocity1 = 1;
        int velocity2 = 0;
        int acceleration = 1;
    
        double[][][] newState = new double[12][2][3];
        
    
        //used to store temporary positions for acceleration calculation
        double[][] tempPositions = new double[12][3];
    
        for(int body = 0; body < oldState.length; body++)
        {
          tempPositions[body] = oldState[body][position];
        }
    
        double[][] updatedForces = Functions.forceCalculator(tempPositions);
    
        //k1 = f(ti, wi) or the derivative at that position (velocity and acceleration since we do not have a function)
        for(int body = 0; body < oldState.length; body++)
        {
            newState[body][velocity2] = oldState[body][velocity1];
            newState[body][acceleration] = VectorOperations.vectorScalarDivision(updatedForces[body], CelestialBody.bodyList[body].getMass());

            newState[body] = MatrixOperations.matrixScalarMultiplication(newState[body], timestep);
        }
    
        for(int body = 0; body < oldState.length; body++)
        {
            newState[body] = MatrixOperations.matrixAddition(oldState[body], newState[body]);
        }
        
        return newState;
    }

        /**
     * Calculates the next state of the spaceship during the landing 
     * @param oldState previous state of the spaceship
     * @param mainThrust
     * @param torque
     * @param timestep
     * @return
     */
    public double[][] landSolve(double[][] oldState, double mainThrust, double torque, double timestep){

        double[][] newState = new double[2][3];

        newState[0] = VectorOperations.vectorAddition(oldState[0], VectorOperations.vectorScalarMultiplication(oldState[1], timestep));

        double[] velocityAddition = new double[3];
        velocityAddition[2] = torque;
        velocityAddition[0] = calculateXAcceleration(mainThrust, velocityAddition[2] + newState[0][2]);
        velocityAddition[1] = calculateYAcceleration(mainThrust, velocityAddition[2] + newState[0][2]);

        newState[1] = VectorOperations.vectorAddition(oldState[1], VectorOperations.vectorScalarMultiplication(velocityAddition, timestep));

        return newState;
    }

    /**
     * Calculates the x acceleration relating to the formula x'' = u * sin(theta)
     * @param u
     * @param theta
     * @return
     */
    private double calculateXAcceleration(double u, double theta)
    {   

        return u * Math.sin(theta);
        
    }

    /**
     * Calculates the y acceleration relating to the formula y'' = u * cos(theta) - g
     * @param u
     * @param theta
     * @return
     */
    private double calculateYAcceleration(double u, double theta)
    {
        return u * Math.cos(theta) - g;
    }

    final double g = 0.001352;
}


