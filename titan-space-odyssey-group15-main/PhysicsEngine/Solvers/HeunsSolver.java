package PhysicsEngine.Solvers;
import PhysicsEngine.Functions;
import PhysicsEngine.Operations.MatrixOperations;
import PhysicsEngine.Operations.VectorOperations;
import SolarSystem.*;


/**
 * This class implement's Heun's method
 */

public class HeunsSolver implements iSolver {

    @Override
    public double[][][] solve(double timestep, double[][][] oldState) {
            
        //aids to understand what is being calculated
        int position = 0;
        int velocity1 = 1;
        int velocity2 = 0;
        int acceleration = 1;

        double[][][] newState = new double[12][2][3];
        
        double[][][] k1 = new double[12][2][3]; // stores velocities and accelerations
        double[][][] k2 = new double[12][2][3];
        double[][][] k3 = new double[12][2][3];


        //used to store temporary positions for acceleration calculation
        double[][] tempPositions = new double[12][3];

        for(int body = 0; body < oldState.length; body++)
        {
        tempPositions[body] = oldState[body][position];
        }

        // calculating the force on all the bodies
        double[][] updatedForces = Functions.forceCalculator(tempPositions);

        //k1 = f(ti, wi) or the derivative at that position (velocity and acceleration since we do not have a function)
        for(int body = 0; body < oldState.length; body++)
        {
            k1[body][velocity2] = oldState[body][velocity1];
            k1[body][acceleration] = VectorOperations.vectorScalarDivision(updatedForces[body], CelestialBody.bodyList[body].getMass());
        }

        //k1 = k1 * h     &&    storing the new positions at w(i) + h*k1
        for(int body = 0; body < oldState.length; body++)
        {
            k1[body] = MatrixOperations.matrixScalarMultiplication(k1[body], timestep);
            tempPositions[body] = VectorOperations.vectorAddition(oldState[body][position], k1[body][velocity2]);
        }

        //updating the forces for position w(i) + h*k1
        updatedForces = Functions.forceCalculator(tempPositions);
        for(int body = 0; body < oldState.length; body++)
        {
            //euler step k2 = w(i) + 1/3*k1
            k2[body][velocity2] = VectorOperations.vectorAddition(oldState[body][velocity1], VectorOperations.vectorScalarMultiplication(k1[body][acceleration], 1/3.0));
            k2[body][acceleration] = VectorOperations.vectorScalarDivision(updatedForces[body], CelestialBody.bodyList[body].getMass());

            //k2 * h
            k2[body] = MatrixOperations.matrixScalarMultiplication(k2[body], timestep);

            //storing the new positions of k2
            tempPositions[body] = VectorOperations.vectorAddition(oldState[body][position], k2[body][velocity2]);
        }
        
        //updating the forces for position w(i) + 1/3*k2
        updatedForces = Functions.forceCalculator(tempPositions);
        for(int body = 0; body < oldState.length; body++)
        {
            //euler step k3 = 2/3*k2
            k3[body][velocity2] = VectorOperations.vectorAddition(oldState[body][velocity1], VectorOperations.vectorScalarMultiplication(k2[body][acceleration], 2/3.0));
            k3[body][acceleration] = VectorOperations.vectorScalarDivision(updatedForces[body], CelestialBody.bodyList[body].getMass());

            //k3 * h
            k3[body] = MatrixOperations.matrixScalarMultiplication(k3[body], timestep);

            //storing the new positions of k3
            tempPositions[body] = VectorOperations.vectorAddition(oldState[body][position], k3[body][velocity2]);
        }

        for(int body = 0; body < oldState.length; body++)
        {
            // //k3 *3
            k1[body] = MatrixOperations.matrixScalarMultiplication(k1[body], 1/4.0);
            k3[body] = MatrixOperations.matrixScalarMultiplication(k3[body], 3/4.0);
        }

        //w (i+1) = w(i) + (k1 + k3) (k1 *1/4 and k3* 3/4 happen above)
        for(int body = 0; body < oldState.length; body++)
        {
            newState[body] = MatrixOperations.matrixAddition(oldState[body],MatrixOperations.matrixAddition(k1[body], k3[body]));
        }

        return newState;
  }
}
