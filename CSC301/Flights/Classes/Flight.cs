using System;
using System.Collections;
using System.Linq;
using System.Text;
using System.Xml;
using System.Text.RegularExpressions;

namespace Flights.Classes
{

    class Flight
    {
        private String flightNo;
        private FlightTime departure;
        private FlightTime arrival;
        private Route route;
        private bool[] daysFlying = new bool[7]; // 0 for monday all the way to 6.

        public String FlightNo
        {
            get
            {
                return this.flightNo;
            }
        }
        public FlightTime Departure
        {
            get
            {
                return this.departure;
            }
        }
        public FlightTime Arrival
        {
            get
            {
                return this.arrival;
            }
        }
        public Route Route
        {
            get
            {
                return this.route;
            }
        }
        public bool[] DaysFlying
        {
            get
            {
                return this.daysFlying;
            }
        }

        public Flight() { }
        public Flight(String xml, Route route)
        { // xml parser for flight details

            XmlDocument doc = new XmlDocument();
            doc.LoadXml(xml);

            XmlElement head = doc.DocumentElement;
            this.flightNo = head.GetAttribute("no");

            this.route = route;

            Regex timeparse = new Regex("([0-9]+):([0-9]+)");

            XmlNode depart = head.SelectSingleNode("/Flight/depart");
            XmlNode arrive = head.SelectSingleNode("/Flight/arrive");
            XmlNode daysflying = head.SelectSingleNode("/Flight/operation");

            Match departMatch = timeparse.Match(depart.InnerText);
            if (departMatch.Success)
            {
                this.departure = new FlightTime(Convert.ToInt16(departMatch.Groups[1].Value), Convert.ToInt16(departMatch.Groups[2].Value), Convert.ToBoolean(depart.Attributes["nextDay"].Value));
            }

            Match arriveMatch = timeparse.Match(arrive.InnerText);
            if (arriveMatch.Success)
            {
                this.arrival = new FlightTime(Convert.ToInt16(arriveMatch.Groups[1].Value), Convert.ToInt16(arriveMatch.Groups[2].Value), Convert.ToBoolean(arrive.Attributes["nextDay"].Value));
            }

            // set default value
            for (int i = 0; i < this.daysFlying.Length; i++)
            {
                this.daysFlying[i] = false;
            }

            if ((daysflying.ChildNodes.Count == 1) && (daysflying.ChildNodes[0].InnerText.ToLower().Equals("alldays")))
            {
                for (int i = 0; i < this.daysFlying.Length; i++)
                {
                    this.daysFlying[i] = true;
                }
            }
            else
            {
                foreach (XmlNode day in daysflying)
                {
                    this.daysFlying[FlightTime.indexForDay(day.InnerText)] = true;
                }

            }
        }

        public int flightDuration()
        { // in minutes

            int arriveHour = this.arrival.hour;
            int arriveMin = this.arrival.min;

            if (this.departure.nextDay)
            {
                arriveHour += 24;
            }

            if (this.arrival.min < this.departure.min)
            {
                return ((arriveHour - 1 - this.departure.hour) * 60) + (this.arrival.min + 60 - this.departure.min);

            }
            else
            {
                return ((arriveHour - this.departure.hour) * 60) + (this.arrival.min - this.departure.min);
            }
        }

        public bool hasFlightAtTime(FlightTime time, String day)
        { // returns true if time given is before flight
            if (this.departure.compareFlightTime(time) <= 0)
            {
                if (time.nextDay)
                {
                    return daysFlying[FlightTime.indexForDayByAddingDays(day, 1)];
                }
                else
                {
                    return daysFlying[FlightTime.indexForDay(day)];
                }
            }
            else
            {
                return false;
            }
        }

        public override string ToString()
        {
            String rtrstr = "\n\t" + this.flightNo + " - depart: " + this.departure + " arrive: " + this.arrival + "\n\t";
            for (int i = 0; i < this.daysFlying.Length; i++)
            {
                if (this.daysFlying[i])
                {
                    rtrstr += FlightTime.days[i] + " ";
                }
            }
            return rtrstr;
        }
    }
}
