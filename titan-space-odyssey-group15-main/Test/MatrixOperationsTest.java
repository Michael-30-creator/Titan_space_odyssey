package Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Assert;
import org.junit.Test;

import PhysicsEngine.Operations.MatrixOperations;
import PhysicsEngine.Operations.VectorOperations;


public class MatrixOperationsTest {

    double[][] matrixA = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    double[][] matrixB = {{9, 8, 7}, {6, 5, 4}, {3, 2, 1}};
    double[][] matrixC = {{1, 2, 3}, {4, 5, 6}};
    double[][] expectedSum = {{10, 10, 10}, {10, 10, 10}, {10, 10, 10}};
    double[][] matrix5A =  {{5, 10, 15}, {20, 25, 30}, {35, 40, 45}};

    double[] vectorA = {1, 2, 3};
    double[] vectorB = {4, 5, 6, 7};
    double[] vectorC = {10, 9, 8};
    double[] vectorSum = {11, 11, 11};
    double[] vectorSubtract = {9, 7, 5};
    double[] vectorHalfA = {0.5, 1, 1.5};


    @Test public void testMatricesWrongSizeAddition() {
        assertNull(MatrixOperations.matrixAddition(matrixA, matrixC));
    }

    @Test public void testMatricesWrongSizeSwitchedAddition() {
        assertNull(MatrixOperations.matrixAddition(matrixC, matrixA));
    }

    @Test public void testMatricesCorrectSizeAddition(){
        assertArrayEquals(expectedSum, MatrixOperations.matrixAddition(matrixA, matrixB));
    }
    
    @Test public void testMatrixScalarMultiplication(){
        assertArrayEquals(matrix5A, MatrixOperations.matrixScalarMultiplication(matrixA, 5));
    }

    @Test public void testVectorsWrongSizeAddition(){
        assertNull(VectorOperations.vectorAddition(vectorA,vectorB ));
    }

    @Test public void testVectorsWrongSizeSwitchedAddition() {
        assertNull(VectorOperations.vectorAddition(vectorB,vectorA));
    }

    @Test public void testVectorsCorrectSizeAddition(){
        Assert.assertArrayEquals(vectorSum, VectorOperations.vectorAddition(vectorA, vectorC), 0.000001);
    }

    @Test public void testVectorsWrongSizeSubtraction(){
        assertNull(VectorOperations.vectorAddition(vectorA,vectorB ));
    }

    @Test public void testVectorsWrongSizeSwitchedSubtraction() {
        assertNull(VectorOperations.vectorAddition(vectorB,vectorA));
    }

    @Test public void testVectorsCorrectSizeSubtraction(){
        Assert.assertArrayEquals(vectorSubtract, VectorOperations.vectorAddition(vectorC, vectorA), 0.000001);
    }

    @Test public void testVectorScalarMultiplication(){
        Assert.assertArrayEquals(matrix5A[0], VectorOperations.vectorScalarMultiplication(matrixA[0], 5),0.000001);
    }

    @Test public void testVectorScalarDivision(){
        Assert.assertArrayEquals(vectorHalfA, VectorOperations.vectorScalarMultiplication(matrixA[0], 2),0.000001);
    }

    

}




