package lab8;
import java.io.*;
import java.util.*;
/**
 *
 * @author jeremy
 * @workstation C199
 * @user hp086541
 * @class fs1
 * @course csc105
 */
public class Main {

    /**
     * static parameter used throughout the main program loop
     */
    private static Graph graph;

    public static void main(String[] args) {

        // attributes used for main program loop
        // flag to stop loop
        // sc to parse selection
        boolean flag = true;
        Scanner sc;

        while (flag) {
            try {

                // print menu and ask for main item selection
                System.out.println(menu());
                System.out.print("Selection: ");

                sc = new Scanner(getInput());

                switch (sc.nextInt()) {
                    case 1:
                        // ask for file to read graph data from and store in
                        // graph object. returns number of nodes read.
                        System.out.print("\nPhile for graph: ");
                        try {
                            System.out.println(Integer.toString(readFile(
                                    new File(getInput()))) + " nodes read.");
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
                        // ask for node ID and then do a depthFirst transversal 
                        // of the node.
                        System.out.print("\nNode ID: ");
                        try {
                            depthFirst(graph, Integer.parseInt(getInput()));
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }
                    break;

                    case 3:
                        // ask for node ID and then do a breadthfirst transversal
                        // of the node.
                        System.out.print("\nNode ID: ");
                        try {
                            breadthFirst(graph, Integer.parseInt(getInput()));
                        } catch (Exception e) {
                            System.out.println(e.toString());
                        }
                    break;

                    case 4:
                        // list connected conponents
                        connectedComponents(graph);
                    break;

                    case 5:
                        //quit program
                        System.exit(0);
                    break;
                }

            } catch (Exception e) {

            }
        }
    }

    // read graph data into graph
    public static int readFile(File phile) throws FileNotFoundException {
        // looks through the file specified using scanner class
        Scanner sc = new Scanner(phile);

        // sets node count to be read
        int count = sc.nextInt();

        // temporary variable as well as counter variables.
        int i,j, temp;

        // instantiate new graph object
        graph = new Graph(count);

        // loops through all nodes
        for (i=0;i<count;i++) {

            // ignore first number (node ID)
            sc.nextInt();

            // grab number of relationships
            temp = sc.nextInt();

            // loops thorugh relationships and sets the values in graph object
            for (j=0;j<temp;j++) {
                graph.setRelationship(i, sc.nextInt());
            }

        }
        
        return count;

    }

    // helper method to do depth first transversal
    public static void depthFirst(Graph g, int node) {

        // instantiate a visited queue
        Queue visited = new Queue();

        // do depth first transversal on node specified.
        dfs(g,visited,node);

    }

    private static void dfs(Graph g, Queue visited, int node) {

        // returns if current node has been visited before else do stuff!
        if (visited.exists(node)) {
            return;
        } else {

            // enqueue current node in visited queue
            visited.enqueue(node);

            // print current node (pre order processing)
            System.out.print(node + " ");

            // goes through all relationships and do depth first transversal.
            // checking for visited node is done in dfs method.
            for (int i=0;i<g.getCount();i++) {
                if (g.getRelationship(node, i)) {
                    dfs(g,visited, i);
                }
            }

            return;
        }
    }

    // do breadth first transveral of specified node using graph
    public static void breadthFirst(Graph g, int node) {

        // start a visited queue and a transversal queue to do level transversal
        Queue visited = new Queue();
        Queue ttQueue = new Queue();

        // node id to start transversing with
        int nodeToLook;

        // enqueu initial node
        ttQueue.enqueue(node);

        // do until transversal queue is empty.
        while (!ttQueue.isEmpty()) {

            // dequeue a node from the queue.
            nodeToLook = ((Integer) ttQueue.dequeue()).intValue();

            // if node has not been visited before, perform transversal
            if (!visited.exists(nodeToLook)) {

                // add not visited node to visited queue
                visited.enqueue(nodeToLook);

                // print node id
                System.out.print(nodeToLook + " ");

                // goes through all relationships of current node and adds it
                // to the queue.
                for (int i=0;i<g.getCount();i++) {
                    if (g.getRelationship(nodeToLook, i)) {
                        ttQueue.enqueue(i);
                    }
                }
            }
        }
    }

    // method to grab all connected components and do a count. makes use of
    // breadth first transversal as its iterative instead of recursive.
    public static void connectedComponents(Graph g) {

        // start a visited and transversal queue for breadth first transversal
        Queue visited = new Queue();
        Queue ttQueue = new Queue();

        // start a variable to hold current node id as well as a count
        int nodeToLook, count = 0;

        // go through all nodes of the graph
        for (int i=0;i<g.getCount();i++) {

            // if current node that is being visited has not been visited,
            // it is a likely canidate for a connected path.
            if (!visited.exists(i)) {

                // perform breadthfirst transversal by enqueuing first element.
                ttQueue.enqueue(i);

                // do breadth first transversal and print node that is connected
                while (!ttQueue.isEmpty()) {
                    nodeToLook = ((Integer) ttQueue.dequeue()).intValue();
                    if (!visited.exists(nodeToLook)) {
                        visited.enqueue(nodeToLook);
                        System.out.print(nodeToLook + " ");

                        for (int j=0;j<g.getCount();j++) {
                            if (g.getRelationship(nodeToLook, j)) {
                                ttQueue.enqueue(j);
                            }
                        }
                    }
                }

                // clear transversal queue in prep for next node to search.
                // prints a new line to show next path and increment count.
                ttQueue.clear();
                System.out.print("\n");
                count++;
            }
        }

        System.out.println(count);
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
       String result = "\n(1) Read data to graph";
       result += "\n(2) Depth first";
       result += "\n(3) Breadth first";
       result += "\n(4) Connected components";
       result += "\n(5) Exit";

       return result;
    }
}
