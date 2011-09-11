using System;
using System.Collections;
using System.Linq;
using System.Text;
using System.Xml;

namespace Flights.Classes
{
    class Route
    {
        private String origin;
        private String destination;
        private ArrayList __flights;

        public String Origin
        {
            get
            {
                return this.origin;
            }
        }
        public String Destination
        {
            get
            {
                return this.destination;
            }
        }
        public Flight[] Flights
        {
            get
            {
                return (Flight[])this.__flights.ToArray(typeof(Flight));
            }
        }

        public Route() { }
        public Route(String xml)
        {
            this.__flights = new ArrayList(0);

            XmlDocument doc = new XmlDocument();
            doc.LoadXml(xml);

            XmlElement head = doc.DocumentElement;
            foreach (XmlAttribute attr in head.Attributes)
            {
                if (attr.Name.ToLower().Equals("origin"))
                {
                    this.origin = attr.Value;
                }
                else if (attr.Name.ToLower().Equals("destination"))
                {
                    this.destination = attr.Value;
                }
            }

            Flight newFlight;
            XmlNodeList flights = head.SelectNodes("/Route/Flight");
            foreach (XmlNode flight in flights)
            {
                newFlight = new Flight(flight.OuterXml, this);
                this.__flights.Add(newFlight);
                newFlight = null;
            }
        }

        // checks if there are flights for the parameters set
        public bool hasFlightsForTime(FlightTime time, String day, int tolerance)
        {
            if (this.flightsForTime((FlightTime)time.Clone(), day, tolerance).Length > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        
        // returns flights that match parameters
        public Flight[] flightsForTime(FlightTime flttime, String fltday, int tolerance)
        {
            // day index, 0 is monday, etc.
            // tolerance is in minutes
            // returns null if not found

            ArrayList flights = new ArrayList(0);
            FlightTime time = (FlightTime)flttime.Clone();
            String day = (String)fltday.Clone();

            if (time.nextDay)
            {
                day = FlightTime.dayByAddingDays(day, 1);
                time.nextDay = false;
            }

            time.addMinutes(tolerance);

            foreach (Flight flt in __flights)
            {
                if (flt.hasFlightAtTime(time, day))
                {
                    flights.Add(flt);
                }
            }

            return (Flight[])flights.ToArray(typeof(Flight));
        }

        // return if flight number exists
        public Boolean hasFlightNo(String flightNo)
        {
            if (flightForNo(flightNo) == null)
            {
                return false;
            }
            else
            {
                return true;
            }

        }
        
        // returns the flight object for the flight number
        public Flight flightForNo(String flightNo)
        {
            foreach (Flight flight in this.__flights)
            {
                if (flight.FlightNo.ToLower().Equals(flightNo.ToLower()))
                {
                    return flight;
                }
            }

            return null;
        }

        public override string ToString()
        {
            String rtrstr = "Origin: " + this.origin + " Destination: " + this.destination + "\nFlights (" + this.__flights.Count + "):\n";

            foreach (Flight flight in this.__flights)
            {
                rtrstr += flight.ToString();
            }

            return rtrstr;
        }
    }
}
