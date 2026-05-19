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
Time Complexity: (O(N . M)) where (N) is the length of the string (s) and (M) is the total character length of all
strings combined inside the words array. This is because indexOf completes a scan across the string payload.

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

        int n = s.length();
        // Step 1: The Boolean Mask (Flagging what needs to be bolded)
        boolean[] bold = new boolean[n];

        for (String word : words) {
            int wordLen = word.length();
            // Find every occurrence of the word in string s
            int index = s.indexOf(word);
            while (index != -1) {
                // Paint this specific section as true in our boolean mask
                for (int i = index; i < index + wordLen; i++) {
                    bold[i] = true;
                }
                // Check if the same word appears later in the string
                index = s.indexOf(word, index + 1);
            }
        }

        // Step 2: Reconstruct the string with tags using our paintbrush analogy
        StringBuilder result = new StringBuilder();
        int runner = 0;

        while (runner < n) {
            if (bold[runner]) {
                // We reached a bold segment! Open the bold tag
                result.append("<b>");

                // Keep moving forward as long as the roadblock requires bolding
                while (runner < n && bold[runner]) {
                    result.append(s.charAt(runner));
                    runner++;
                }

                // The bold segment ended, close the tag
                result.append("</b>");
            } else {
                // Standard non-bold text, just append and pass through
                result.append(s.charAt(runner));
                runner++;
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        AddBoldTag addBoldTag = new AddBoldTag();
        System.out.println(addBoldTag.addBoldTag("abcxyz123", new String[]{"abc", "123"}));
        System.out.println(addBoldTag.addBoldTag("aaabbb", new String[]{"aa", "b"}));
    }
}

