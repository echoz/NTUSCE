using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Flights.Classes
{
    //
    // borrowed excellent node/graph data structure from MSDN with slight enhancements
    // URL: http://msdn.microsoft.com/en-us/vcsharp/aa336800.aspx
    //

    class Graph<T>
    {
        private NodeList<T> nodeSet;

        public Graph() : this(null) { }
        public Graph(NodeList<T> nodeSet)
        {
            if (nodeSet == null)
                this.nodeSet = new NodeList<T>();
            else
                this.nodeSet = nodeSet;
        }

        public void AddNode(GraphNode<T> node)
        {
            // adds a node to the graph
            nodeSet.Add(node);
        }

        public void AddNode(T value)
        {
            // adds a node to the graph
            nodeSet.Add(new GraphNode<T>(value));
        }

        public void AddDirectedEdge(GraphNode<T> from, GraphNode<T> to)
        {
            from.Neighbors.Add(to);
        }

        public void AddDirectedEdge(T from, T to)
        {

            if (!Contains(from))
                AddNode(from);
            if (!Contains(to))
                AddNode(to);

            GraphNode<T> fromNode = (GraphNode<T>)nodeSet.FindByValue(from);
            GraphNode<T> toNode = (GraphNode<T>)nodeSet.FindByValue(to);

            fromNode.Neighbors.Add(toNode);
        }

        public void AddUndirectedEdge(GraphNode<T> from, GraphNode<T> to)
        {
            from.Neighbors.Add(to);

            to.Neighbors.Add(from);
        }

        public void AddUndirectedEdge(T from, T to)
        {

            if (!Contains(from))
                AddNode(from);
            if (!Contains(to))
                AddNode(to);

            GraphNode<T> fromNode = (GraphNode<T>)nodeSet.FindByValue(from);
            GraphNode<T> toNode = (GraphNode<T>)nodeSet.FindByValue(to);

            fromNode.Neighbors.Add(toNode);

            fromNode.Neighbors.Add(toNode);
        }

        public bool Contains(T value)
        {
            return nodeSet.FindByValue(value) != null;
        }

        public bool Remove(T value)
        {
            // first remove the node from the nodeset
            GraphNode<T> nodeToRemove = (GraphNode<T>)nodeSet.FindByValue(value);
            if (nodeToRemove == null)
                // node wasn't found
                return false;

            // otherwise, the node was found
            nodeSet.Remove(nodeToRemove);

            // enumerate through each node in the nodeSet, removing edges to this node
            foreach (GraphNode<T> gnode in nodeSet)
            {
                int index = gnode.Neighbors.IndexOf(nodeToRemove);
                if (index != -1)
                {
                    // remove the reference to the node and associated cost
                    gnode.Neighbors.RemoveAt(index);
                }
            }

            return true;
        }

        public NodeList<T> Nodes
        {
            get
            {
                return nodeSet;
            }
        }

        public int Count
        {
            get { return nodeSet.Count; }
        }
    }
}
