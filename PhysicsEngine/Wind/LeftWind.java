package PhysicsEngine.Wind;


/**
 * Wind arriving from the left
 * Extends Wind abstract class
 */
public class LeftWind extends Wind {

    // Choosing an angle such that cos(angle) is negative. Using degrees, in the range [90-270]
    private final double[] angleBoundaries= {90, 270};

    /**
     *@param maxWindVelocityKmh is the maximum velocity the wind can have in km/h, decreasing getting closer titan
     */
    public LeftWind(double maxWindVelocityKmh){
        super(maxWindVelocityKmh);
    }

    @Override
    public double[] applyWind(double[] currentProbeVelocity) {
        setDirection(true);
        //calculate the X and Y 
        double[] windVelocity = super.calculateWindVelocity(angleBoundaries, 0); // passing 0 since the distance is not adapted

        int xDirection = 0;
        int yDirection = 1;

        double[] velocityAfterWind = {currentProbeVelocity[xDirection] - windVelocity[xDirection], currentProbeVelocity[yDirection] - windVelocity[yDirection], currentProbeVelocity[2]};
        return velocityAfterWind;
    }

    @Override
    public double[] applyWind(double[] currentProbeVelocity, double distanceFromSurface) {
        setDirection(true);
        //calculate the X and Y 
        double[] windVelocity = super.calculateWindVelocity(angleBoundaries, distanceFromSurface);

        int xDirection = 0;
        int yDirection = 1;

        double[] velocityAfterWind = {currentProbeVelocity[xDirection] - windVelocity[xDirection], currentProbeVelocity[yDirection] - windVelocity[yDirection], currentProbeVelocity[2]};
        return velocityAfterWind;
    }

}
