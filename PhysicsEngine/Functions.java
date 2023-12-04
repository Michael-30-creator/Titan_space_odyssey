package PhysicsEngine;

import PhysicsEngine.Operations.VectorOperations;
import PhysicsEngine.States.State;
import SolarSystem.CelestialBody;

/**
 * This class contains all numerical methods that are used for computations.
 */
public class Functions {

    static double G = 6.6743 * Math.pow(10, -20);

    CelestialBody listOfCelestialBodies[] = CelestialBody.bodyList;

    int rowSpaceship = 8;
    
    /**
     * calculates the force of one celestial body on another celestial body
     * @param bodyA
     * @param bodyB
     * @return Force of bodies in three dimensions
     */
    public double[] forceCalculator(CelestialBody bodyA, CelestialBody bodyB){

        double[] vectorA = State.getPosition(bodyA.rowInState).clone();
        double[] vectorB = State.getPosition(bodyB.rowInState).clone();

        double a = G * bodyA.mass * bodyB.mass;

        double[] b = VectorOperations.vectorScalarDivision(VectorOperations.vectorSubtraction(vectorA, vectorB), Math.pow(VectorOperations.euclideanForm(vectorA, vectorB), 3));

        double[] force = VectorOperations.vectorScalarMultiplication(b, a);

        return force;
    }

    /**
     * calculates the new forces based on a given state
     * @param state the current state of a celestial body
     * @return forces with respect to the new state
     */
    public static double[][] forceCalculator(double[][] state)
    {
        double[][] forces = new double[12][3];

        for(int bodyA = 0; bodyA < state.length; bodyA++)
        {
            double[] vectorA = state[bodyA];

            for(int bodyB = 0; bodyB < state.length; bodyB++)
            {
                if(bodyB != 8  && bodyA != bodyB)
                {
                    double[] vectorB = state[bodyB];

                    double a = G * CelestialBody.bodyList[bodyA].getMass() * CelestialBody.bodyList[bodyB].getMass();
    
                    double[] b = VectorOperations.vectorScalarDivision(VectorOperations.vectorSubtraction(vectorA, vectorB), Math.pow(VectorOperations.euclideanForm(vectorA, vectorB), 3));
    
                    forces[bodyA] = VectorOperations.vectorAddition(forces[bodyA],VectorOperations.vectorScalarMultiplication(b, a));
                }
                
            }
        }

        for(int bodyA = 0; bodyA < state.length; bodyA++)
        {
            forces[bodyA] = VectorOperations.vectorScalarMultiplication(forces[bodyA], -1);
        }

        return forces;
    }

    /**
     * calculates the sum of the forces the celestial bodies have on the space ship
     * @param spaceship the spaceship celestial bodu
     * @return sum of all forces on the spaceship
     */
    public double[] forceOnSpaceship(CelestialBody spaceship){
        
        double[] fullForce = new double[3];

        for(int i = 0; i < 12; i++) 
        {
            if(i != rowSpaceship)//leaves out spaceship
            {
                fullForce = VectorOperations.vectorAddition(fullForce, forceCalculator(listOfCelestialBodies[spaceship.rowInState], listOfCelestialBodies[i]));
            }
        }

        return VectorOperations.vectorScalarMultiplication(fullForce, -1);
    }

    /**
     * calculates the forces of other bodies on a body A, ignoring the spaceship
     * @param bodyA a celestial body
     * @return a force vector representing the force of other celestial bodies on bodyA
     */
    public double[] forceOnPlanet(CelestialBody bodyA)
    {
        double[] fullForce = new double[3];

        for(int i = 0; i < 12; i++)
        {
            if(i != bodyA.rowInState && i != rowSpaceship) //leaves out spaceship
            {
                fullForce = VectorOperations.vectorAddition(fullForce, forceCalculator(listOfCelestialBodies[bodyA.rowInState], listOfCelestialBodies[i]));
            }
            
        }

        return VectorOperations.vectorScalarMultiplication(fullForce, -1);
    }

    /**
     * calculates the velocity of a planet with respect to time
     * @param time the timestep used in vector calculations
     * @param body a celestial body
     * @return the next velocity of a celestial body
     */
    public double[] velocityOfBody(double time, CelestialBody body)
    {
        double[] velocity = new double[3];

        //velocity1 = velocity0 + (Force/mass) * time

        velocity = VectorOperations.vectorScalarDivision(State.getForce(body.rowInState), body.mass);
        velocity = VectorOperations.vectorScalarMultiplication(velocity, time);
        velocity = VectorOperations.vectorAddition(velocity, State.getVelocity(body.rowInState));

        return velocity;
    }

    /** 
     * calculates the new position of a body
     * @param time timestep used in vector calculations
     * @param body a Celestial body 
     * @return the next position of the celestial body 
     */
    public double[] newPositionOfBody(double time, CelestialBody body)
    {
        double[] position = new double[3];

        position = VectorOperations.vectorScalarMultiplication(State.getVelocity(body.rowInState), time);
        position = VectorOperations.vectorAddition(State.getPosition(body.rowInState), position);
        
        return position;
    }

    /**
     * calculates a velocity to bring the spacecraft into orbit
     * @param state current state of the planets
     * @return a velocity that brings the spacecraft into orbit, balance between veloticy and gravitational force of titan
     */
    public static double[] getVelocityForOrbit(double[][][] state)
    {
        double[][] tempPositions = new double[12][3];

        for(int body = 0; body < state.length; body++)
        {
          tempPositions[body] = state[body][0];
        }

        //Gravitiational Force between spacecraft and Titan divided by spacecraft's mass
        
        double [] A = VectorOperations.vectorScalarDivision(getForceBetweenTwoBodies(state[8][0], state[7][0], CelestialBody.bodyList[8], CelestialBody.bodyList[7]),CelestialBody.bodyList[8].getMass());
        return A;
    }

    /**
     * calculates the amount of velocity changed for the spacecraft in each axis (used for fuel consumption)
     * @param state current state of the planets
     * @param newVelocity the new velocity of the spacecraft
     * @return the absolute difference between both velocity vectors
     */
    public static double[] changeInVelocity(double[][][] state, double[] newVelocity){

        double[] velocityChange = new double[3];

        velocityChange[0] = Math.abs(state[8][1][0]) - Math.abs(newVelocity[0]);
        velocityChange[1] = Math.abs(state[8][1][1]) - Math.abs(newVelocity[1]);
        velocityChange[2] = Math.abs(state[8][1][2]) - Math.abs(newVelocity[2]);

        return velocityChange;
    }
    /**
     * Calculates the force between 2 celestial bodies, given their current position
     * @param positionA position of Celestial body A
     * @param positionB position of Celesitial body B
     * @param bodyA Celestial body A
     * @param bodyB Celestial body B
     * @return a force vector representing the force between the 2 bodies
     */
    private static double[] getForceBetweenTwoBodies(double[] positionA, double[] positionB, CelestialBody bodyA, CelestialBody bodyB)
    {
        double a = G * bodyA.mass * bodyB.mass;

        double[] b = VectorOperations.vectorScalarDivision(VectorOperations.vectorSubtraction(positionA, positionB), Math.pow(VectorOperations.euclideanForm(positionA, positionB), 3));

        return VectorOperations.vectorScalarMultiplication(b, a);
    }

    /**
     * Quadratic Equation solver
     * ax^2 + bx + c = 0
     * x = (-b + sqrt(b^2 -4ac))/2a
     * @param a 
     * @param b
     * @param c
     * @return solved equation for x
     */
    static public double quadraticEquation(double a, double b, double c){
        double discriminant = Math.sqrt(Math.abs((b*b) - (4*a*c)));
        double x = (-b - discriminant)/ (2*a);
        return x;
    }
}
