package PhysicsEngine.Solvers;

import PhysicsEngine.Functions;
import PhysicsEngine.Operations.MatrixOperations;
import PhysicsEngine.Operations.VectorOperations;
import SolarSystem.CelestialBody;

/**
 * This class implements the 4th order Runge-Kutta method
 */

public class RungeKutta4Solver implements iSolver
{
  EulerSolver euler = new EulerSolver();

  @Override
  public double[][][] solve(double timestep, double[][][] oldState) 
  {
    //aids to understand what is being calculated
    int position = 0;
    int velocity1 = 1;
    int velocity2 = 0;
    int acceleration = 1;

    double[][][] newState = new double[12][2][3];
    
    double[][][] k1 = new double[12][2][3]; // stores velocities and accelerations
    double[][][] k2 = new double[12][2][3];
    double[][][] k3 = new double[12][2][3];
    double[][][] k4 = new double[12][2][3];

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
    //euler step k2 = w(i) + 1/2*k1
      k2[body][velocity2] = VectorOperations.vectorAddition(oldState[body][velocity1], VectorOperations.vectorScalarMultiplication(k1[body][acceleration], 1/2.0));
      k2[body][acceleration] = VectorOperations.vectorScalarDivision(updatedForces[body], CelestialBody.bodyList[body].getMass());

      //k2 * h
      k2[body] = MatrixOperations.matrixScalarMultiplication(k2[body], timestep);

      //storing the new positions of k2
      tempPositions[body] = VectorOperations.vectorAddition(oldState[body][position], k2[body][velocity2]);
    }

    //updating the forces for position w(i) + 1/2*k2
    updatedForces = Functions.forceCalculator(tempPositions);
    for(int body = 0; body < oldState.length; body++)
    {
      //euler step
        k3[body][velocity2] = VectorOperations.vectorAddition(oldState[body][velocity1], VectorOperations.vectorScalarMultiplication(k2[body][acceleration], 1/2.0));
        k3[body][acceleration] = VectorOperations.vectorScalarDivision(updatedForces[body], CelestialBody.bodyList[body].getMass());

        //k3 * h
        k3[body] = MatrixOperations.matrixScalarMultiplication(k3[body], timestep);

        //storing the new positions of k3
        tempPositions[body] = VectorOperations.vectorAddition(oldState[body][position], k3[body][velocity2]);
    }

    //updating the forces for position w(i) + k3
    updatedForces = Functions.forceCalculator(tempPositions);
    for(int body = 0; body < oldState.length; body++)
    {
      //euler step
        k4[body][velocity2] = VectorOperations.vectorAddition(oldState[body][velocity1], k3[body][acceleration]);
        k4[body][acceleration] = VectorOperations.vectorScalarDivision(updatedForces[body], CelestialBody.bodyList[body].getMass());

      //k4 * h
      k4[body] = MatrixOperations.matrixScalarMultiplication(k4[body], timestep);
    }

    for(int body = 0; body < oldState.length; body++)
    {
        // //k2 *2 and k3 *2
        k1[body] = MatrixOperations.matrixScalarMultiplication(k1[body], 1/6.0);
        k2[body] = MatrixOperations.matrixScalarMultiplication(k2[body], 2/6.0);
        k3[body] = MatrixOperations.matrixScalarMultiplication(k3[body], 2/6.0);
        k4[body] = MatrixOperations.matrixScalarMultiplication(k4[body], 1/6.0);
    }


    //w (i+1) = w(i) + (k1 + k2 + k3 + k4) (k2, k3 times 2, all * 1/6 happens above)
    for(int body = 0; body < oldState.length; body++)
    {
        newState[body] = MatrixOperations.matrixAddition(oldState[body],MatrixOperations.matrixAddition(MatrixOperations.matrixAddition(k1[body], k2[body]), MatrixOperations.matrixAddition(k3[body], k4[body])));
    }

    return newState;
  }

/**
 * Solves the equations that are used in the landing process. Since now torque and thrust are included, this needed a different method
 * @param oldState the current state of the spaceship, with oldState[0] being the positions/angle, and oldState[1] the respective velocities
 * @param mainThrust in km/s
 * @param torque in radians/s
 * @param timestep in seconds
 * @return the next state of the spaceship, updating the positions and velocities based on the thrust and torque
 */
  public double[][] solve(double[][] oldState, double mainThrust, double torque, double timestep)
  {
    double[][] newState = new double[2][3];
    double[][] tempState = new double[2][3];

    double[][] k1 = getK(oldState,mainThrust, torque, timestep); 
    tempState = MatrixOperations.matrixAddition(oldState, addKToState(MatrixOperations.matrixScalarMultiplication(k1, 1/2.0), oldState));

    double[][] k2 = getK(tempState,mainThrust, torque, timestep/2.0);
    tempState = MatrixOperations.matrixAddition(tempState, addKToState(MatrixOperations.matrixScalarMultiplication(k2, 1/2.0), oldState));

    double[][] k3 = getK(tempState,mainThrust, torque, timestep/2.0);
    tempState = MatrixOperations.matrixAddition(tempState, addKToState(k3, oldState));

    double[][] k4 = getK(tempState,mainThrust, torque, timestep);

    k2 = MatrixOperations.matrixScalarMultiplication(k2, 2);
    k3 = MatrixOperations.matrixScalarMultiplication(k3, 2);

    double[][] newVelocities = MatrixOperations.matrixAddition(MatrixOperations.matrixAddition(k1, k2), MatrixOperations.matrixAddition(k3, k4));
    newVelocities = MatrixOperations.matrixScalarMultiplication(newVelocities, 1/6.0);

    newState = MatrixOperations.matrixAddition(oldState, newVelocities);
    newState[0][2] = oldState[0][2] + oldState[1][2] + torque;
    return newState;
  }

  final double g = 0.001352;

  /**
   * Calculates the new accelerations based on thrust, gravity and torque
   * @param currentState current state that is modified
   * @param mainThrust in km/s
   * @param torque in radians/s
   * @param timestep given timestep
   * @return the 'derivative' of the current State
   */
  private double[][] getK(double[][] currentState, double mainThrust, double torque, double timestep)
  {
    double[][] kx = new double[2][3];
    kx[0] = currentState[1];
    kx[1][2] = torque;
    kx[1][0] = calculateXAcceleration(mainThrust, kx[1][2] + currentState[0][2]);
    kx[1][1] = calculateYAcceleration(mainThrust, kx[1][2] + currentState[0][2]);
    return kx;
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

    /**
     * Adds the kx state matrix calculated above in the getK method to the current state of the spaceship
     * @param currentState
     * @param kx
     * @return
     */
    private double[][] addKToState(double[][] currentState, double[][] kx)
    {
      return MatrixOperations.matrixAddition(currentState, kx);
    }

}
