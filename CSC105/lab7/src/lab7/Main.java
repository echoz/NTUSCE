package lab7;
import java.io.*;
import java.util.*;
/**
 *
 * @author jeremy foo jie you
 * @workstation C199
 * @user hp086541
 * @class fs1
 * @lab 7
 * @course csc105
 */
public class Main {

    // variables to be used by the program.
    private static BST t1 = new BST();
    private static BST t2 = new BST();
    private static BST t3 = new BST();
    
    public static void main(String[] args) {
        /*
         * variables used in the main program loop
         * - flag to quite the main menu loop
         * - scanner class to parse input
         * - size for storing of records reading size
         * - temp binary search tree for use in reading and parsing customer
         *   data for binary search tree insertion.
         * */
        boolean flag = true;
        Scanner sc;
        int size=0;
        BST temp;

        while (flag) {
            try {
                
                // print menu and ask for main item selection
                System.out.println(menu());
                System.out.print("Selection: ");

                sc = new Scanner(getInput());

                switch (sc.nextInt()) {
                    case 1:
                        // ask for file to grab data for T1 and store into BST t1
                        // output if errors occur.
                        System.out.print("\nPhile for T1: ");
                        try {
                            System.out.println(Integer.toString(readFile(
                                    new File(getInput()))) + " records read.");
                        } catch (java.util.NoSuchElementException nseex) {
                            System.out.println("Wrong format");
                        } catch (FileNotFoundException fnfe) {
                            System.out.println("File does not exist. " +
                                                    fnfe.toString());
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }
                    break;

                    case 2:

                        // perform inorder listing of nodes in t1
                        System.out.println("\nIn-order listof the node IDs:");
                        inOrder(t1);
                        System.out.println("\n");
                    break;

                    case 3:

                        // list all the depeepet nodes of t1 (ie. lowest level)
                        System.out.println("\nList of deepest nodes:");
                        deepestNodes(t1);
                        System.out.println("\n");
                    break;

                    case 4:

                        // do reverse in order listing of t1
                        System.out.println("\nDescending order list of node IDs");
                        descending(t1);
                        System.out.println("\n");
                    break;

                    case 5:
                        System.out.print("\nPhile for T1: ");
                        try {
                            size = 0;

                            // clear t1 and store t1 content in temp
                            t1.clear();      
                            size = readFile(new File(getInput()));
                            temp = t1.clone();

                            // ask for t2 and store t2 from results that have
                            // been read into t1
                            System.out.print("\nPhile for T2: ");
                            t1.clear();
                            t2.clear();
                            size += readFile(new File(getInput()));
                            t2 = t1.clone();

                            // set correct t1 values back from temp and clear temp
                            t1 = temp.clone();
                            temp = null;

                            // show number of records read and perform intersect
                            // operation.
                            System.out.println(size + " records read.");
                            intersect(t1,t2);

                            // prints the post order listing of node ids in t3
                            System.out.println("Post order list of node IDs:");
                            postOrder(t3);
                        // show any errors that occur
                        } catch (java.util.NoSuchElementException nseex) {
                            System.out.println("Wrong format");
                        } catch (FileNotFoundException fnfe) {
                            System.out.println("File does not exist. " +
                                                    fnfe.toString());
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }
                    break;

                    case 6:
                        System.exit(0);
                    break;
                }

            } catch (Exception e) {

            }
        }
    }

    // read through customer records file by line and parses it using
    // a seperate method that returns a full filled customer class.
    public static int readFile(File phile) throws java.io.FileNotFoundException,
                                                    java.text.ParseException {

        // variables used. scanner users a new line delimiter to read
        // line by line
        int count = 0;
        Scanner sc = new Scanner(phile).useDelimiter("\n");
        Customer temp;

        // ensures that there are still content in the file to be read,
        // passes scanner class to parsing method if there are.
        while (sc.hasNext()) {
            temp = parseCustomerDetails(sc);

            // ensures that the returned object is not null (ie. new line or
            // empty class) and inserts it into t1 bst. increment count of
            // records read.
            if (temp != null) {
                t1.insert(temp);
                count++;
            }
        }
        
        return count;
    }

    // help function for post order transversal of binary search tree.
    public static void postOrder(BST customers) {
        postOrderTT(customers.getRoot());
    }

    // displays account id using post order transversal of binary search tree
    private static void postOrderTT(BSTNode cur) {
        if (cur == null) return;
        postOrderTT(cur.getLeft());
        postOrderTT(cur.getRight());
        System.out.print(cur.getItem().getAccountID() + " ");
    }

    // help method for inorder transversal of binary search tree
    public static void inOrder(BST customers) {
        inOrderTT(customers.getRoot());
    }

    //displays account id using post order transverasl of binary search tree
    private static void inOrderTT(BSTNode cur) {
        if (cur == null) return;
        inOrderTT(cur.getLeft());
        System.out.print(cur.getItem().getAccountID() + " ");
        inOrderTT(cur.getRight());
    }

    // prints the nodes from the lowest level of the binary search tree
    public static void deepestNodes(BST customers) {

        /* variables used
         * - nodes stores the nodes that will be used for level by level
         *   transverasl
         * - currentLevel stores the nodes of that current level
         * - cur stores the current bst node
         * - count stores the number of records transversed.
         * */
        Queue nodes = new Queue();
        Queue currentLevel = new Queue();
        BSTNode cur = null;
        int count = 0;
        boolean flag = true;

        // begin by enqueuing initial node with seperator of null
        nodes.enqueue(customers.getRoot());
        nodes.enqueue(null);

        while (flag) {
            // grab the current node
            cur = (BSTNode) nodes.dequeue();

            // checks if current node not is null. this satisfys the condition
            // that the the node is a valid node.
            if (cur != null) {

                // store the current node in the currentLevel queue
                currentLevel.enqueue(cur);

                // enqueue its children for level transversal
                nodes.enqueue(cur.getLeft());
                nodes.enqueue(cur.getRight());

                // increament node count.
                count++;
                
            } else if ((cur == null) && (count == customers.getCount())) {

                // if the current node is null AND the count of items enqueued
                // thus far is equals to the amount of nodes in the binary search
                // tree, this means that its at the end of the level transverasl
                // quit the loop if that's the case.
                flag = false;
            } else {
                // however, if anything else (which means current node is equal
                // to null, it means its come to the end of the level.
                // clear the current level queue to begin storing the next
                // bunch of nodes from the next level.
                currentLevel.clear();
            }
        }

        // transverse the current level queue and display all nodes of the
        // queue which are the nodes from the last level.
        currentLevel.rewind();
        while (currentLevel.hasNext()) {
            cur = (BSTNode) currentLevel.next().getItem();
            System.out.print(cur.getItem().getAccountID() + " ");
        }
    }

    // helper method for doing reverse transveral of binary search tree to get
    // values in descending order
    public static void descending(BST customers) {
        inOrderRTT(customers.getRoot());
    }

    // does reverse inorder tree transverasl to print node account id.
    private static void inOrderRTT(BSTNode cur) {
        if (cur == null) return;
        inOrderRTT(cur.getRight());
        System.out.print(cur.getItem().getAccountID() + " ");
        inOrderRTT(cur.getLeft());
    }

    // parse customer details from file. terminates when empty newline detected
    private static Customer parseCustomerDetails(Scanner sc)
                                        throws java.text.ParseException {
        // variables used for parsing customer details
        /* - keyvalue string to store key and value pairs
         * - key string to store key
         * - value string to store related value
         * - sd to parse keyvalue to get key and value strings
         * - c temporary customer object to fill with details and return
         * */
        String keyvalue, key, value;
        Scanner sd;
        Customer c = null;
        
        while (sc.hasNext()) {

            // grab key value line
            keyvalue = sc.next().trim();

            // ensures that keyvalue line is not an empty newline (end of
            // customer record
            if (keyvalue.length() > 0) {

                // checks to see if temp customer object is not instantiated.
                // instantiate if it is not
                if (c == null) {
                    c = new Customer();
                }

                // parse out the key and value from the keyvalue
                sd = new Scanner(keyvalue).useDelimiter("=");
                key = sd.next().trim().toUpperCase();
                value = sd.next().trim();

                // fills temporary customer class with details
                if (key.equals("ACCOUNT ID")) {
                    c.setAccountID(Integer.parseInt(value));
                } else if (key.equals("NAME")) {
                    c.setName(value);
                } else if (key.equals("ADDRESS")) {
                    c.setAddress(value);
                } else if (key.equals("DOB")) {
                    c.setDob(value);
                } else if (key.equals("PHONE NUMBER")) {
                    c.setPhoneNumber(value);
                } else if (key.contains("ACCOUNT BALANCE")) {
                    c.setBalance(Double.parseDouble(value));
                }

            } else {
                break;
            }
        }

        return c;
    }

    // performa intersection of 2 specified trees and stores in t3
    public static void intersect(BST customers, BST customers2) {
        t3.clear();
        intersectTT(customers.getRoot(), customers2.getRoot());
    }

    // intersection tree transversal
    private static void intersectTT(BSTNode cur, BSTNode cur2) {

        // checks if the current node of tree 1 and 2 exist, if they do not,
        // it means that inersection cannot occur here.
        if ((cur == null) || (cur2 == null)) return;

        // if intersection happens, clone the current item from tree 1
        // and set its account id as the sum of its account id and tree 2's
        // current item.

        // insert this into t3 for storage as final interected class.
        Customer temp = cur.getItem().clone();
        temp.setAccountID((temp.getAccountID() + cur2.getItem().getAccountID()));
        t3.insert(temp);

        intersectTT(cur.getLeft(), cur2.getLeft());
        intersectTT(cur.getRight(), cur2.getRight());
    }

    // grab input from the System IO and return a string of input.
    public static String getInput() {
        try {
            String result="";
            int ch;
            boolean flag = true;

            // grab everything and store in result buffer until a newline or
            // tab character is entered
            while (flag) {
                ch = System.in.read();
                if ((ch == '\t') || (ch == '\n')) {
                    flag = false;
                } else {
                    result += (char) ch;
                }
            }
            return result.trim();
        } catch (IOException e) {

            // returns a null object on error. very unlikely to happen.
            return null;
        }
    }

    private static String menu() {
       String result = "\n(1) Read data to BST";
       result += "\n(2) In-order";
       result += "\n(3) Print deepest ndoes";
       result += "\n(4) Print descending order";
       result += "\n(5) Intersect";
       result += "\n(6) Exit";

       return result;
    }

}
