package src.linkedlist;

/*
Given the head of a singly linked list, return true if it is a palindrome or false otherwise.

Example 1:
Input: head = [1,2,2,1]
Output: true

Example 2:
Input: head = [1,2]
Output: false

Constraints:
The number of nodes in the list is in the range [1, 105].
0 <= Node.val <= 9
*/

/*
Time Complexity: O(N)
Space Complexity: O(1)
 */

public class PalindromeLinkedList {
    private boolean isPalindrome(ListNode head){
        // sanity check
        if(head == null || head.next == null){
            return true;
        }

        // Logic: 2 pointers. 3 steps.
        // First: find middle using slow and fast pointers. Slow is the middle.
        // Second: Reverse the second half starting from slow. New head becomes right pointer
        // Third: Left pointer is given head, right is at new head. Check if left val and right val are equal. If not
        // return false. Loop check: till right pointer is not null

        // find middle: use slow and fast pointer
        ListNode slow = getMiddleNode(head, head);

        // slow is the middle node. Now reverse the 2nd half w.r.t to middle node
        ListNode right = reverseListAndGetNewHead(slow);
        ListNode left = head;

        while(right != null){
            // palindrome check
            if(right.val != left.val){
                return false;
            }
            right = right.next;
            left = left.next;
        }

        return true;
    }

    private ListNode getMiddleNode(ListNode fast, ListNode slow){
        while(fast != null && fast.next != null){
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    private ListNode reverseListAndGetNewHead(ListNode current){
        ListNode prev = null;
        while(current != null){
            // secure next
            ListNode next = current.next;
            // change direction
            current.next = prev;
            // move prev
            prev =  current;
            // move current
            current = next;
        }
        return prev;
    }

    public static void main(String[] args){
        PalindromeLinkedList palindromeLinkedList = new PalindromeLinkedList();
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(2);
        ListNode l4 = new ListNode(1);

        l1.next = l2;
        l2.next = l3;
        l3.next = l4;

        System.out.println(palindromeLinkedList.isPalindrome(l1));

        ListNode l5 = new ListNode(1);
        ListNode l6 = new ListNode(2);
        l5.next = l6;

        System.out.println(palindromeLinkedList.isPalindrome(l5));
    }
}
