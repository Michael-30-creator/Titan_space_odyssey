package PhysicsEngine.Simulations;
import java.util.Arrays;

import PhysicsEngine.Functions;
import PhysicsEngine.Solvers.EulerSolver;
import PhysicsEngine.Solvers.RungeKutta4Solver;
import PhysicsEngine.Thrust;
import PhysicsEngine.Controller.FeedbackController;
import PhysicsEngine.Controller.OpenLoopController;
import PhysicsEngine.Controller.iController;
import PhysicsEngine.JourneyPhase.LandingPhase;
import PhysicsEngine.JourneyPhase.TravelPhase;
import PhysicsEngine.JourneyPhase.iJourneyPhase;
import PhysicsEngine.Solvers.iSolver;
import PhysicsEngine.States.State;
import PhysicsEngine.Operations.MatrixOperations;
import PhysicsEngine.Operations.VectorOperations;
import SolarSystem.CelestialBody;
import PhysicsEngine.Wind.*;

/**
 * This class simulates the spaceship's journey to Titan and the landing
 */

public class Simulation {

    final boolean SHOWENDPOSITIONS = false;

    Functions functions = new Functions();
    iSolver solver;
    State state = new State();
    iController controller;

    //controls the timestep and solver used in respective phases
    iJourneyPhase journeyPhase;

    boolean areGivenState = false;
    double[][] givenState = {{-50, 200, Math.PI/2}, {0.1,0,0}};

    double framesTotal = 200;
    int secondsOfTravel = 31536000; //seconds in a year //
    int secondsOfLanding = 500;// 433; //seconds for landing for the Open Loop
    int totalSecondsOfTravel = secondsOfLanding + secondsOfTravel;
    boolean openLoop = true;


    //These are the velocities that have to be changed to modify the probe at the beginning or at the point to go back
    double[] startingVelocity = {49.58306860517117, 38.2950835525803, 1.9666795395409593};

    //Both of these are used to change the phases of the mission
    boolean goIntoOrbit = true;

    boolean inOrbit = false;

    public Simulation(double timeStep, iSolver solver)
    {
        this.journeyPhase = new TravelPhase(timeStep);
        this.solver = solver;
    }

    /**
     * Sets up the celestial bodies in the solar system in their starting positions and updates them 
     */
    public void planetarySetUp() 
    {   
        CelestialBody.setupCelestialBodies();

        int amountOfPositionsStoredTravel = journeyPhase.getAmountOfPositionsStored(secondsOfTravel,journeyPhase.getStepSize());
        int framesPer10Seconds = journeyPhase.getAmountOfFramesNeeded(amountOfPositionsStoredTravel, framesTotal, journeyPhase.getStepSize());
        

        state.setTimedPosition();

        double[][][] nextState = new double[12][2][3];
        
        spacecraftEngine(startingVelocity);

        for(int i = 0 ; i < (amountOfPositionsStoredTravel); i++)
        {   
            nextState = solver.solve(journeyPhase.getStepSize(), state.getState());
            
            state.setState(nextState);

            orbit(); //orbits if distance is below 300

            //this stores the positions of a planet 100 times a year
            if(i % framesPer10Seconds == 0)
            {
                state.setTimedPosition();
            }
        }


        landingSimulation(nextState);

    }

    /**
     * Simulates the landing of the spaceship on Titan
     * @param stateInOrbit the state of the spaceship while in orbit of Titan
     */
    private void landingSimulation(double[][][] stateInOrbit)
    {
        journeyPhase = new LandingPhase();
        int storePositionsEveryXSeconds = 5;

        if(!openLoop){
            EulerSolver eulerSolver = new EulerSolver();
            storePositionsEveryXSeconds = 15;
        }
        RungeKutta4Solver RK4Solver = new RungeKutta4Solver();

        double[] landingSpot = new double[2];
        landingSpot[0] = stateInOrbit[7][0][0];
        landingSpot[1] = stateInOrbit[7][0][1] + CelestialBody.bodyList[7].getRadius();

        //orbit until we are on top of the wanted position of titan
        double[][] initialState = getInitialLandingState(stateInOrbit);

        //Choosing of controller
        controller = new OpenLoopController(landingSpot, initialState[1]);
        //Setting the x and y velocities to 0
        initialState[1][0] = 0;
        initialState[1][1] = 0;

        EulerSolver eulerSolver = new EulerSolver();
        if(!openLoop){
            controller = new FeedbackController(1, landingSpot);
            secondsOfLanding = 1500;
        }

        double[] newUV = new double[2];
        Wind wind = new LeftAndRightWind(10);

        for(int i = 0 ; i < (secondsOfLanding); i++)
        {
            //gets needed U and V dependant on time or the current state of the spaceship
            newUV = controller.getUV(initialState,i);

            // LeftAndRightWind, RightWind, LeftWind
            //applywind to velocities
            //initialState[1] = wind.applyWind(initialState[1]);
            //initialState[1] = wind.applyWind(initialState[1], initialState[0][1] - OpenLoopController.LANDING_POSITION[1]);

            //passes UV into the solver to apply UV on the current state
            if(openLoop){
                initialState = RK4Solver.solve(initialState,newUV[0],newUV[1], journeyPhase.getStepSize());
            } if(!openLoop) {
                initialState = eulerSolver.landSolve(initialState,newUV[0],newUV[1], journeyPhase.getStepSize());
            }
            initialState[0][2] = fullCircle(initialState[0][2]);


            //IMPLEMENT EVERYTHING ABOVE -----------------------------------------
            if(i % storePositionsEveryXSeconds == 0)
            {
                //stores the position, minus by the position of titan to make titan center in the GUI
                state.setLandingPosition(VectorOperations.vectorSubtraction(initialState[0], new double[] {OpenLoopController.LANDING_POSITION[0],OpenLoopController.LANDING_POSITION[1],0}));
            }
            
            
            double[] probePosition = new double[2];
            probePosition[0] = initialState[0][0];
            probePosition[1] = initialState[0][1];

            if(!openLoop){
                if(probePosition[1] - OpenLoopController.LANDING_POSITION[1]  <= 0){
                    break;
                }
            }
            
            if(VectorOperations.euclideanForm(probePosition, OpenLoopController.LANDING_POSITION)>previousDistance)
            {
               // System.out.println("-----------------------------------------" + i);
            }
            previousDistance = VectorOperations.euclideanForm(probePosition, OpenLoopController.LANDING_POSITION);

            probePosition[0] = initialState[1][0];
            probePosition[1] = initialState[1][1];
          // System.out.println(Arrays.toString(probePosition));

            
        }

            double[] probePosition = new double[2];
            probePosition[0] = initialState[0][0];
            probePosition[1] = initialState[0][1];
            
            System.out.println("The final distance is: km" + VectorOperations.euclideanForm(probePosition, OpenLoopController.LANDING_POSITION));
            System.out.println("The final velocities are: " + Arrays.toString(initialState[1]));
    }

    private double previousDistance = Integer.MAX_VALUE;

    /**
     * Gets the initial landing state of the spaceship
     * @param state
     * @return
     */
    private double[][] getInitialLandingState(double[][][] state)
    {
        double[][] newState = new double[2][3];
        newState[0][0] = state[7][0][0];
        newState[0][1] = state[7][0][1] + CelestialBody.bodyList[7].getRadius() + 239.50899;

        newState[1][0] = state[8][1][0];
        newState[1][1] = state[8][1][1];
        newState[0][2] = VectorOperations.calculateAngle(new double[] {newState[1][0],newState[1][1]}, new double[] {10,0});

        if(areGivenState)
        {

        newState[0][0] = state[7][0][0] + givenState[0][0];
        newState[0][1] = state[7][0][1] + CelestialBody.bodyList[7].getRadius() + givenState[0][1];

        newState[1][0] = givenState[1][0];
        newState[1][1] = givenState[1][1];
        newState[0][2] = VectorOperations.calculateAngle(new double[] {newState[1][0],newState[1][1]}, new double[] {10,0});

        }
        return newState;

    }
    
    /**
     * Resets the angle to a 2PI base system (Prevents negative values or values above 2PI)
     * @param currentAngle
     * @return
     */
    public double fullCircle(double currentAngle){
        if (currentAngle < 0){
            currentAngle += 2* Math.PI;
        }
        if(currentAngle > 2*Math.PI){
            currentAngle -= 2* Math.PI;
        }
        return currentAngle;
    }

    /**
     * Calculates the landing position based on a state of the spaceship
     * @param state
     * @return
     */
    private double[] calculateLandingPosition(double[][][] state)
    {
        double[] distanceVector = new double[2];
        distanceVector[0] = state[8][0][0] - state[7][0][0];
        distanceVector[1] = state[8][0][1] - state[7][0][1];
        distanceVector = takeOffRadius(distanceVector);

        double[] landingPosition = new double[2];
        landingPosition[0] = state[8][0][0] - distanceVector[0];
        landingPosition[1] = state[8][0][1] - distanceVector[1];
        return landingPosition;
    }

    /**
     * Calculates the distance to Titan's surface with respect to its radius and centre
     * @param distance 
     * @return
     */
    private double[] takeOffRadius(double[] distance)
    {
        double h = Math.sqrt(Math.pow(distance[0], 2) + Math.pow(distance[1], 2));

        h -= CelestialBody.bodyList[7].radius;


        double a = Math.cos(VectorOperations.calculateAngle(distance, new double[]{10,0})) * h;

        double b = Math.sin(VectorOperations.calculateAngle(distance, new double[]{10,0})) * h;

        double[] distanceToSurface = new double[2];
        distanceToSurface[0] = a;
        distanceToSurface[1] = b;

        return distanceToSurface;
    }

    /*
     * Returns the actual distance from Titan to the probe
     */
    private double getDistaceProbeTitan(){
        return VectorOperations.euclideanForm(state.getState()[8][0],state.getState()[7][0])- CelestialBody.bodyList[7].radius;
    }

    double timeInOrbit = 10200; //seconds, derived from calculations of ORBITAL TIME with respect to Titan's mass and the spaceship's distance
    double startTimeOrbit = 0;

    /**
     * Checks if the spaceship is in orbit and within 300km of Titan, if yes, sets the spaceships velocity to that of being in an orbit
     */
    private void orbit()
    {
        if(getDistaceProbeTitan() < (300 ) && goIntoOrbit == true)
        {
            spacecraftEngine(Functions.getVelocityForOrbit(state.getState()));
            goIntoOrbit = false;
        }

        if(goIntoOrbit == false)
        {
            startTimeOrbit += journeyPhase.getStepSize();
        }  
    }


    /*This method is used for any change in velocity for the probe
     * Calculates how much fuel is consumed and changes the fuelConsumption variable
     * Changes the velocity of the probe in state
     */ 
    public void spacecraftEngine(double[] newVelocity){
        double fuelUsed = Thrust.fuelConsumption(Functions.changeInVelocity(state.getState(), newVelocity));
        state.fuelConsumption += fuelUsed;
        state.setSpaceshipVelocity(newVelocity);
       
    }

    static double closestDistanceToTitan = Double.MAX_VALUE;

    private void checkClosestDistance()
    {
        double distance = getDistaceProbeTitan();

        if(distance < closestDistanceToTitan)
        {
            closestDistanceToTitan = distance;
        }
    }

    public double getClosestDistanceToTitan()
    {
        return closestDistanceToTitan;
    }
}
