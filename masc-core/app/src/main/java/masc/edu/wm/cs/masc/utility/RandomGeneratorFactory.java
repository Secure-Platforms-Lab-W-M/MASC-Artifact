package masc.edu.wm.cs.masc.utility;

import java.util.Random;

public class RandomGeneratorFactory {

    private static long seed = 123456789;

    public Random getGenerator(){
        return new Random(seed);
    }


}
