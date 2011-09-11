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

    class GraphNode<T> : Node<T>
    {
        public GraphNode() : base() { }
        public GraphNode(T value) : base(value) { }
        public GraphNode(T value, NodeList<T> neighbors) : base(value, neighbors) { }

        new public NodeList<T> Neighbors
        {
            get
            {
                if (base.Neighbors == null)
                    base.Neighbors = new NodeList<T>();

                return base.Neighbors;
            }
        }
    }
}
