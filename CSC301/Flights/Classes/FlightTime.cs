using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Flights.Classes
{
    class FlightTime : ICloneable
    {
        public int hour, min;
        public bool nextDay;
        public static String[] days = { "mo", "tu", "we", "th", "fr", "sa", "su" };

        public FlightTime(int hour, int min, bool nextDay)
        {
            this.hour = hour;
            this.min = min;
            this.nextDay = nextDay;
        }

        public int compareFlightTime(FlightTime candidate)
        { // returns -1 for less then, 0 for equals and 1 for more than

            // check for similarity
            if ((candidate.nextDay == this.nextDay) && (candidate.hour == this.hour) && (candidate.min == this.min))
                return 0;

            // deal with larger than
            if ((candidate.nextDay) && (!this.nextDay))
                return 1;

            if (candidate.hour > this.hour)
                return 1;

            if ((candidate.hour == this.hour) && (candidate.min > this.min))
                return 1;

            return -1;

        }

        public void addMinutes(int min)
        {
            if ((this.min + min) > 60)
            {
                this.min = (this.min + min) % 60;
                this.hour += (this.min + min) / 60;
                if (this.hour > 24)
                {
                    this.hour -= 24;
                    this.nextDay = true;
                }
            }
        }

        #region static methods

        public static int indexForDay(String day)
        {
            for (int i = 0; i < FlightTime.days.Length; i++)
            {
                if (day.ToLower().Equals(FlightTime.days[i]))
                {
                    return i;
                }
            }

            return -1;
        }

        public static int indexForDayByAddingDays(String day, int days)
        {
            return (indexForDay(day) + days) % FlightTime.days.Length;
        }

        public static String dayByAddingDays(String day, int days)
        {
            return FlightTime.days[FlightTime.indexForDayByAddingDays(day, days)];
        }

        #endregion

        #region ICloneable Members

        public object Clone()
        {
            FlightTime copy = new FlightTime(this.hour, this.min, this.nextDay);
            return copy;
        }

        #endregion

        public override string ToString()
        {
            return this.hour + ":" + ((this.min < 10) ? ("0" + this.min) : this.min.ToString()) + " " + ((nextDay) ? "next day" : "");
        }

    }
}
