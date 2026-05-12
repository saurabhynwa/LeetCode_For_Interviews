package src.slidingWindow;

/*
Given a string s and an integer k, return the length of the longest substring of s that contains at most k distinct
characters.

Example 1:

Input: s = "eceba", k = 2
Output: 3
Explanation: The substring is "ece" with length 3.
Example 2:

Input: s = "aa", k = 1
Output: 2
Explanation: The substring is "aa" with length 2.


Constraints:

1 <= s.length <= 5 * 104
0 <= k <= 50
*/

/*
Time Complexity: (O(n)) — We traverse the string once with the end pointer.

Space Complexity: (O(1)) — The size of the state array is fixed at 128 (or 26), which does not grow with the input size.

For HashMap version of this algorithm is (O(min(n, m))), where (n) is the length of the string and (m) is the size of
the character set (the alphabet).In many interview contexts, this is often simplified to (O(1)) if the character set is
fixed (e.g., only 26 uppercase English letters or 128 ASCII characters).
 */

import java.util.HashMap;
import java.util.Map;

public class LongestSubStringWithAtMostKDistinctCharacters {
    public int lengthOfLongestSubstringKDistinct(String s, int k) {
        // sanity check
        if(s == null || s.isEmpty()){
            return 0;
        }

        int result = 0;

        // Logic: Sliding window. Our window can have at most k distinct characters. We maintain a map with character as
        // key and its occurrence as value. The moment our window see more than 2 characters, we slide our window by
        // moving the start pointer.

        Map<Character, Integer> charMap = new HashMap<>();

        for(int startPtr = 0, endPtr = 0; endPtr < s.length(); endPtr++){
            char current = s.charAt(endPtr);

            // update map for current char
            charMap.put(current, charMap.getOrDefault(current, 0) + 1);

            // check if window is breached
            while(charMap.size() > k){
                // move the start pointer and update map accordingly
                char charAtStartPtr = s.charAt(startPtr);
                int value = charMap.get(charAtStartPtr);

                if(value - 1 == 0){
                    // remove from Map
                    charMap.remove(charAtStartPtr);
                } else {
                    // update map with reduced value
                    charMap.put(charAtStartPtr, value - 1);
                }

                // move startPtr
                startPtr++;
            }

            // check for length
            result = Math.max(result, endPtr - startPtr + 1);
        }

        return result;
    }

    static void main(){
        LongestSubStringWithAtMostKDistinctCharacters obj1 = new LongestSubStringWithAtMostKDistinctCharacters();
        System.out.println(obj1.lengthOfLongestSubstringKDistinct("eceba", 2));
        System.out.println(obj1.lengthOfLongestSubstringKDistinct("aa", 1));
    }
}
