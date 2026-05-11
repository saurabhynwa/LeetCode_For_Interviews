package src.slidingWindow;

import java.util.Map;
import java.util.HashMap;

/*
Given a string s, find the length of the longest substring without duplicate characters.

Example 1:

Input: s = "abcabcbb"
Output: 3
Explanation: The answer is "abc", with the length of 3. Note that "bca" and "cab" are also correct answers.
Example 2:

Input: s = "bbbbb"
Output: 1
Explanation: The answer is "b", with the length of 1.
Example 3:

Input: s = "pwwkew"
Output: 3
Explanation: The answer is "wke", with the length of 3.
Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.

Constraints:

0 <= s.length <= 5 * 104
s consists of English letters, digits, symbols and spaces.
*/

/*
Time Complexity = O(n)
1) Single Pass: The endPtr iterates through the string exactly once.
2) Constant Time Operations: Inside the loop, Map.get(), Map.put(), Map.containsKey(), and Math.max() are all (O(1))
operations on average.

Space Complexity: (O(min(n, m)))
1) Map Size: You are storing each unique character and its most recent index.
2) Variable (n): In the worst case (all characters are unique), the map stores (n) entries.
3) Variable (m) (Character Set): The map's size is also bounded by the number of unique characters in the input alphabet
 (e.g., 128 for Basic ASCII, 256 for Extended ASCII).
4) Total: (O(min(n, m))). Even for a massive string, the space usage plateaus once every possible character is
represented in the map.
 */

public class LongestSubStringDistinct {
    private int getLongestDistinctSubStringLength(String str){
        int longestDistinctSubStringLength = 0;

        // sanity check
        if(str == null || str.isEmpty()){
            return longestDistinctSubStringLength;
        }

        // Logic: Sliding window. Since we don't have fixed window size our 'end' pointer expands the window and 'start'
        // pointer contracts it by moving forward. Key detail => start pointer only moves forward. This is important for
        // sliding window implementation.

        // End pointer iterates over the characters and adds it to a HashMap (key = character, value = index). The
        // moment 'end' pointer detects a duplicate, we get the current index of the original character from the HashMap
        // and move the start pointer to it's next index. It is important to use Math.max while moving the start pointer
        // to ensure that we don't go back. Example string "abba". Once the 'start' pointer comparison is done, we
        // update the index of the duplicate character with the latest index that 'end' pointer is on. At last, we
        // compare the max distinct sub string length.

        Map<Character, Integer> charMap = new HashMap<>();

        for(int startPtr = 0, endPtr = 0; endPtr < str.length(); endPtr++){
            char current = str.charAt(endPtr);

            if(charMap.containsKey(current)){
                int originalIndexOfTheCurrentChar = charMap.get(current);
                // Math.max ensures startPtr never moves backward. We move startPtr to the next index of the duplicate
                // character's first occurrence.
                startPtr = Math.max(startPtr, originalIndexOfTheCurrentChar + 1);
            }

            // update the Map
            charMap.put(current, endPtr);

            longestDistinctSubStringLength = Math.max(longestDistinctSubStringLength, endPtr - startPtr + 1);
        }

        return longestDistinctSubStringLength;
    }

    static void main(){
        LongestSubStringDistinct longestSubStringDistinct = new LongestSubStringDistinct();
        System.out.println(longestSubStringDistinct.getLongestDistinctSubStringLength("abba"));
        System.out.println(longestSubStringDistinct.getLongestDistinctSubStringLength("abcabcbb"));
        System.out.println(longestSubStringDistinct.getLongestDistinctSubStringLength("bbbbb"));
        System.out.println(longestSubStringDistinct.getLongestDistinctSubStringLength("pwwekw"));
        System.out.println(longestSubStringDistinct.getLongestDistinctSubStringLength("nfpdmpi"));
    }
}
