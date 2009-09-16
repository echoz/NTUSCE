/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lab9;

/**
 *
 * @author hp086541
 */
public class BSTNode {

    // binary search tree node attributes
    private Customer item;
    private BSTNode left;
    private BSTNode right;

    // default constructor
    public BSTNode() {
        this.item = null;
        this.left = null;
        this.right = null;
    }

    // constructor with customer specified
    public BSTNode(Customer c) {
        this.item = c.clone();
        this.left = null;
        this.right = null;
    }

    // full constructor with all attributes specified
    public BSTNode (Customer c, BSTNode left, BSTNode right) {
        this.item = c.clone();
        this.right = right;
        this.left = left;
    }

    // getter and setter methods
    public Customer getItem() { return item; }
    public void setItem(Customer item) { this.item = item.clone(); }

    public BSTNode getLeft() { return left; }
    public void setLeft(BSTNode left) { this.left = left; }

    public BSTNode getRight() { return right; }
    public void setRight(BSTNode right) { this.right = right; }
}
