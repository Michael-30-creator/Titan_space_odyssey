package PhysicsEngine.Operations;
/**
 * This class performs vector operations on 3 dimensional vectors
 */
public class VectorOperations {
    double x;
    double y;
    double z;

    public VectorOperations()
    {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public VectorOperations(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * subtracts vector B from A
     * @param vectorA
     * @param vectorB
     * @return new vector
     */
    public static double[] vectorSubtraction(double[] vectorA, double[] vectorB)
    {
        if(vectorA.length != vectorB.length)
        {
            return null;
        }

        double[] A = new double[vectorA.length];

        for(int i = 0; i < vectorA.length; i++)
        {
            A[i] = vectorA[i] - vectorB[i];
        }

        return A;
    }

    /**
     * Adds vector B to A
     * @param vectorA
     * @param vectorB
     * @return new vector
     */
    public static double[] vectorAddition(double[] vectorA, double[] vectorB)
    {
        if(vectorA.length != vectorB.length)
        {
            return null;
        }

        double[] A = new double[vectorA.length];

        for(int i = 0; i < vectorA.length; i++)
        {
            A[i] = vectorA[i] + vectorB[i];
        }

        return A;
    }

    /**
     * calculates the distance between two vectors in 3D. Sqrt of the sum of the distances squared. It is used as the divisor in Force Calculator
     * @param vectorA
     * @param vectorB
     * @return distance between the two vectors
     */
    public static double euclideanForm(double[] vectorA, double[] vectorB)
    {
        
        double[] A = vectorSubtraction(vectorA, vectorB);

        double euclideanValue = 0;

        for(int i = 0; i < vectorA.length; i++)
        {
            euclideanValue += Math.pow(A[i], 2);
        }

        euclideanValue = Math.sqrt(euclideanValue);

        return euclideanValue;
    }

    /**
     * divides A by scalar value
     * @param vectorA
     * @param value
     * @return
     */
    public static double[] vectorScalarDivision(double[] vectorA, double value)
    {
        double[] A = new double[vectorA.length];

        if(value == 0)
        {
            System.out.println("Division by 0");
        }

        for(int i = 0; i < vectorA.length; i++)
        {
            A[i] = vectorA[i] / value;
        }

        return A;
    }

    /**
     * multiplies vector A with scalar
     * @param vectorA
     * @param value
     * @return
     */
    public static double[] vectorScalarMultiplication(double[] vectorA, double scalar)
    {
        double[] A = new double[vectorA.length];

        for(int i = 0; i < vectorA.length; i++)
        {
            A[i] = vectorA[i] * scalar;
        }

        return A;
    }
    
    /**
     * Calculates the magnitude of three vectors in x, y, z coordinate system
     * @param vectorx vector in x coordinate system
     * @param vectory vector in y coordinate system
     * @param vectorz vector in z coordinate system
     * @return magnitude of the three vectors
     */
    public static double magnitude(double vectorx, double vectory, double vectorz){
        double first = Math.pow(vectorx, 2) + Math.pow(vectory, 2) + Math.pow(vectorz, 2);
        double magnitude = Math.sqrt(first);
        return magnitude; 
    }

    /**
     * Calculates the dot product between 2 vectors
     * @param vectorA 
     * @param vectorB
     * @return
     */
    public static double dotProduct(double[] vectorA, double[] vectorB)
    {
        double dotProduct = 0;

        for(int i = 0; i < vectorA.length; i++)
        {
            dotProduct += vectorA[i] * vectorB[i];
        }

        return dotProduct;
    }

    /**
     * Calculates the magnitude of a single vector
     * @param vector
     * @return
     */
    public static double magnitude(double[] vector)
    {
        double magnitude = 0;

        for(int i = 0; i < vector.length; i++)
        {
            magnitude += Math.pow(vector[i], 2);
        }

        return Math.sqrt(magnitude);
    }

    /**
     * Calculates the angle between vectorA and vectorB
     * @param vectorA
     * @param vectorB
     * @return
     */
    public static double calculateAngle(double[] vectorA, double[] vectorB)
    {
        double dotProduct = VectorOperations.dotProduct(vectorA,vectorB);
        double aMag = VectorOperations.magnitude(vectorA);
        double bMag = VectorOperations.magnitude(vectorB);

        double check = vectorA[0]*vectorB[1] - vectorA[0]*vectorB[0];
        
        if(check < 0)
        {
            return 2*Math.PI - Math.acos(dotProduct/(aMag * bMag));
        }

        return Math.acos(dotProduct/(aMag * bMag));
    }
}
