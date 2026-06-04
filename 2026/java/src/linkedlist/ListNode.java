package src.linkedlist;

public class ListNode {
    public int val;
    public ListNode next;

    public ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    public static void printLinkedList(ListNode head){
        ListNode current = head; // so that we keep the reference to 'head' intact.
        while(current != null){
            System.out.print(current.val + " -> ");
            current = current.next;
        }
        System.out.println();
    }
}
