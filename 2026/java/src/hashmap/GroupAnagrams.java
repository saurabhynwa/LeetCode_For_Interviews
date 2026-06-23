package src.hashmap;

/*
Given an array of strings strs, group the anagrams together. You can return the answer in any order.

Example 1:
Input: strs = ["eat","tea","tan","ate","nat","bat"]
Output: [["bat"],["nat","tan"],["ate","eat","tea"]]

Explanation: There is no string in strs that can be rearranged to form "bat". The strings "nat" and "tan" are anagrams
as they can be rearranged to form each other. The strings "ate", "eat", and "tea" are anagrams as they can be rearranged
to form each other.

Example 2:
Input: strs = [""]
Output: [[""]]

Example 3:
Input: strs = ["a"]
Output: [["a"]]

Constraints:

1 <= strs.length <= 104
0 <= strs[i].length <= 100
strs[i] consists of lowercase English letters.
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

/*
Time Complexity: O(N * K log K) where N is the total number of words in the input array, and K is the maximum length of
a single string. We loop through all N words sequentially. For each word, sorting its K characters takes O(K log K) time

Space Complexity: O(N * K) to store all the processed strings inside our anagramGroupRegistry map memory.
 */

public class GroupAnagrams {
    private List<List<String>> groupAnagrams(String[] strs){
        // edge case
        if(strs == null || strs.length == 0){
            return new ArrayList<>();
        }

        // Logic: for the current word, sort it and use that as the HashMap key. Values will be the original word.
        Map<String, List<String>> anagramMap = new HashMap<>();

        for (String word : strs) {
            // convert to char array for sorting
            char[] wordArr = word.toCharArray();
            // sort it
            Arrays.sort(wordArr);
            // create string of this sorted word
            String anagramKey = new String(wordArr);

            // check if the map has entry for this key, if not create one
            if (!anagramMap.containsKey(anagramKey)) {
                anagramMap.put(anagramKey, new ArrayList<>());
            }

            // append the current word to the list. No need to put back to map, as list are mutable
            anagramMap.get(anagramKey).add(word);
        }

        return new ArrayList<>(anagramMap.values());
    }

    private void printResult(List<List<String>> result){
        for(List<String> list: result){
            System.out.print("[ ");
            for(String str: list){
                System.out.print(str + ", ");
            }
            System.out.print("], ");
        }
    }

    public static void main() {
        GroupAnagrams groupAnagrams = new GroupAnagrams();
        groupAnagrams.printResult(groupAnagrams.groupAnagrams(new String[]{"eat","tea","tan","ate","nat","bat"}));
    }
}
