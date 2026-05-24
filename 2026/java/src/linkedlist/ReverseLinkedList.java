package src.linkedlist;

public class ReverseLinkedList {

    private void printLinkedList(ListNode head){
        while(head != null){
            System.out.print(head.val + " -> ");
            head = head.next;
        }
        System.out.println();
    }

    private ListNode reverseLinkedList(ListNode head){
        // Logic: initialize a node called previous to null. Assign current to head. While current != null
        // 1. store the reference of current.next
        // 2. switch current.next to previous
        // 3. move previous to current
        // 4. move current to next
        // return previous
        ListNode prev = null;
        ListNode current = head;

        while(current != null){
            // secure current.next reference
            ListNode next = current.next;
            // switch current.next direction
            current.next = prev;
            // move previous ahead
            prev = current;
            // move current ahead
            current = next;
        }

        return prev;
    }

    public static void main(String[] args){
        ReverseLinkedList obj = new ReverseLinkedList();
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);
        ListNode l5 = new ListNode(5);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l4.next = l5;
        l5.next = null;

        obj.printLinkedList(l1);
        obj.printLinkedList(obj.reverseLinkedList(l1));
    }
}
