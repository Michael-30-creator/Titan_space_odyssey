package PhysicsEngine.States;

public class RocketState {

    private static RocketState instance;
    private double[] positionsMidpoint = new double[2];
    private double[] positionsTail = new double[2];
    private double[] positionsTip = new double[2];
    private double[] velocities = new double[2];

    double angle;

    private RocketState(double[]positionsMidpoint, double[] positionsTail,double[] positionsTip,double[] velocities, double angle){
        this.positionsMidpoint = positionsMidpoint;
        this.positionsTail = positionsTail;
        this.positionsTip = positionsTip;
        this.velocities = velocities;
        this.angle = angle;
    }

    public static RocketState getInstance(double[]positionsMidpoint, double[] positionsTail,double[] positionsTip,double[] velocities, double angle) {
        if (instance == null) {
          instance = new RocketState(positionsMidpoint, positionsTail, positionsTip,velocities,angle);
        }
        return instance;
    }

    public static RocketState getInstance() {
        return instance;
    }

    public static void setInstance(RocketState instance) {
        RocketState.instance = instance;
    }

    public double[] getPositionsMidpoint() {
        return positionsMidpoint;
    }

    public void setPositionsMidpoint(double[] positionsMidpoint) {
        this.positionsMidpoint = positionsMidpoint;
    }

    public double[] getPositionsTail() {
        return positionsTail;
    }

    public void setPositionsTail(double[] positionsTail) {
        this.positionsTail = positionsTail;
    }

    public double[] getPositionsTip() {
        return positionsTip;
    }

    public void setPositionsTip(double[] positionsTip) {
        this.positionsTip = positionsTip;
    }

    public double[] getVelocities() {
        return velocities;
    }

    public void setVelocities(double[] velocities) {
        this.velocities = velocities;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
