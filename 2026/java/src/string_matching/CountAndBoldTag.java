package src.string_matching;

/*
Given a string output and a list of source strings sources, return two results: (1) counts: a list where counts[i] is
the number of occurrences (allowing overlaps) of sources[i] in output; (2) tagged: a new string constructed by wrapping
each maximal contiguous region of output that is covered by at least one match with <tag> and </tag>. After each such
tag, append bracketed source indices in ascending order (e.g., [0][3]) indicating all distinct i for which at least one
occurrence of sources[i] overlaps that region. Indices are 0-based. Overlapping or touching matches are merged into a
single tagged region. Matching is exact and case-sensitive.

Example 1
Input = "ababa", sources = ["aba", "bab", "ab"]
Output
([2, 1, 2], "<tag>ababa</tag>[0][1][2]")
Notes
Occurrences: "aba" at [0,3) and [2,5), "bab" at [1,4), "ab" at [0,2) and [2,4). Entire string is covered, so one region
[0,5). Indices overlapping the region are {0,1,2}.

Example 2
Input = "aaaab", sources = ["aa", "aaa", "b"]
Output
([3, 2, 1], "<tag>aaaa</tag>[0][1]<tag>b</tag>[2]")

Notes: "aa" occurs 3 times, "aaa" occurs 2 times, and "b" once. Covered regions are [0,4) and [4,5). First region
overlaps sources 0 and 1; second overlaps source 2.

Constraints
1 <= len(output) <= 200000
1 <= len(sources) <= 5000
1 <= sum(len(s) for s in sources) <= 200000
All sources[i] are non-empty; duplicates allowed
Matching is exact and case-sensitive
Indices in annotations are 0-based, unique per region, and sorted ascending
 */

import java.util.HashMap;
import java.util.Map;

/*
Time Complexity: (O(N x M x L)) where (N) is the length of the string (s) and (M) is number of words given and (L) is
the maximum length of a word.

Space Complexity: O(N + W) N = auxiliary space to host the boolean[] bold array & W = number of words that match
*/

public class CountAndBoldTag {
    private String getBoldedString(String inputStr, String[] words){
        // sanity checks
        if(inputStr == null || inputStr.trim().isEmpty() || words.length == 0){
            return inputStr;
        }

        // maintaining a Map to track the count of words in the input string
        Map<String, Integer> frequencyMap = new HashMap<>();

        // for each index in the input string, using the substring method where the currentIndex is the starting point
        // and currentIndex + the wordLength of the word from the string [] input is the ending point (substring method
        // end index is exclusive! Hence, the currentIndex + wordLength strategy) is used to match using 'equals'.

        // we are also maintaining a input string array sized boolean array. This array index values are flipped to true
        // when we have a match.

        // bold start - current boolean cell is true && previous index should be false, handle for zero index !
        // append the current input string character
        // bold end - current boolean cell is true && next index is false, handle for last index !
        int strLength = inputStr.length();
        boolean[] isBold = new boolean[strLength];

        // phase_1: flip all the indexes in the boolean array to true for matching words
        for(int currentIndex = 0; currentIndex < strLength; currentIndex++){
            // loop over words array and check for match for each character in the input string
            for(String word: words){
                int wordLength = word.length();

                // guardrail check - index boundary && substring match
                if(currentIndex + wordLength <= strLength &&
                        inputStr.substring(currentIndex, currentIndex + wordLength).equals(word)){
                    // we have a hit for the current word. Update the map first
                    frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);

                    // flip the index range in boolean array to true for the above input string index range match
                    for(int booleanArrIndex = currentIndex; booleanArrIndex < currentIndex + wordLength; booleanArrIndex++){
                        isBold[booleanArrIndex] = true;
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();

        // phase_2: loop over the input string and check boolean array for bold tag open and close
        for(int currentIndex = 0; currentIndex < strLength; currentIndex++){
            // bold start - current boolean index value is true && previous index value is false, handle for zero index
            if(isBold[currentIndex] && (currentIndex == 0 || !isBold[currentIndex - 1])){
                sb.append("<b>");
            }

            // append the current character from the input string
            sb.append(inputStr.charAt(currentIndex));

            // bold end - current boolean index value is true && next index value is false, handle for last index
            if(isBold[currentIndex] && (currentIndex == strLength - 1 || !isBold[currentIndex + 1])){
                sb.append("</b>");
            }
        }

        // print map result
        if(frequencyMap.isEmpty()){
            System.out.println("No bold tag found !");
        } else {
            for(Map.Entry<String, Integer> entry: frequencyMap.entrySet()){
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        CountAndBoldTag countAndBoldTag = new CountAndBoldTag();
        System.out.println(countAndBoldTag.getBoldedString("ababa", new String[]{"aba", "bab", "ab"}));
        System.out.println(countAndBoldTag.getBoldedString("aaaab", new String[]{"aa", "aaa", "b"}));
        System.out.println(countAndBoldTag.getBoldedString("abcxyz123", new String[]{"abc", "123"}));
        System.out.println(countAndBoldTag.getBoldedString("aaabbb", new String[]{"aa", "b"}));
    }
}
