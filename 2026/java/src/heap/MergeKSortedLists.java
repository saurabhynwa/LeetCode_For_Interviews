package src.heap;

import src.linkedlist.ListNode;
import java.util.PriorityQueue;

/*
Time Complexity = O(N log k)

1) N = Total number of nodes across all lists combined.
2) k = The number of linked lists.
3) The Logic: The main loop runs exactly N times because every single node enters and exits the heap exactly once. Each
heap operation (poll and add) takes logarithmic time relative to its size, costing 𝒪(log𝑘).

Space Complexity = O(k)

1) The Logic: The PriorityQueue is dynamically capped. It only ever holds at most k nodes simultaneously (one
representative from the front of each active line). Memory footprint is completely independent of list length.
 */
public class MergeKSortedLists {
    private ListNode mergeKLists(ListNode[] lists){
        // sanity check
        if(lists == null || lists.length == 0){
            return null;
        }

        // min heap
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>((a,b) -> Integer.compare(
                a.val, b.val
        ));

        // we will only add non-null heads of the list to the minHeap. And then use the minHeap and the node's '.next'
        // to connect the sorted lists in a global sorted list
        for(ListNode head: lists){
            if(head != null){
                minHeap.add(head);
            }
        }

        // initialize a dummy node that sits behind the head. Spawn a duplicate pointer on this dummy, which connects
        // all the nodes. 'dummy.next' points to head once the duplicate pointer is finished
        ListNode dummy = new ListNode(-1);
        ListNode current = dummy;

        while(!minHeap.isEmpty()){
            ListNode smallestNode = minHeap.poll();

            // connect the previous node to this popped node
            current.next = smallestNode;
            // move current
            current = current.next;

            // check if the smallestNode popped has a node next to it. If yes, add that to minHeap
            if(smallestNode.next != null){
                minHeap.add(smallestNode.next);
            }
        }

        return dummy.next;
    }

    public static void main(String[] args){
        MergeKSortedLists obj = new MergeKSortedLists();
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(4);
        ListNode l3 = new ListNode(5);

        l1.next = l2;
        l2.next = l3;
        l3.next = null;

        ListNode l4 = new ListNode(1);
        ListNode l5 = new ListNode(3);
        ListNode l6 = new ListNode(4);

        l4.next = l5;
        l5.next = l6;
        l6.next = null;

        ListNode l7 = new ListNode(2);
        ListNode l8 = new ListNode(6);

        l7.next = l8;
        l8.next = null;

        ListNode[] lists = new ListNode[3];
        lists[0] = l1;
        lists[1] = l4;
        lists[2] = l7;

        ListNode.printLinkedList(l1);
        ListNode.printLinkedList(l4);
        ListNode.printLinkedList(l7);
        ListNode.printLinkedList(obj.mergeKLists(lists));
    }
}
