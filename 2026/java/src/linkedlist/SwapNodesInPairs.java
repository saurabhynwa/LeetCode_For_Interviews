package src.linkedlist;

/*
Given a linked list, swap every two adjacent nodes and return its head. You must solve the problem without modifying the
values in the list's nodes (i.e., only nodes themselves may be changed.)

Example 1:
Input: head = [1,2,3,4]
Output: [2,1,4,3]


Example 2:
Input: head = []
Output: []

Example 3:
Input: head = [1]
Output: [1]

Example 4:
Input: head = [1,2,3]
Output: [2,1,3]

Constraints:

The number of nodes in the list is in the range [0, 100].
0 <= Node.val <= 100
 */

import static src.linkedlist.ListNode.printLinkedList;

/*
Time Complexity = O(N)

Space Complexity = O(1)
 */
public class SwapNodesInPairs {
    private ListNode swapNodesInPairs(ListNode head){
        // sanity check
        if(head == null || head.next == null){
            return head;
        }

        // Logic: we use right pointer and the next node to it for swapping. Once the swap is done, it is important for
        // us to rejoin the list that went under the change to the new swapped nodes.

        ListNode dummy = new ListNode(-1);
        dummy.next = head; // secure connection to head

        ListNode left = dummy; // copy pointer to establish link between dummy and new head post first swap and after
        // that 'left' plays the role of bridge between the left part of list linking to the newly swapped nodes.

        ListNode right = head;

        while(right != null && right.next != null){
            // secure next right
            ListNode nextRight = right.next;
            // secure nextRight's next. When swap happens, nextRight points to right, and right needs to change its
            // direction from nextRight (to avoid cycle) to the next node, which is nextRight's next.
            ListNode nextRightNext = nextRight.next;

            // swap the nodes by changing directions
            nextRight.next= right;
            right.next = nextRightNext;

            // now link the old/left part of the list to the new start of the swapped nodes
            left.next = nextRight;

            // now move left to end node of the swapped pair as that node will become the new starting point for old/left
            // list when a swap happens
            left = right;

            // now move right to the next node for swapping to continue
            right = right.next; // could have also used nextRightNext, one and the same thing
        }

        return dummy.next;
    }

    public static void main(String[] args){
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);
        ListNode l5 = new ListNode(5);

        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l4.next = l5;

        SwapNodesInPairs obj = new SwapNodesInPairs();
        System.out.println("Original list:");
        printLinkedList(l1);
        System.out.println("List Post swapping:");
        printLinkedList(obj.swapNodesInPairs(l1));

    }
}
