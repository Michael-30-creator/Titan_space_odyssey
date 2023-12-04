package PhysicsEngine.Optimization;
import PhysicsEngine.Simulations.SimulationOptimization;
import PhysicsEngine.Operations.VectorOperations;

import java.util.Arrays;


public class geneticAlgorithm {
    
    private int populationSize; //amount of children in a generation
    private double mutationRate; //the amount at which the velocity vector will be changed in a generation. It dictates by how much the velocity vector will be changed each time
    private int numGenerations; //the amount of generation 
    public Trajectory population; //the population of trajectories
    public final double finalmutationRate = 0.0001; //the fitness rate that will be used for the last generation. Used to calculate by how much the mutation rate will be changed each iteration.
    public final int numberOfBest = 5; //changes how many indivuals are passed on to the next generation CHECK THAT POP SIZE IS DIVISIBLE BY THIS
    public double mutationRateChange;   
    public static void main(String[] args) {

        geneticAlgorithm test = new geneticAlgorithm(30, 0.5, 100); //POP SIZE HAS TO BE DIVISIBLE BY NUMBEROFBEST
        System.out.println(test.evolution());
    }


    public geneticAlgorithm(int populationSize, double initialMutationRate, int numGenerations){
        this.populationSize = populationSize; //decides how many individuals will be created per generator
        this.mutationRate = initialMutationRate; //the initial mutation rate that will be used
        this.numGenerations = numGenerations; //the cap how many generations will be created
        this.mutationRateChange = (mutationRate-finalmutationRate)/numGenerations;
            
    }
    
    //modifies the mutation rate of the genetic algorithm
    //It modifies it in a way so that the last generation that's done would have a mutation rate of 0.05 (velocity)
    public void mutationRateModifier(){
        mutationRate = mutationRate - mutationRateChange;  
    }

    /**
     * Runs the simulation using a specific velocity vector for the probe
     * @param velocity the velocity vector that will be used for the probe in the simulation
     * @return the distance between the probe and titan at the end of the simulation
     */
    public double runTest(double[] velocity){
        SimulationOptimization simulation = new SimulationOptimization(50);
        System.out.println("Initial probe velocity");
        System.out.println(Arrays.toString(velocity));
        double[][] positions = simulation.planetarySetUp(velocity[0], velocity[1], velocity[2]);
        double[] probePosition = positions[0];
        double[] titanPosition = positions[1];
        return getFitness(probePosition, titanPosition);
    }

    /**
     * Calculates distance between the probe and titan
     * @param titanPosition final coordinates of titan
     * @param probePosition final coordinates of the probe
     * @return the eucludiean distance between both points
     */
    public double getFitness(double[] probePosition, double[] titanPosition){
        return VectorOperations.euclideanForm(probePosition, titanPosition);
    }

    /**
     * A method to initialize the population in a generation
     * @param parentTrajectories an array of the best trajectories in the last generation. Uses the trajectory type class
     * @return an array of elementes of type trajectory
     */
    public Trajectory[] createPopulation(Trajectory[] parentTrajectories){
        Trajectory[] population = new Trajectory[populationSize];
        int arrayCounter = 0; //keeping track of the position in the population array where the child has to be added
        for (int i = 0; i < numberOfBest; i++){
            for(int j=0; j < populationSize/numberOfBest; j++){
                population[arrayCounter] = new Trajectory(parentTrajectories[i].velocity, mutationRate);
                population[arrayCounter].fitness = runTest(population[arrayCounter].velocity);
                arrayCounter++;
            }
            
        }
        return population;
    }
    
    /**
     * Find the best 5 trajectories in the population
     * @param population an array of all the indivuals from that generation. 
     * @return a vector array containing a new position of the body
     */
    public Trajectory[] findBest(Trajectory[] population){
        Trajectory[] best = Arrays.copyOfRange(population, 0, numberOfBest);
        for (Trajectory element : population){
            for(int i = 0; i < best.length; i++){
                if(element.fitness < best[i].fitness){
                    best[i] = element;
                    break;
                }
                
            }
        }
        
        //runs the hillclimb algorithm on the best one of the best one in the generation
        double[] hillClimbResults = runHillClimb(best[0].velocity, best[0].fitness);
        best[0].fitness = hillClimbResults[3];
        best[0].velocity = Arrays.copyOfRange(hillClimbResults, 0, 3);         
        return best;
    }

    /**
     * The method to run the genetic algorithm
     * @return the best trajectories
     */
    public Trajectory[] evolution(){
        
        //stores the initial velocity vector
        //Best velocity to titan:
        double[] startingVelocity = {-45.82267566768859, 12.505183285117178, 0.1537477890858314};
        //creates an array to populate the population
        Trajectory[] initialTrajectoryArray = new Trajectory[5];
        

        for(int i = 0; i < numberOfBest; i++){ //creates an array copying the starting velocity so the creatPopulation method works
            initialTrajectoryArray[i] = new Trajectory(startingVelocity, mutationRate);
            initialTrajectoryArray[i].fitness = runTest(initialTrajectoryArray[i].velocity);
        }

       

        //runs the genetic algorithm until it reaches the generation cap
        Trajectory[] population = createPopulation(initialTrajectoryArray);
        for(int i = 0; i < numGenerations; i++){
            population = createPopulation(findBest(population));
            printVelocities(findBest(population)); 
            printFitnesses(findBest(population));
            mutationRateModifier();
        }
        
        return findBest(population);
    }

    /**
     * Runs the hill climbing algorithm on a specificed trajectory
     * @param velocity array from an individual
     * @param the fitness of an individual
     * @return the new best velocity vector and the distance all in one array
     */
    public double[] runHillClimb(double[] velocities, double currentDistance){
        HillClimbAllAxes climbAll = new HillClimbAllAxes(velocities, currentDistance, 8, 0.1, 7);
        double[] returnValues = new double[4]; //First three are velocity, last one is distance 
        for(int i = 0; i < 2; i++) {
                currentDistance = climbAll.RunAllAxesOnce(velocities, currentDistance);
                System.out.println(Arrays.toString(velocities));
                System.out.println("Current distance is:" + currentDistance);
                returnValues[0] = velocities[0];
                returnValues[1] = velocities[1];
                returnValues[2] = velocities[2];
                returnValues[3] = currentDistance;
        }
        return returnValues;
    }
    /**
     * prints a velocity array
     * @param individuals an array of type trajectory
     */
    public void printVelocities(Trajectory[] individuals){
        for(Trajectory individual : individuals){
            System.out.println(Arrays.toString(individual.velocity));
        }
    }

     /**
     * prints a fitness array
     * @param individuals an array of type trajectory
     */
    public void printFitnesses(Trajectory[] individuals){
        for(Trajectory individual : individuals){
            System.out.println(individual.fitness);
        }
    }
}

