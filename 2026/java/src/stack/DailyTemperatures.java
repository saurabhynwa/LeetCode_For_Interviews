package src.stack;
/*
Given an array of integers temperatures represents the daily temperatures, return an array answer such that answer[i]
is the number of days you have to wait after the ith day to get a warmer temperature. If there is no future day for
which this is possible, keep answer[i] == 0 instead.

Example 1:

Input: temperatures = [73,74,75,71,69,72,76,73]
Output: [1,1,4,2,1,1,0,0]
Example 2:

Input: temperatures = [30,40,50,60]
Output: [1,1,1,0]
Example 3:

Input: temperatures = [30,60,90]
Output: [1,1,0]


Constraints:

1 <= temperatures.length <= 105
30 <= temperatures[i] <= 100
 */
import java.util.Stack;
/*
Time Complexity: (O(N)). Every single index is pushed onto the stack exactly once and popped at most once, guaranteeing
 a completely linear runtime.

Space Complexity: (O(N)) auxiliary space in the worst case (e.g., temperatures are in descending order like [80, 70, 60]
, where every element sits in the stack waiting).
 */

public class DailyTemperatures {
    public int[] dailyTemperatures(int[] temperatures) {
        // Logic: Monotonic stack build using index
        int[] result = new int[temperatures.length];

        Stack<Integer> indexStack = new Stack<>();

        for(int currentIndex = 0; currentIndex < temperatures.length; currentIndex++){
            int currentTemperature = temperatures[currentIndex];

            if(indexStack.isEmpty()){
                indexStack.push(currentIndex);
            } else {
                // check whether currentTemperature > stack top
                // if yes, take the difference of currentIndex - stackTopIndex and store it in
                // result[stackTopIndex]
                // if no, then push the currentIndex to stack
                while(!indexStack.isEmpty() && currentTemperature > temperatures[indexStack.peek()]){
                    int prevIndex = indexStack.pop();
                    result[prevIndex] = currentIndex - prevIndex;
                }
                indexStack.push(currentIndex);
            }
        }

        return result;
    }

    private void printResultArray(int[] result){
        System.out.print("[");
        for(int i = 0; i < result.length; i++){
            if(i == result.length - 1){
                System.out.print(result[i]);
            } else {
                System.out.print(result[i] + ", ");
            }
        }
        System.out.print("]");
        System.out.println();
    }

    public static void main(String[] args) {
        DailyTemperatures dt = new DailyTemperatures();
        dt.printResultArray(dt.dailyTemperatures(new int[]{73,74,75,71,69,72,76,73}));
        dt.printResultArray(dt.dailyTemperatures(new int[]{30,40,50,60}));
        dt.printResultArray(dt.dailyTemperatures(new int[]{30,60,90}));
    }
}
