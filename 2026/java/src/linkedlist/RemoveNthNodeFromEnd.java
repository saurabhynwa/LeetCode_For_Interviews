package src.linkedlist;

/*
Given the head of a linked list, remove the nth node from the end of the list and return its head.

Example 1:
Input: head = [1,2,3,4,5], n = 2
Output: [1,2,3,5]

Example 2:
Input: head = [1], n = 1
Output: []

Example 3:
Input: head = [1,2], n = 1
Output: [1]

Constraints:

The number of nodes in the list is sz.
1 <= sz <= 30
0 <= Node.val <= 100
1 <= n <= sz
 */

import static src.linkedlist.ListNode.printLinkedList;
/*
Time Complexity = O(N)

Space Complexity = O(1)
 */
public class RemoveNthNodeFromEnd {
    private ListNode removeNthFromEnd(ListNode head, int n){
        // declare a dummy and point it to head. This will come in handy in case head itself gets removed
        ListNode dummy = new ListNode(-1);
        dummy.next = head;

        // place fast and slow pointer at dummy
        ListNode fast = dummy;
        ListNode slow = dummy;

        // create a gap of 'n' between fast and slow
        for(int i = 0; i < n; i++){
            fast = fast.next;
        }

        // now move fast and slow at same pace till fast reaches the end node. When that happens, slow is at node whose
        // next node we need to remove.
        while(fast.next != null){
            fast = fast.next;
            slow = slow.next;
        }

        // to remove slow's next node, we will break the chain and point it to next of the node to be removed
        slow.next = slow.next.next;

        // return head whose reference is stored in dummy.next
        return dummy.next;
    }

    public static void main(String[] args){
        RemoveNthNodeFromEnd obj = new RemoveNthNodeFromEnd();
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);
        ListNode l5 = new ListNode(5);

        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l4.next = l5;

        System.out.println("Original list:");
        printLinkedList(l1);
        System.out.println("List after removal:");
        printLinkedList(obj.removeNthFromEnd(l1, 2));

        ListNode l6 = new ListNode(1);
        System.out.println("Original list:");
        printLinkedList(l6);
        System.out.println("List after removal:");
        printLinkedList(obj.removeNthFromEnd(l6, 1));

        ListNode l7 = new ListNode(2);
        l6.next = l7;
        System.out.println("Original List");
        printLinkedList(l6);
        System.out.println("List after removal");
        printLinkedList(obj.removeNthFromEnd(l6, 1));
    }
}
