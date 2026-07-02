package src.interviews.array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// input = [0, 1, 3, 0, 4, 4, 2, 1, 0, 5]. Create unique sub arrays. They should not contain duplicate elements and the
// original order/sequence from input should be preserved

/*
Time Complexity: O(N)

Space Complexity: O(N), worst case all the elements are unique and HashMap extends to 'N' keys
 */

public class UniqueArrays {
    private int[][] getUniqueArrays(int[] nums){
        if(nums == null || nums.length == 0){
            return new int[0][0];
        }

        // Logic: As we walk maintain a frequency Map. The frequency determines which List the current number should be
        // added. In list number gets appended at last, so order is maintained. Moment we see duplicate, it goes to the
        // new list. Hence, all our lists have unique and order preserved integers from input.

        // Basically the moment you see a number reoccur, you put it into a new list. Now which list that number should
        // go into is decided by its occurrence count. Let's say we are seeing the number for the first time, so it
        // should go to first list which sits at zero index, you see it second time, 2nd list (index 1) and so on.

        Map<Integer, Integer> frequencyMap = new HashMap<>();
        List<List<Integer>> resultList = new ArrayList<>();

        for(int num: nums){
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);

            int numberOfOccurrence = frequencyMap.get(num);

            if(numberOfOccurrence > resultList.size()){
                // create a new list, add this num and add to the global list
                List<Integer> tempList = new ArrayList<>();
                tempList.add(num);
                resultList.add(tempList);
            } else {
                // get the list. Use index values to fetch the relevant list.
                int fetchListIndex = numberOfOccurrence - 1;
                resultList.get(fetchListIndex).add(num);
            }
        }

        // List has Integer, we want to return int, reverse autoboxing !
        int[][] resultArray = new int[resultList.size()][];
        int outerIndex = 0;

        for(List<Integer> innerList: resultList){
            int[] innerArray = new int[innerList.size()];

            for(int i = 0; i < innerList.size(); i++){
                innerArray[i] = innerList.get(i);
            }

            resultArray[outerIndex] = innerArray;
            outerIndex++;
        }

        return resultArray;
    }

    private void printResult(int[][] resultArray){
        if(resultArray == null || resultArray.length == 0){
            System.out.println("no result available to print");
            return;
        }

        System.out.println("[");
        for(int i = 0; i < resultArray.length; i++){
            int[] innerArray = resultArray[i];

            System.out.print("[");
            for(int j = 0; j < innerArray.length; j++){
                if(j == innerArray.length - 1){
                    System.out.print(innerArray[j]);
                } else {
                    System.out.print(innerArray[j] + ", ");
                }
            }
            System.out.print("]");

            if(i != resultArray.length - 1){
                System.out.println(", ");
            } else {
                System.out.println();
            }
        }
        System.out.print("]");
        System.out.println();
    }

    public static void main(String[] args) {
        UniqueArrays uniqueArrays = new UniqueArrays();
        uniqueArrays.printResult(uniqueArrays.getUniqueArrays(new int[]{0,1,3,0,4,4,2,1,0,5}));
    }
}
