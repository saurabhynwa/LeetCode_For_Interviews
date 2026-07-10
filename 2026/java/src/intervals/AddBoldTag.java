package src.intervals;

import java.util.ArrayList;
import java.util.List;

/*
You are given a string s and an array of strings words. You should add a closed pair of bold tag <b> and </b> to wrap
the substrings in s that exist in words. If two such substrings overlap, you should wrap them together with only one
pair of closed bold-tag. If two substrings wrapped by bold tags are consecutive, you should combine them. Return s after
adding the bold tags.

Example 1:

Input: s = "abcxyz123", words = ["abc","123"]
Output: "<b>abc</b>xyz<b>123</b>"
Explanation: The two strings of words are substrings of s as following: "abcxyz123".
We add <b> before each substring and </b> after each substring.
Example 2:

Input: s = "aaabbb", words = ["aa","b"]
Output: "<b>aaabbb</b>"
Explanation:
"aa" appears as a substring two times: "aaabbb" and "aaabbb".
"b" appears as a substring three times: "aaabbb", "aaabbb", and "aaabbb".
We add <b> before each substring and </b> after each substring: "<b>a<b>a</b>a</b><b>b</b><b>b</b><b>b</b>".
Since the first two <b>'s overlap, we merge them: "<b>aaa</b><b>b</b><b>b</b><b>b</b>".
Since now the four <b>'s are consecutive, we merge them: "<b>aaabbb</b>".
 */

/*
Time Complexity: (O(N x M x L)) where (N) is the length of the string (s) and (M) is number of words given and (L) is
the maximum length of a word.

Space Complexity: (O(N)) auxiliary space to host the boolean[] bold array footprint.
*/

/*
Logic: The Blueprint (The 2-Step Strategy)
1) The Boolean Mask: Create a boolean[] bold array of the same length as your string (s), initialized to false. Loop
through your 'words' dictionary. Every time a word matches a substring in (s), flip those indices in your array to true.

2) The Paintbrush Reconstruction: Loop through your string. If the boolean array transitions from false to true, open a
<b> tag. If it transitions from true to false, close a </b> tag.
 */

public class AddBoldTag {
    private String addBoldTag(String s, String[] words) {
        if (s == null || s.isEmpty() || words == null || words.length == 0) {
            return s;
        }

        // Logic: loop on each character of the input string and use the current index & current index + wordLen as the
        // substring range match. Because substring end range index is exclusive, it's important to add word length to
        // current index. This way we solve the end range exclusive issue. Wherever we find a match we flip the flag in
        // the extra boolean flag array we are maintaining.
        // bold start - current boolean cell is true and previous one is false, handle for zero index
        // then append the character on current index
        // bold end - current boolean cell is true and next cell is false, handle for last index
        int strLength = s.length();
        boolean[] isBold = new boolean[strLength];

        for(int currentIndex = 0; currentIndex < strLength; currentIndex++){
            for(String word: words){
                int wordLen = word.length();
                // guard rail check
                if(currentIndex + wordLen <= strLength && s.substring(currentIndex, currentIndex + wordLen).equals(word)){
                    // flip all the indexes in this range in the boolean array to true
                    for(int j = currentIndex; j < currentIndex + wordLen; j++){
                        isBold[j] = true;
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();

        for(int currentIndex = 0; currentIndex < strLength; currentIndex++){
            // bold start - current boolean cell is true and previous index is false, handle zero index
            if(isBold[currentIndex] && (currentIndex == 0 || !isBold[currentIndex - 1])){
                sb.append("<b>");
            }

            // append current char from string input
            sb.append(s.charAt(currentIndex));

            // bold end - current boolean cell is true and next index is false, handle last index
            if(isBold[currentIndex] && ((currentIndex == strLength - 1) || !isBold[currentIndex + 1])){
                sb.append("</b>");
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        AddBoldTag addBoldTag = new AddBoldTag();
        System.out.println(addBoldTag.addBoldTag("abcxyz123", new String[]{"abc", "123"}));
        System.out.println(addBoldTag.addBoldTag("aaabbb", new String[]{"aa", "b"}));
    }
}

