package PhysicsEngine.Wind;

import java.util.Arrays;

public class windMain {
    public static void main(String[] args) {
        
        //creating an object wind with rightwind, leftwind or leftandrightwind
        Wind wind = new LeftWind(200);
        
        //example data
        double[] velocity = {0,0,0};

        System.out.println(Arrays.toString(velocity));
        
        //calling the function
        velocity = wind.applyWind(velocity);

        //changing both the y and x
        System.out.println(Arrays.toString(velocity));
    }
}
