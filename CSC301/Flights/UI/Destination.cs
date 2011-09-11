using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Flights.UI
{
    class Destination
    {
        private String __value;

        public String Value
        {
            get
            {
                return this.__value;
            }

            set
            {
                this.__value = value;
            }
        }

        public Destination(String val)
        {
            this.__value = val;
        }
    }
}
