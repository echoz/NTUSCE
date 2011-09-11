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

    class Node<T>
    {
        // Private member-variables
        private T data;
        private NodeList<T> neighbors = null;

        public Node() {}
        public Node(T data) : this(data, null) {}
        public Node(T data, NodeList<T> neighbors)
        {
            this.data = data;
            this.neighbors = neighbors;
        }

        public T Value
        {
            get
            {
                return data;
            }
            set
            {
                data = value;
            }
        }

        protected NodeList<T> Neighbors
        {
            get
            {
                return neighbors;
            }
            set
            {
                neighbors = value;
            }
        }
    }
}
