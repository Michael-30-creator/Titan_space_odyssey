package PhysicsEngine;

import PhysicsEngine.Operations.VectorOperations;

public class Thrust {

    
    /**
     * Calculates the impulse in the timestep
     * @param forceVector a force vector
     * @param timestep the timestep being used
     * @return the impulse vector
     */
    public static double[] impulse(double[] forceVector, double timestep){
        //does a check if the total force is bigger or not than the max force possible 
        if (VectorOperations.magnitude(forceVector[0], forceVector[1], forceVector[2]) > (3*10^7)){
            System.out.println("Error: Exceeding maximal Force.");
            return null;
        }
        
        double[] impulse = new double[3];
        impulse[0] = (forceVector[0] * timestep)/50000;
        impulse[1] = (forceVector[1] * timestep)/50000;
        impulse[2] = (forceVector[2] * timestep)/50000;

        return impulse; //returns how much velocity has to be added 
    }


    /**
     * Calculates the fuel consumption based on the formula ||I||*1ms^-1
     * @param impulse impulse vector
     * @return the fuel consumption as a double
     */
    public static double fuelConsumption(double[] impulse){
        double magnitude = VectorOperations.magnitude(impulse[0], impulse[1], impulse[2]);
        return magnitude*1000; //return is the amount of kg of fuel used
    }


}
