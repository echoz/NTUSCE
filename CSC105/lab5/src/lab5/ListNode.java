package lab5;
/**
 *
 * @author jeremy foo jie you
 * @workstation C199
 * @user hp086541
 * @class fs1
 * @lab 5
 * @course csc105
 */
public class ListNode {

    // references to listnode attributes
    private Object item;
    private ListNode next;

    /*
     * Constructors
     * */
    public ListNode() {
        this.item = null;
        this.next = null;
    }

    public ListNode(Object item) {
        this.item = item;
        this.next = null;
    }

    public ListNode(Object item, ListNode next) {
        this.item = item;
        this.next = next;
    }

    // getter setter methods
    public ListNode getNext() { return this.next; }
    public ListNode setNext(ListNode n) { this.next = n; return this; }
    public Object getItem() { return this.item; }
    public ListNode setItem(Object obj) { this.item = obj; return this; }

    public String toString() {
        return this.item.toString();
    }
}
