package PhysicsEngine.Wind;


/**
 * The wind arrives from all sides
 * Extends the abstract class
 */
public class LeftAndRightWind extends Wind{
    
    // Choosing a random angle from which the wind will arrive. Using degrees, in the range [0-365]
    final double[] angleBoundaries = {0, 360};

    /**
     *@param maxWindVelocityKmh is the maximum velocity the wind can have in km/h, decreasing getting closer titan
     */
    public LeftAndRightWind(double maxWindVelocityKmh){
        super(maxWindVelocityKmh);
    }

    @Override
    public double[] applyWind(double[] currentProbeVelocity) {
        
        //calculate wind in x and y directions 
        double[] windVelocity = super.calculateWindVelocity(angleBoundaries, 0);// distance from surface is not adapted

        int xDirection = 0;
        int yDirection = 1;

        double[] velocityAfterWind = {currentProbeVelocity[xDirection] - windVelocity[xDirection], currentProbeVelocity[yDirection] - windVelocity[yDirection], currentProbeVelocity[2]};
        return velocityAfterWind;
    }

    @Override
    public double[] applyWind(double[] currentProbeVelocity, double distanceFromSurface) {
        
        //calculate wind in x and y directions 
        double[] windVelocity = super.calculateWindVelocity(angleBoundaries, distanceFromSurface);

        int xDirection = 0;
        int yDirection = 1;

        double[] velocityAfterWind = {currentProbeVelocity[xDirection] - windVelocity[xDirection], currentProbeVelocity[yDirection] - windVelocity[yDirection], currentProbeVelocity[2]};
        return velocityAfterWind;

    }
}
