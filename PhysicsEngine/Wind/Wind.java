
package PhysicsEngine.Wind;
import java.lang.Math;
import java.util.Random;

/**
 * An abstract class representing the wind in Titan's atmosphere
 * Create an instance of Wind using LeftWind, RightWind or LeftAndRightWind classes.
 * Call the constructor with the preferred maxWindVelocity in km/h value.
 * Call the method applyWind (i.e. currentVelocity = instanceName.applyWind(currentVelocity); )
 * Calling applyWind with the parameters (currentVelocity, distanceProbeTitan), maxWindVelocity is adapted to the atmosphere layer
*/
public abstract class Wind {

    static boolean Direction = true;
    protected final double maxWindVelocity;
    
    /**
     * @param maxWindVelocityKmh the maximum velocity the wind can have in km/h, decreasing getting closer titan
     */
    protected Wind(double maxWindVelocityKmh){
        this.maxWindVelocity = maxWindVelocityKmh;// transform km/h in km/s
    }

    /**
     * @param currentProbeVelocity the velocity that will be applied to the wind
     * @return new probe velocity after the wind effect, i.e. currentVelocity = applyWind(currentVelocity)
     */
    public abstract double[] applyWind(double[] currentProbeVelocity);


    /**
     * Adapt the the wind to the probe's distance away from Titan's surface (closer to Titan = less wind)
     * @param currentProbeVelocity the velocity that will be applied to the wind
     * @param distanceFromSurface the distance from Titan's surface
     * @return new probe velocity after the wind effect, i.e. currentVelocity = applyWind(currentVelocity)
     */
    public abstract double[] applyWind(double[] currentProbeVelocity, double distanceFromSurface);


    /**
     * Calculates the wind velocity depending on the angle boundaries and the distance from Titan's surface
     * @param angleBoundaries an array of angle boundaries in degrees 
     * @param distanceFromSurface the distance from Titan's surface
     * @return an array with x and y wind velocity (y wind velocity = 0)
     */
    public double[] calculateWindVelocity(double[] angleBoundaries, double distanceFromSurface) {

        Random random = new Random();
        double windChangeX = 0;// wind velocity on x
        double windChangeY = 0;// wind velocity on y
        double angle; //the angle from wich the wind will arrive

        
        // since the boundaries could be in multiple ranges(ie 270-365, 0-90), choose randomly two boundaries
        int randomIndex = random.nextInt(angleBoundaries.length / 2); // Select a random pair of angle boundaries
        double startAngle = angleBoundaries[randomIndex * 2];
        double endAngle = angleBoundaries[randomIndex * 2 + 1];
        
        //randomizing a wind strength in the boundaries 1 to maxWindVelocity, always in km/s
        double windVelocity = random.nextDouble() * (maxWindVelocity - 1) + 1;

        //randomly finding the angle the wind arrives from 
        angle = random.nextDouble() * (endAngle - startAngle) + startAngle;

        // radiants are expected in input, not degrees
        windChangeX = (adaptWindToLayerOfAtmosphere(distanceFromSurface,windVelocity) * Math.cos(Math.toRadians(angle)))/3600; // transform km/h in km/s
        //windChangeY = (adaptWindToLayerOfAtmosphere(distanceFromSurface,windVelocity) * Math.sin(Math.toRadians(angle)))/ 3600; 

        return new double[] {windChangeX, windChangeY};

    }

    /**
     * Adapts the wind velocity depending on the atmosphere of Titan that the probe is in
     * Adapts the wind velocity depending on the atmosphere of Titan that the probe is in(proportion is based on below data)
     * @param windVelocity the randomised current wind velocity 
     * @param currentDistanceFromSurface the probe's distance from Titan's surface
     * @return the maxWindvelocity relative to the layer of the atmosphere the probe is in and the maxVelocity bound
     * Layers of Titan's atmosphere:
            1. Troposphere: 0-32 km (0-20 mi),  wind speeds range from 0 to 100 kilometers per hour
            2. Stratosphere: 32-100 km (20-62 mi), wind speeds range from  100 to 400 kilometers per hour
            3. Mesosphere: 100-210 km (62-130 mi),  wind speeds range from  50 to 200 kilometers per hour
            4. Thermosphere: Above 210 km (130 mi), wind speeds range from  0 to 50 kilometers per hour
     */
    private double adaptWindToLayerOfAtmosphere(double currentDistanceFromSurface, double windVelocity){

        int troposphereDistance = 32;
        int stratosphereDistance = 100;
        int mesosphereDistance = 210;
        
        if(currentDistanceFromSurface <= troposphereDistance) {  //Troposphere
            return (windVelocity/4);

        } else if (currentDistanceFromSurface <= stratosphereDistance) { //Stratosphere
            return (windVelocity);

        } else if (currentDistanceFromSurface <= mesosphereDistance) { //Mesosphere
            return (2*windVelocity/4);

        } else {     //Thermosphere over 210km
            return (windVelocity/8);
        } 
    }

    public void setDirection(boolean direction) {
        Direction = direction;
    }

    public static boolean getDirection(){
        return Direction;
    }
        
}
