package PhysicsEngine.Wind;

/**
 * The wind arriving from the right
 * Extends Wind abstract class
 */
public class RightWind extends Wind {

    // Choosing an angle such that cos(angle) is positive. Using degrees, in the range [270-360], [0-90]
    final double[] angleBoundaries = {270, 360, 0, 90};

    /**
     *@param maxWindVelocityKmh the maximum velocity the wind can have in km/h, decreasing getting closer titan
     */
    public RightWind(double maxWindVelocityKmh){
        super(maxWindVelocityKmh);
    }

    @Override
    public double[] applyWind(double[] currentProbeVelocity, double currentDistanceFromSurface) { 
        setDirection(false);
        //calculate the X and Y 
        double[] windVelocity = super.calculateWindVelocity(angleBoundaries, currentDistanceFromSurface);
        
        int xDirection = 0;
        int yDirection = 1;

        double[] velocityAfterWind = {(currentProbeVelocity[xDirection] - windVelocity[xDirection]), (currentProbeVelocity[yDirection] - windVelocity[yDirection]), currentProbeVelocity[2]};
        return velocityAfterWind;
    }

    @Override
    public double[] applyWind(double[] currentProbeVelocity) { 
        setDirection(false);
        //calculate the X and Y 
        double[] windVelocity = super.calculateWindVelocity(angleBoundaries, 0);

        int xDirection = 0;
        int yDirection = 1;

        double[] velocityAfterWind = {currentProbeVelocity[xDirection] - windVelocity[xDirection], currentProbeVelocity[yDirection] - windVelocity[yDirection], currentProbeVelocity[2]};
        return velocityAfterWind;
    }
}
