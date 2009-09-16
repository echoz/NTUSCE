package lab9;
/**
 *
 * @author jeremy foo jie you
 * @workstation C199
 * @user hp086541
 * @class fs1
 * @lab 7
 * @course csc105
 */
public class Queue extends List {
    private ListNode tail;

    // default constructor
    public Queue() {
        super();
        tail = null;
    }

    // add objec to queue, at the end of it. increment item count as wel.
    public boolean enqueue(Object o) {
        itemcount++;
        if (isEmpty()) {
            head = tail = new ListNode(o);
            return true;
        }
        tail.setNext(new ListNode(o));
        tail = tail.getNext();
        return true;
    }

    // remove the first item from the queue and return that object.
    public Object dequeue() {
        if (head == null) return null;
        Object deqObj = head.getItem();
        head = head.getNext();
        if (itemcount == 0) tail = null;
        return deqObj;
    }


    // clears the queue to nothing.
    public void clear() {
        this.head = null;
        this.tail = null;
        this.itemcount = 0;
    }
}
