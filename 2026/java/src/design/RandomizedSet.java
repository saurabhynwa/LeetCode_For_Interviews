package src.design;

/*
Implement the RandomizedSet class:

RandomizedSet() Initializes the RandomizedSet object.
bool insert(int val) Inserts an item val into the set if not present. Returns true if the item was not present, false otherwise.
bool remove(int val) Removes an item val from the set if present. Returns true if the item was present, false otherwise.
int getRandom() Returns a random element from the current set of elements (it's guaranteed that at least one element exists when this method is called). Each element must have the same probability of being returned.
You must implement the functions of the class such that each function works in average O(1) time complexity.



Example 1:

Input
["RandomizedSet", "insert", "remove", "insert", "getRandom", "remove", "insert", "getRandom"]
[[], [1], [2], [2], [], [1], [2], []]
Output
[null, true, false, true, 2, true, false, 2]

Explanation
RandomizedSet randomizedSet = new RandomizedSet();
randomizedSet.insert(1); // Inserts 1 to the set. Returns true as 1 was inserted successfully.
randomizedSet.remove(2); // Returns false as 2 does not exist in the set.
randomizedSet.insert(2); // Inserts 2 to the set, returns true. Set now contains [1,2].
randomizedSet.getRandom(); // getRandom() should return either 1 or 2 randomly.
randomizedSet.remove(1); // Removes 1 from the set, returns true. Set now contains [2].
randomizedSet.insert(2); // 2 was already in the set, so return false.
randomizedSet.getRandom(); // Since 2 is the only number in the set, getRandom() will always return 2.


Constraints:

-231 <= val <= 231 - 1
At most 2 * 105 calls will be made to insert, remove, and getRandom.
There will be at least one element in the data structure when getRandom is called.
 */

import java.util.*;

public class RandomizedSet {
    // Logic: maintain hashmap of value to index, and list of values. Use random to generate random index within the
    // list range and get that value. Trick is in the remove part. While list has remove method on it, that internally
    // triggers an array copy over and moves all elements from right to left. We can make that O(1) by swapping the last
    // value to the index that is being removed and then removing the last index of the list itself (duplicate value)

    private final Map<Integer, Integer> valueToIndexMap;
    private final List<Integer> valueList;
    private final Random randomEngine;

    public RandomizedSet(){
        this.valueToIndexMap = new HashMap<>();
        this.valueList = new ArrayList<>();
        this.randomEngine = new Random();
    }

    private boolean insert(int val){
        if(valueToIndexMap.containsKey(val)){
            // already entry exists in the set, can't insert duplicates
            return false;
        }

        int insertionIndex = valueList.size();
        valueToIndexMap.put(val, insertionIndex);
        valueList.add(val);

        return true;
    }

    private boolean remove(int val){
        if(!valueToIndexMap.containsKey(val)){
            return false;
        }

        // lastIndex value swap and the pop
        int indexToBeRemoved = valueToIndexMap.get(val);
        int lastIndex = valueList.size() - 1;
        int valueAtLastIndex = valueList.get(lastIndex);

        if(indexToBeRemoved != lastIndex){
            // copy over in the list
            valueList.set(indexToBeRemoved, valueAtLastIndex);
            // update map for the lastIndexValue key
            valueToIndexMap.put(valueAtLastIndex, indexToBeRemoved);
        }

        // remove from the list the duplicate value sitting on the last index
        valueList.remove(lastIndex);
        // remove from map the key 'val'
        valueToIndexMap.remove(val);

        return true;
    }

    private int getRandom(){
        int randomIndex = randomEngine.nextInt(valueList.size());
        return valueList.get(randomIndex);
    }
}
