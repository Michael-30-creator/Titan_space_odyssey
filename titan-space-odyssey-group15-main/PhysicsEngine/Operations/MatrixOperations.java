package PhysicsEngine.Operations;

/**
 * This class implements operations on 3 dimensional matrices
 */

public class MatrixOperations {
    
    /**
     * Adds matrix B to A
     * @param vectorA
     * @param vectorB
     * @return new matrix
     */
    public static double[][] matrixAddition(double[][] matrixA, double[][] matrixB)
    {
        if(matrixA.length != matrixB.length || matrixA[0].length != matrixB[0].length)
        {
            System.out.println("Illegal Matrix operation");
            return null;
        }

        double[][] A = new double[matrixA.length][matrixA[0].length];

        for(int i = 0; i < matrixA.length; i++)
        {
            for(int j = 0; j < matrixA[0].length; j++)
            {
                A[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }

        return A;
    }

    /**
     * Subtracts matrix B to A
     * @param vectorA
     * @param vectorB
     * @return new matrix
     */
    public static double[][] matrixSubtraction(double[][] matrixA, double[][] matrixB)
    {
        if(matrixA.length != matrixB.length || matrixA[0].length != matrixB[0].length)
        {
            System.out.println("Illegal Matrix operation");
            return null;
        }

        double[][] A = new double[matrixA.length][matrixA[0].length];

        for(int i = 0; i < matrixA.length; i++)
        {
            for(int j = 0; j < matrixA[0].length; j++)
            {
                A[i][j] = matrixA[i][j] - matrixB[i][j];
            }
        }

        return A;
    }

    /**
     * multiplies matrix A with scalar
     * @param matrixA
     * @param value
     * @return
     */
    public static double[][] matrixScalarMultiplication(double[][] matrixA, double scalar)
    {
        double[][] A = new double[matrixA.length][matrixA[0].length];

        for(int i = 0; i < matrixA.length; i++)
        {
            for(int j = 0; j < matrixA[0].length; j++)
            {
                A[i][j] = matrixA[i][j] * scalar;
            } 
        }

        return A;
    }
    
}
