/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lab7;

/**
 *
 * @author hp086541
 */
public class BST implements Cloneable {

    // binary search tree attributes
    private BSTNode root;
    private int count;

    // default constructor
    public BST() {
        root = null;
    }

    // private access constructor to be used for cloning only.
    private BST(BSTNode root, int count) {
        this.root = root;
        this.count = count;
    }

    // check if binary search tree is empty;
    public boolean isEmpty() { return root == null; }

    // returns the root node
    public BSTNode getRoot() { return this.root; }

    // return item count
    public int getCount() { return count; }

    // clears the binary tree
    public void clear() {
        root = null;
        count = 0;
    }

    // inserts a node to the tree by transversal.
    public void insert(Customer c) {
        if (root == null) {
            root = new BSTNode(c);
        } else {
            insertTT(root, c);
        }
        count++;
    }

    // ensure that insertion into the tree is correct by transversing and looking
    // for the correct location to insert.
    private void insertTT(BSTNode cur, Customer c) {
        if (cur.getItem().getAccountID() > c.getAccountID()) {
            if (cur.getLeft() == null) {
                cur.setLeft(new BSTNode(c));
            } else {
                insertTT(cur.getLeft(), c);
            }
        } else {
            if (cur.getRight() == null) {
                cur.setRight(new BSTNode(c));
            } else {
                insertTT(cur.getRight(), c);
            }
        }
    }

    // clone it.
    public BST clone() {
        return new BST(this.root, this.count);
    }
}
