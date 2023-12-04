package PhysicsEngine.Optimization;
import PhysicsEngine.Operations.VectorOperations;

import java.util.Random;

/**
 * This class is used to store the trajectory for the genetic algorithm
 */
class Trajectory{

    public double[] velocity; //stores the velocity vector
    double mutationRate; //stores the mutation rate
    public double fitness; //stores the fitness (final distance between the probe and titan)
    
    public Trajectory(double[] parentVelocity, double mutationRate){
        this.mutationRate = mutationRate;
        this.velocity = mutator(parentVelocity);
    }

    /**
    * Mutates the velocity vector given to the instance of the trajectory class
    * @param parentVelocity a velocity vector that was used by its ancestor
    * @return the new velocity vector that the trajectory will use
    */
    public double[] mutator(double[] parentVelocity){

        double[] mutatedVelocity = new double[3];
        Random random = new Random();
        int axis = random.nextInt(3);
        int sign = random.nextInt(2); //this will be used to change the velocity vector in a positive or negative way
        
        boolean mutated = false;
        
        double mutationValue = mutationRate;

        for(int i = 0; i < mutatedVelocity.length; i++){
            mutatedVelocity[i] = parentVelocity[i];
        }
        if (sign == 1){
            mutationValue *= -1;
        }
        
        while(!mutated){
            mutatedVelocity[axis] = parentVelocity[axis] + mutationValue;
            if(VectorOperations.magnitude(mutatedVelocity[0],mutatedVelocity[1],mutatedVelocity[2]) <= 100){
                mutated = true;
            } else {
                mutatedVelocity[axis] = parentVelocity[axis];
                int change = random.nextInt(2);
                if (change == 0){
                    if (axis == 2){
                        axis = 0;
                    } else{
                        axis++;
                    }
                } 
                    else if (change == 1){
                    mutationValue *= -1;
                    }
                }
        }

        return mutatedVelocity; 
    }
}

