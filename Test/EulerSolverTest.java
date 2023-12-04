package Test;

import org.junit.Test;

import PhysicsEngine.Solvers.EulerSolver;

import org.junit.Assert;


public class EulerSolverTest{
    EulerSolver tester = new EulerSolver();

    double[] arrayA = {5,10,15};
    double[] arrayABy10 = {1,2,3};
    double[] arrayB = {3,4,5};
    double[] nextArrayB = {13,24,35};


    @Test public void accelerationCalcTest(){
        Assert.assertArrayEquals(arrayABy10, tester.accelerationCalculation(5, arrayA ),0.000001);
    }

    @Test public void nextVelocityTest(){
        Assert.assertArrayEquals(nextArrayB, tester.nextVelocity(arrayB, arrayA, 2),0.000001);
    }

    @Test public void solveTest1(){}

    @Test public void solveTest2(){}

    @Test public void solveTest3(){}
}
