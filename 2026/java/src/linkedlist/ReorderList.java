package src.linkedlist;

/*
You are given the head of a singly linked-list. The list can be represented as:

L0 → L1 → … → Ln - 1 → Ln
Reorder the list to be on the following form:

L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → …
You may not modify the values in the list's nodes. Only nodes themselves may be changed.

Example 1:
Input: head = [1,2,3,4]
Output: [1,4,2,3]

Example 2:
Input: head = [1,2,3,4,5]
Output: [1,5,2,4,3]

Constraints:

The number of nodes in the list is in the range [1, 5 * 104].
1 <= Node.val <= 1000
 */

import static src.linkedlist.ListNode.printLinkedList;

/*
Time Complexity = O(N)

Space Complexity = O(1)
 */
public class ReorderList {
    private ListNode reorderList(ListNode head){
        // sanity check
        if(head == null || head.next == null){
            return head;
        }

        // Logic: use fast and slow pointers to find middle, then reverse from middle to end. The use left and right to
        // alternate. Exit condition is when right.next == null. Trace even and odd length lists on paper to develop or
        // confirm intuition

        // find middle using fast and slow
        ListNode middle = getMiddleNode(head, head);

        // now reverse from middle to end
        ListNode right = reverseAndGetNewHead(middle);
        ListNode left = head;

        while(right.next != null){
            // secure next left
            ListNode nextLeft = left.next;
            // point left towards right
            left.next = right;
            // move left
            left = nextLeft;
            // secure next right
            ListNode nextRight = right.next;
            // point right towards left
            right.next = left;
            // move right
            right = nextRight;
        }

        return head;
    }

    private ListNode reverseAndGetNewHead(ListNode current){
        ListNode prev = null;
        while(current != null){
            // secure next
            ListNode nextCurrent = current.next;
            // reverse the link
            current.next = prev;
            // move prev
            prev = current;
            // move current
            current = nextCurrent;
        }
        return prev;
    }

    private ListNode getMiddleNode(ListNode fast, ListNode slow){
        while(fast != null && fast.next != null){
            // because you cannot invoke '.next' on null
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    public static void main(String[] args){
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);

        l1.next = l2;
        l2.next = l3;
        l3.next = l4;

        System.out.println("Original List:");
        printLinkedList(l1);
        ReorderList obj = new ReorderList();
        System.out.println("Reordered List:");
        printLinkedList(obj.reorderList(l1));

        System.out.println();

        ListNode l5 = new ListNode(1);
        ListNode l6 = new ListNode(2);
        ListNode l7 = new ListNode(3);
        ListNode l8 = new ListNode(4);
        ListNode l9 = new ListNode(5);

        l5.next = l6;
        l6.next = l7;
        l7.next = l8;
        l8.next = l9;

        System.out.println("Original List:");
        printLinkedList(l5);
        System.out.println("Reordered List:");
        printLinkedList(obj.reorderList(l5));
    }
}
