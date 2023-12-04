package PhysicsEngine.Simulations;

import PhysicsEngine.Functions;
import PhysicsEngine.Solvers.RungeKutta4Solver;
import PhysicsEngine.Solvers.iSolver;
import PhysicsEngine.States.State2;
import PhysicsEngine.Operations.VectorOperations;
import SolarSystem.CelestialBody;


/* This class is used for the hillClimb and other optimization algorithms to be able to run a simulation of the solar system
 * It is a modified but mostly copied version of the Simulation class
 */
public class SimulationOptimization {

    final boolean SHOWENDPOSITIONS = true;

    Functions functions = new Functions();
    iSolver solver = new RungeKutta4Solver();
    State2 state = new State2();

    double timeStep; // in seconds
    int lengthOfSimulation = 31536000; //seconds of one year // if the simulation is runned in 333536000, the probe visually touches titan

    public SimulationOptimization(double timeStep)
    {
        this.timeStep = timeStep;
    }

    public double[][] planetarySetUp(double valuex, double valuey, double valuez) 
    {   
        state = new State2();
        CelestialBody.setupCelestialBodies();

        int timesPerSimulation = (int) Math.ceil(lengthOfSimulation / timeStep);

        state.state[8][1][0] = valuex;
        state.state[8][1][1] = valuey;
        state.state[8][1][2] = valuez;


        double[][][] nextState = new double[12][2][3];

        for(int i = 0 ; i < (timesPerSimulation); i++)
        {   
            nextState = solver.solve(timeStep, state.getState());
            
            state.setState(nextState);

    
        }

        double[] probePosition = state.getState()[8][0];
        double[] titanPosition = state.getState()[2][0]; //NAME SAYS TITAN BUT THIS IS NOW REFERRING TO EARTH
        double[][] twopositions = new double[2][3];
        twopositions[0] = probePosition;
        twopositions[1] = titanPosition;

        if(!SHOWENDPOSITIONS)
        {
            System.out.println("distance:");
            System.out.println(getDistaceProbeTitan());
        }

        return twopositions;

    }

    /*
     * returns the actual distance from Titan to the probe
     */
    private double getDistaceProbeTitan(){
        return VectorOperations.euclideanForm(state.getState()[8][0],state.getState()[2][0]);
    }

    

}