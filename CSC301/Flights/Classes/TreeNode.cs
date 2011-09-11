using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Flights.Classes
{
    class TreeNode<T> : Node<T>
    {
        public TreeNode() : base() { }
        public TreeNode(T data) : base(data, null) { }

        public NodeList<T> Children
        {
            get
            {
                if (base.Neighbors == null)
                    base.Neighbors = new NodeList<T>();

                return this.Neighbors;
            }
        }
    }
}
