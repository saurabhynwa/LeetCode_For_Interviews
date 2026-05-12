package src.slidingWindow;

import java.util.Map;
import java.util.HashMap;

/*
You are given a string s and an integer k. You can choose any character of the string and change it to any other
uppercase English character. You can perform this operation at most k times.

Return the length of the longest substring containing the same letter you can get after performing the above operations.

 Example 1:

Input: s = "ABAB", k = 2
Output: 4
Explanation: Replace the two 'A's with two 'B's or vice versa.
Example 2:

Input: s = "AABABBA", k = 1
Output: 4
Explanation: Replace the one 'A' in the middle with 'B' and form "AABBBBA".
The substring "BBBB" has the longest repeating letters, which is 4.
There may exists other ways to achieve this answer too.


Constraints:

1 <= s.length <= 105
s consists of only uppercase English letters.
0 <= k <= s.length
 */

/*
Time Complexity: (O(n)) — We traverse the string once with the end pointer.

Space Complexity: (O(1)) — The size of the state array is fixed at 128 (or 26), which does not grow with the input size.

For HashMap version of this algorithm is (O(min(n, m))), where (n) is the length of the string and (m) is the size of
the character set (the alphabet).In many interview contexts, this is often simplified to (O(1)) if the character set is
fixed (e.g., only 26 uppercase English letters or 128 ASCII characters).
 */

public class LongestRepeatingCharacterReplacement {
    private int getLongestRepeatingSubStringLength(String str, int k){
        int longestRepeatingSubStringLength = 0;

        // sanity check
        if(str == null || str.isEmpty()){
            return longestRepeatingSubStringLength;
        }

        // Logic: Sliding window. We have 'k' operations that we can use to change characters and form the longest
        // repeating substring. So the question here is what is the window size ? Answer: window size keeps on expanding
        // until the number of characters in the window that needs to be replaced are greater than 'k'. When we come
        // across that condition, we slide our window by moving start pointer.

        // For the scenario where our window contains characters that are inside the allowed 'k' operations, we keep on
        // expanding the window and check the max sub string size.

        // key point: we are recording max frequency which is the most number of times a character occurs in a window.
        // This variable only grows and never reduces even if the start pointer moves. Why so ? because this variable
        // tells us what window size we need to actually scan for. So there is no point of looking for max result in a
        // smaller window. For the existing window size, result won't change anyway.

        // MaxFrequency doesn't relate to the character count we maintain in HashMap or array. It tells us about the
        // window size that needs to be looked upon next. Example, you have calculated the max sub string for window
        // size 4, now next result would be possible from a greater window size.

        Map<Character, Integer> charMap = new HashMap<>();
        int maxFrequency = 0;

        for(int startPtr = 0, endPtr = 0; endPtr < str.length(); endPtr++){
            char charAtEndPtr = str.charAt(endPtr);

            // update the map
            charMap.put(charAtEndPtr, charMap.getOrDefault(charAtEndPtr, 0) + 1);

            // check for the maxFrequency. It only grows. So use Math.max
            maxFrequency = Math.max(maxFrequency, charMap.get(charAtEndPtr));

            // check whether the elements in existing window that needs to be replaced are > k
            // total characters in the window - maxFrequency > k
            if((endPtr - startPtr + 1) - maxFrequency > k){
                char charAtStartPtr = str.charAt(startPtr);
                int value = charMap.get(charAtStartPtr);
                // update map
                charMap.put(charAtStartPtr, value - 1);

                //move startPtr
                startPtr++;
            }

            // check for max substring length
            longestRepeatingSubStringLength = Math.max(longestRepeatingSubStringLength, endPtr - startPtr + 1);
        }

        return longestRepeatingSubStringLength;
    }

    private int arrayVersion(String str, int k){
        int longestRepeatingSubStringLength = 0;

        // sanity check
        if(str == null || str.isEmpty()){
            return longestRepeatingSubStringLength;
        }

        // a to z : 97 to 122
        // A to Z: 65 to 90
        int[] alphabetAscii = new int[122]; // substitute for HashMap

        int maxFrequency = 0;

        for(int startPtr = 0, endPtr = 0; endPtr < str.length(); endPtr++){
            char charAtEndPtr = str.charAt(endPtr);

            // character value is the array index and the value at the index is the count of how many times that
            // particular character has occurred in the given input
            alphabetAscii[charAtEndPtr]++;

            // check max frequency to help determine window size that needs to be checked for
            maxFrequency = Math.max(maxFrequency, alphabetAscii[charAtEndPtr]);

            // check if the elements in the existing window that needs to be replaced are outrunning the available 'k'
            // operations. Total characters in the window - maxFrequency > k
            if((endPtr - startPtr + 1) - maxFrequency > k){
                // move the start pointer and update the count accordingly
                char charAtStartPtr = str.charAt(startPtr);
                alphabetAscii[charAtStartPtr]--;
                startPtr++;
            }

            // longest sub string length check
            longestRepeatingSubStringLength = Math.max(longestRepeatingSubStringLength, endPtr - startPtr + 1);
        }

        return longestRepeatingSubStringLength;
    }

    static void main(){
        LongestRepeatingCharacterReplacement longestRepeatingCharacterReplacement = new LongestRepeatingCharacterReplacement();
        System.out.println("HashMap version");
        // HashMap version
        System.out.println(longestRepeatingCharacterReplacement.getLongestRepeatingSubStringLength("ABB", 1));
        System.out.println(longestRepeatingCharacterReplacement.getLongestRepeatingSubStringLength("AAAA", 0));
        System.out.println(longestRepeatingCharacterReplacement.getLongestRepeatingSubStringLength("ABAB", 2));
        System.out.println(longestRepeatingCharacterReplacement.getLongestRepeatingSubStringLength("AABABBA", 1));

        System.out.println("Array version");
        // Array version
        System.out.println(longestRepeatingCharacterReplacement.arrayVersion("ABB", 1));
        System.out.println(longestRepeatingCharacterReplacement.arrayVersion("AAAA", 0));
        System.out.println(longestRepeatingCharacterReplacement.arrayVersion("ABAB", 2));
        System.out.println(longestRepeatingCharacterReplacement.arrayVersion("AABABBA", 1));
    }
}
