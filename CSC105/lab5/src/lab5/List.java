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
public class List {
    // setup list functionality through variables
    private ListNode head;
    private int itemcount;
    private ListNode pointer;

    /*
     * Constructor
     * */
    public List() {
        this.head = null;
        this.itemcount = 0;
        this.pointer = this.head;
    }

    // pseudo size method to return item count.
    public int size() { return this.itemcount; }

    // grab object at particular index.
    public Object getItem(int idx) {
        if (idx > itemcount) {
            return null;
        } else {
            return search(idx).getItem();
        }
    }

    // pseudo enumerator function. returns current pointer listnode and inrements
    // to next listnode.
    public ListNode next() {
        ListNode toreturn;
        
        if (this.pointer != null) {
            toreturn = this.pointer;
            this.pointer = this.pointer.getNext();

            return toreturn;
        } else {
            return null;
        }
    }

    // checks to see if pointer is at last element of list.
    public boolean hasNext() {
        if (this.pointer != null) {
            return true;
        } else {
            return false;
        }
    }

    // reset pointer
    public List rewind() {
        this.pointer = this.head;

        return this;
    }

    // remove item from linked list based upon index.
    public boolean remove(int idx) {
        ListNode previous, current;

        if ((idx > itemcount) || (idx < 0)) {
            return false;
        } else if (idx == 0) {
            current = this.head.getNext();
            this.head = current;
            return true;
        } else {
            previous = search(idx-1);
            current = previous.getNext();

            previous.setNext(current.getNext());
            return true;
        }
    }

    // add item to the back of the linked list.
    public boolean add(Object o) {
        return insert(o, this.size());
    }

    // insert item at index of the linked list.
    public boolean insert(Object o, int idx) {
        ListNode previous, current;
        
        if (idx > itemcount) {
            return false;
        } else {
            if (idx > 0) {
                if (this.size() > 1) {
                    previous = search(idx-1);
                    current = previous.getNext();
                    previous.setNext(new ListNode(o, current));
                } else {
                    head.setNext(new ListNode(o, null));
                }
            } else {
                current = head;
                head = new ListNode(o, current);
            }

            itemcount++;
            return true;
        }
    }

    // look through the list to return the node at the index idx.
    public ListNode search(int idx) {
        ListNode current = head;

        if (idx > itemcount) {
            return null;
        } else {
            for (int i=0;i<idx;i++) {
                current = current.getNext();
            }
        }

        return current;
    }

    public String toString() {
        String result = "";
        
        this.rewind();
        while (this.hasNext()) {
            result += this.next().getItem().toString();
        }

        return result;
    }
}
