package lab8;

/**
 *
 * @author jeremy
 * @workstation C199
 * @user hp086541
 * @class fs1
 * @course csc105
 */
public class Graph {
    // attributes of the graph class :)
    private boolean matrix[][];
    private int count;

    // initialise a graph with the number of nodes
    public Graph(int nodecount) {
        // sets private attribute array with correct size.
        this.matrix = new boolean[nodecount][nodecount];

        // sets default value for array.
        for (int i=0;i<nodecount;i++) {
            for (int j=0;j<nodecount;j++) {
                this.matrix[i][j] = false;
            }
        }

        // set graph node count
        this.count = nodecount;
    }

    // set a relationship between two nodes
    public void setRelationship(int src, int dest) {
        this.matrix[src][dest] = true;
    }

    // remove a relationship between two nodes.
    public void removeRelationship(int src, int dest) {
        this.matrix[src][dest] = false;
    }

    // get the relationship status between two nodes.
    public boolean getRelationship(int src, int dest) {
        return this.matrix[src][dest];
    }

    // returns th node count.
    public int getCount() { return this.count; }

    // outputs the adjcency matrix.
    public String toString() {
        String result = "   ";
        for (int i=0;i<count;i++) {
            result += i + " ";
        }
        result += "\n";
        for (int i=0;i<count;i++) {
            result += i + ": ";
            for (int j=0;j<count;j++) {
                if (matrix[i][j]) {
                    result += "1 ";
                } else {
                    result += "0 ";
                }
            }
            result += "\n";
        }
        return result;
    }
}
