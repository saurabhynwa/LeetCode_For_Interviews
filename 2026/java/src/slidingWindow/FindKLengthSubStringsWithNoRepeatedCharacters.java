package src.slidingWindow;

/*
Given a string s and an integer k, return the number of substrings in s of length k with no repeated characters.

Example 1:

Input: s = "havefunonleetcode", k = 5
Output: 6
Explanation: There are 6 substrings they are: 'havef','avefu','vefun','efuno','etcod','tcode'.
Example 2:

Input: s = "home", k = 5
Output: 0
Explanation: Notice k can be larger than the length of s. In this case, it is not possible to find any substring.

Constraints:

1 <= s.length <= 104
s consists of lowercase English letters.
1 <= k <= 104
*/

/*
Time Complexity = O(n)

Space Complexity = O(1)
 */

import java.util.HashMap;
import java.util.Map;

public class FindKLengthSubStringsWithNoRepeatedCharacters {
    public int numKLenSubstrNoRepeats(String s, int k) {
        // sanity check
        if(s == null || s.isEmpty() || s.length() < k){
            return 0;
        }

        // Logic: Sliding window. We will maintain a Map for the window size 'k'. If window size and map size is same that means we have all unique
        // elements in our window.

        int result = 0;
        Map<Character, Integer> charMap = new HashMap<>();

        for(int startPtr = 0, endPtr = 0; endPtr < s.length(); endPtr++){
            char current = s.charAt(endPtr);

            // update map
            charMap.put(current, charMap.getOrDefault(current, 0) + 1);

            // check if window size is reached
            if(endPtr - startPtr + 1 == k){
                // check for map size
                if(charMap.size() == k){
                    // add to result
                    result++;
                }

                // slide window by moving start pointer and update map accordingly
                char charAtStartPtr = s.charAt(startPtr);
                int value = charMap.get(charAtStartPtr);

                if(value - 1 == 0){
                    // remove key from Map
                    charMap.remove(charAtStartPtr);
                } else {
                    // update map with reduced value
                    charMap.put(charAtStartPtr, value - 1);
                }

                // move start pointer
                startPtr++;
            }
        }

        return result;
    }

    public int arrayVersion(String s, int k) {
        if (s == null || s.length() < k) return 0;

        int[] counts = new int[26]; // Assuming lowercase English letters
        int uniqueCount = 0;
        int result = 0;

        for (int startPtr = 0, endPtr = 0; endPtr < s.length(); endPtr++) {
            // 1. Add current character
            int endIdx = s.charAt(endPtr) - 'a'; // a to z: ASCII 97 to 122 and A to Z: ASCII 65 to 90
            if (counts[endIdx] == 0) uniqueCount++;
            counts[endIdx]++;

            // 2. Check if window size 'k' is reached
            if (endPtr - startPtr + 1 == k) {
                // If uniqueCount == k, all characters in the window are unique
                if (uniqueCount == k) {
                    result++;
                }

                // 3. Shrink window from the left
                int startIdx = s.charAt(startPtr) - 'a';
                counts[startIdx]--;
                if (counts[startIdx] == 0) uniqueCount--;

                startPtr++;
            }
        }
        return result;
    }


    static void main(){
        FindKLengthSubStringsWithNoRepeatedCharacters obj1 = new FindKLengthSubStringsWithNoRepeatedCharacters();
        System.out.println(obj1.numKLenSubstrNoRepeats("havefunonleetcode", 5));
        System.out.println(obj1.numKLenSubstrNoRepeats("home", 5));

        System.out.println(obj1.arrayVersion("havefunonleetcode", 5));
        System.out.println(obj1.arrayVersion("home", 5));
    }
}
