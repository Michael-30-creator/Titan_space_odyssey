package PhysicsEngine.JourneyPhase;

import PhysicsEngine.Solvers.RungeKutta4Solver;
import PhysicsEngine.Solvers.iSolver;

/**
 * This class represents the travel of the spaceship to Titan from Earth
 * implements the iJourneyPhase interface
 */

public class TravelPhase implements iJourneyPhase{

    private double timestep;
    private iSolver solver = new RungeKutta4Solver();

    public TravelPhase (double timeStep)
    {
        this.timestep = timeStep;
    }

    @Override
    public double getStepSize() {
        return timestep;
    }

    @Override
    public iSolver getSolver() {
        return solver;
    }

    @Override
    public int getAmountOfPositionsStored(double seconds, double stepSize) {
        return (int) Math.ceil(seconds / stepSize);
    }

    @Override
    public int getAmountOfFramesNeeded(double amountOfPositionsStored, double framesTotal, double stepSize) {
        return (int) Math.ceil(amountOfPositionsStored / (framesTotal/2.0) + ((amountOfPositionsStored/stepSize)%100));
    }

}
