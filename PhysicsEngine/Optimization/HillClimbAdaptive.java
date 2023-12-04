package PhysicsEngine.Optimization;

import PhysicsEngine.Simulations.SimulationOptimization;
import PhysicsEngine.Operations.VectorOperations;

import java.util.Arrays;



public class HillClimbAdaptive{
    
    private int timestep=0;
    private int n = 0;
    private double radiousReached = 0;
    private int count = 0; //counting the steps
    private double maxMagnitude = 0; // Maximum magnitude of the initial velocity vector km/s


    public static void main(String[] args) {
        HillClimbAdaptive run = new HillClimbAdaptive();
    }

    //setup the parameters
    public HillClimbAdaptive(){

        this.n = 4;// number of neigbour vectors
        this.radiousReached = 0.000000000000000001; // minimum dimension of the radious, when reached this value the algorithm stop
        this.timestep = 100;// timestep
        this.maxMagnitude = 100.0;
        double[] initialVector = {49.58313440693111, 38.29506290304066, 1.9666588900013093};//state initial vector(described by the velocities for x,y,z)(magnitude <= 60)
        double r = 2; //initial radious of the circle(search space)

        run(r, initialVector);
    }

    /*
     * Runs the algorithm
     * Whether better inital velocities are found among the neighbours, recall itself updating @param InitialVector
     * Otherwise reduce the search space (the radious @param r) and recall itself
     * Stops when @param r == radiousReached
     */
    private void run( Double r,double[] initialVector){

        if(r <= radiousReached){
            return;
        }
        System.out.println(count++);
        System.out.println("Current Velocity:" + Arrays.toString(initialVector));

        //run the simulation for the initial vector and gets the distance
        double InitialVectorDistance = getDistanceAfterSimulation(initialVector);

        //find the neighbourVectors
        double[][] neighboursVectors = new double[n][3];// n Vectors defined by three velocities
        neighboursVectors = findNeighbours(r, initialVector);// returns double[n][3]
        
        //run the simualation for each neighbour and finds the Best one. in [0] is stored number among n of the vector and [1] is the distance p-t
        double[] bestNeighbourVector = getFittestVector(neighboursVectors);

        
        //State if the intial vector was better or there is a better neighbour (the fitness depends on the final distance)
        if(InitialVectorDistance > bestNeighbourVector[1]){
            initialVector = neighboursVectors[(int)bestNeighbourVector[0]];// the most fittest neighbour become the initial vector, the algorithm is rerunned
            //r = (r*7)/6;//increases the search space in order to higher the chance of get a global maxima
            System.out.println("New Velocity");
        }
        else{
            r = (5*r)/6;// else the radious is reduced(the scale is random, use the one you want)
            System.out.println("Radious reduced:" + r);
        }
        //n = (int)(10000 * r);
        run(r, initialVector);
            
    }


    /**
     * Calculates the distance between the probe and Titan after running a simulation with the given velocities
     * @param velocities defines the initial velocity the probe is lauched with
     * @return The distance between the probe and Titan after the simulation has run
     */
    private double getDistanceAfterSimulation(double[] velocities){
        SimulationOptimization simulation = new SimulationOptimization(timestep);
        double[][] positionsAfterSimulation = simulation.planetarySetUp(velocities[0], velocities[1], velocities[2]);

        //Print the distance after the first simulation
        return VectorOperations.euclideanForm(positionsAfterSimulation[0],positionsAfterSimulation[1]);
    }


    /**
     * Finds the fittest vector among the given vectors based on the distance after running simulations
     * @param vectors The array of vectors
     * @return double[2] where [0] is the 0 <= number < n of the best neighbour(with lower final distance) 
       and [0] the distance probe-titan after running the simulation with its velocities
     */
    private double[] getFittestVector(double[][] vectors){

        double[] distancesAfterSimulation =  new double[n];

        //collects for each neighbour the probe-titan distance after run a simulation with its values(velcity)
        for(int i=0; i < n; i++){
            distancesAfterSimulation[i] = getDistanceAfterSimulation(vectors[i]);
        }

        //find the number n of neighbour with the lower distance 
        int vectorLowerDistance = 0;
        for(int i=1; i < n; i++){
            if(distancesAfterSimulation[i]<distancesAfterSimulation[vectorLowerDistance])
                vectorLowerDistance = i;
        }

        return new double[]{(double)vectorLowerDistance, distancesAfterSimulation[vectorLowerDistance] };// [0] is the number n of the neighbour and [1] its distance
    }

   
    /**
     * The vectors are derived by positioning in a even way n points on a circle in the 3d space 
     * of radius @param r and centered in the point p from where the probe take off.
     * @param r the radius 
     * @param initialVector the initial vector containing positions
     * @return double[n][3], each neighbour vector n is defined within its velocities(x,y,z)
     */
    private double[][] findNeighbours(double r, double[] initialVector) {
        double[][] neighboursVectors = new double[n][3];
        double angleIncrement = 2 * Math.PI / (double) n;
        
    
        for (int i = 0; i < n; i++) {
            double angle = i * angleIncrement;
            double x = initialVector[0] + r * Math.cos(angle);
            double y = initialVector[1] + r * Math.sin(angle);
            double z = initialVector[2] + r * Math.sin(angle); // Update z-coordinate calculation
    
            // Check magnitude and scale the vector if necessary
            double magnitude = Math.sqrt(x * x + y * y + z * z);
            if (magnitude > maxMagnitude) {
                double scaleFactor = maxMagnitude / magnitude;
                x *= scaleFactor;
                y *= scaleFactor;
                z *= scaleFactor;
            }
    
            neighboursVectors[i] = new double[]{x, y, z};
        }
    
        return neighboursVectors;
    }

}
