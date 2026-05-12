package src.slidingWindow;

/*
Given a string s, return the length of the longest substring that contains at most two distinct characters.

Example 1:

Input: s = "eceba"
Output: 3
Explanation: The substring is "ece" which its length is 3.
Example 2:

Input: s = "ccaabbb"
Output: 5
Explanation: The substring is "aabbb" which its length is 5.


Constraints:

1 <= s.length <= 105
s consists of English letters.
*/

/*
Time Complexity = O(n)

Space Complexity = O(1). For HashMap as well, because it is going to hold only English characters which are fixed. If we
had used array then it would be O(1) because array length is fixed. Same reference can be applied to HashMap for
interview purposes.
 */

import java.util.Map;
import java.util.HashMap;

public class LongestSubStringWithAtMost2DistinctCharacters {
    private int lengthOfLongestSubstringTwoDistinct(String s) {
        // sanity check
        if(s == null || s.isEmpty()){
            return 0;
        }

        int result = 0;

        // Logic: Sliding window. Our window can have at most 2 distinct characters (0, 1, 2). We maintain a map with
        // character as key and its occurrence as value. The moment our window see more than 2 characters, we slide our
        // window by moving the start pointer.

        Map<Character, Integer> charMap = new HashMap<>();

        for(int startPtr = 0, endPtr = 0; endPtr < s.length(); endPtr++){
            char current = s.charAt(endPtr);

            // update map for current char
            charMap.put(current, charMap.getOrDefault(current, 0) + 1);

            // check if window is breached
            while(charMap.size() > 2){
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
        LongestSubStringWithAtMost2DistinctCharacters obj1 = new LongestSubStringWithAtMost2DistinctCharacters();
        System.out.println(obj1.lengthOfLongestSubstringTwoDistinct("eceba"));
        System.out.println(obj1.lengthOfLongestSubstringTwoDistinct("ccaabbb"));
    }
}
