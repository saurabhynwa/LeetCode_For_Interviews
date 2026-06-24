package src.design;

import java.util.HashMap;
import java.util.Map;

/*
Design a data structure that follows the constraints of a Least Recently Used (LRU) cache.

Implement the LRUCache class:
    - LRUCache(int capacity) Initialize the LRU cache with positive size capacity.
    - int get(int key) Return the value of the key if the key exists, otherwise return -1.
    - void put(int key, int value) Update the value of the key if the key exists. Otherwise, add the key-value pair to
    the cache. If the number of keys exceeds the capacity from this operation, evict the least recently used key.

The functions get and put must each run in O(1) average time complexity.

Example 1:
Input
["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]
[[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]
Output
[null, null, null, 1, null, -1, null, -1, 3, 4]

Explanation
LRUCache lRUCache = new LRUCache(2);
lRUCache.put(1, 1); // cache is {1=1}
lRUCache.put(2, 2); // cache is {1=1, 2=2}
lRUCache.get(1);    // return 1
lRUCache.put(3, 3); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
lRUCache.get(2);    // returns -1 (not found)
lRUCache.put(4, 4); // LRU key was 1, evicts key 1, cache is {4=4, 3=3}
lRUCache.get(1);    // return -1 (not found)
lRUCache.get(3);    // return 3
lRUCache.get(4);    // return 4


Constraints:

1 <= capacity <= 3000
0 <= key <= 104
0 <= value <= 105
At most 2 * 105 calls will be made to get and put.
 */

public class LRUCache {
    // HashMap to support O(1) get and put with Doubly linked list acting as the cache usage order

    private static class CacheNode {
        int key;
        int value;
        CacheNode previous;
        CacheNode next;

        public CacheNode(int key, int value){
            this.key = key;
            this.value = value;
        }
    }

    // class level entities
    private final int capacity;
    private final Map<Integer, CacheNode> mapRegistry;

    // for doubly linked list, use head and tail nodes to manage null/end conditions
    private final CacheNode headSentinel;
    private final CacheNode tailSentinel;

    public LRUCache(int capacity){
        this.capacity = capacity;
        // initialize map
        this.mapRegistry = new HashMap<>();

        // initialize the head and tail nodes. Initialize with -1 to avoid null checks
        this.headSentinel = new CacheNode(-1, -1);
        this.tailSentinel = new CacheNode(-1, -1);

        // wire up head and tail together, doubly linked list !
        this.headSentinel.next = this.tailSentinel;
        this.tailSentinel.previous = this.headSentinel;
    }

    // we always add node next to head
    private void addNodeToFront(CacheNode node){
        // link up node's pointers
        node.previous = headSentinel;
        node.next = headSentinel.next;

        // update head and tail links w.r.t node
        headSentinel.next.previous = node;
        headSentinel.next = node;
    }

    private void removeNodeFromList(CacheNode node){
        // secure links for node to be removed
        CacheNode previousNode = node.previous;
        CacheNode nextNode = node.next;

        // remove node by updating links
        previousNode.next = nextNode;
        nextNode.previous = previousNode;
    }

    // LRU cache used, need to refresh cache for ordering
    private void refreshNodeUsage(CacheNode node){
        removeNodeFromList(node);
        addNodeToFront(node);
    }

    // when capacity is full, you need to evict the oldest key from the cache
    private CacheNode popOldestNodeFromTail(){
        CacheNode oldestNode = tailSentinel.previous;
        removeNodeFromList(oldestNode);
        // we also need to remove from the map registry
        return oldestNode;
    }

    private int get(int key){
        if(!mapRegistry.containsKey(key)){
            return -1;
        } else {
            CacheNode existingNode = mapRegistry.get(key);
            // cache has been hit, refresh it for the existing node
            refreshNodeUsage(existingNode);
            return existingNode.value;
        }
    }

    private void put(int key, int value){
        if(mapRegistry.containsKey(key)){
            // cache already has that key, no need to add again
            CacheNode existingNode = mapRegistry.get(key);
            // update the latest value for this key
            existingNode.value = value;
            // refresh the cache with this new value
            refreshNodeUsage(existingNode);
        } else {
            // create new node
            CacheNode newNode = new CacheNode(key, value);

            // check if cache is at capacity or not
            if(mapRegistry.size() >= capacity){
                // evict the oldest key
                CacheNode oldestNode = popOldestNodeFromTail();
                // remove from map
                mapRegistry.remove(oldestNode.key);
            }

            // add the new node to cache and to map registry as well
            addNodeToFront(newNode);
            mapRegistry.put(newNode.key, newNode);
        }
    }

    public static void main(String[] args) {
        System.out.println("Initializing LRU Cache with a capacity of 2 slots...");
        LRUCache cache = new LRUCache(2);

        // 1. Add key 1 and 2
        cache.put(1, 10); // Cache state: [1:10] -> Sentinels
        cache.put(2, 20); // Cache state: [2:20] -> [1:10] -> Sentinels
        System.out.println("Put (1, 10) and (2, 20) executed successfully.");

        // 2. Fetch key 1 (This should refresh its usage status)
        int value1 = cache.get(1);
        System.out.println("Get(1) returned: " + value1 + " (Expected: 10)");
        // Cache state updated: [1:10] -> [2:20] (Key 2 is now the oldest!)

        // 3. Put key 3. Cache is full! Key 2 should be evicted.
        System.out.println("Adding (3, 30)... This triggers an eviction event.");
        cache.put(3, 30); // Cache state: [3:30] -> [1:10] (Key 2 dropped!)

        // 4. Verify Key 2 is gone
        int value2 = cache.get(2);
        System.out.println("Get(2) returned: " + value2 + " (Expected: -1 due to eviction)");

        // 5. Update Key 1 value to 40
        cache.put(1, 40);
        System.out.println("Updated Key 1 to 40. Get(1) returns: " + cache.get(1) + " (Expected: 40)");
    }
}
