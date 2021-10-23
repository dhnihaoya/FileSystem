package Utils;

import java.util.ArrayList;
import java.util.Collections;

public class RandomUtil {

    public static ArrayList<Integer> randomShuffledNumbers(int totalNumbers){
        ArrayList<Integer> numbers = new ArrayList<>();
        for(int i = 0; i < totalNumbers; i++){
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        return numbers;
    }
}
