/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package lab9;

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

    public void remove(int id) {
        if (root == null) return; 
        remove2(root, root, id);
    }

    private void remove2(BSTNode pre, BSTNode cur, int id) {
       if (cur == null) return;
       if (cur.getItem().getAccountID() == id) {
            if ((cur.getLeft() == null) || (cur.getRight() == null)) {
                 case2(pre, cur); 
            } else {
                case3(cur);
            }
            return;
       }
       if (cur.getItem().getAccountID() > id) {
            remove2(cur, cur.getLeft(), id);
       } else {
           remove2(cur, cur.getRight(), id);
       }
    }


    private void case2(BSTNode pre, BSTNode cur) {
        if (pre == cur) {
             if (cur.getLeft() != null) {
                 root = cur.getLeft();
             } else {
                 root = cur.getRight();
             }
             return;
        }
        if (pre.getRight() == cur) { 
             if (cur.getLeft() == null) { 
                  pre.setRight(cur.getRight());
             } else {
                 pre.setRight(cur.getLeft());
             }
        } else { 
             if (cur.getLeft() == null) {
                 pre.setLeft(cur.getRight());
             } else {
                 pre.setLeft(cur.getLeft());
             }
        }
    }


    private void case3(BSTNode cur) {
        BSTNode is, isFather;
        is = isFather = cur.getRight();
        while (is.getLeft() != null) {
           isFather = is;
           is = is.getLeft();
        }
        cur.setItem(is.getItem());           
             
        if (is == isFather) {
            cur.setRight(is.getRight());
        } else {
            isFather.setLeft(is.getRight());
        }
    }

    // clone it.
    public BST clone() {
        return new BST(this.root, this.count);
    }
}
