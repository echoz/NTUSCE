using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Collections;
using Flights.Classes;
using Flights.UI;

namespace Flights
{
    public partial class Form1 : Form
    {
        private Timetable tt;


        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            flyingFrom.DataBindings.Add("Text", flightsBindingSource, "Route.Origin");
            flyingTo.DataBindings.Add("Text", flightsBindingSource, "Route.Destination");
            toggleFlightDetails(false);
            tabControl1.Enabled = false;
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            checkOriginDest(sender, e);
        }

        private void checkOriginDest(object sender, EventArgs e)
        {
            if ((comboBox2.SelectedValue != null) && (comboBox1.SelectedValue != null))
            {

                if (comboBox1.SelectedValue.Equals(comboBox2.SelectedValue))
                {
                    button1.Enabled = false;
                    checkBox1.Enabled = false;
                    checkBox1_CheckedChanged(sender, e);
                }
                else
                {
                    button1.Enabled = true;
                    checkBox1.Enabled = true;
                    checkBox1_CheckedChanged(sender, e);
                }
            }     
        }

        private void comboBox2_SelectedIndexChanged(object sender, EventArgs e)
        {
            checkOriginDest(sender, e);
        }

        private void button2_Click(object sender, EventArgs e)
        {



        }

        private void button1_Click(object sender, EventArgs e)
        {
            String day = dateTimePicker1.Value.DayOfWeek.ToString().Substring(0,2);
            String dayReturn = dateTimePicker2.Value.DayOfWeek.ToString().Substring(0, 2);

            String origin = comboBox1.SelectedValue.ToString();
            String dest = comboBox2.SelectedValue.ToString();
            
            FlightTime start = new FlightTime(dateTimePicker1.Value.Hour, dateTimePicker1.Value.Minute, false);
            FlightTime returnTime = new FlightTime(dateTimePicker2.Value.Hour, dateTimePicker2.Value.Minute, false);
            int tolerance = (int)numericUpDown1.Value;

            toolStripComboBox1.Items.Clear();
            result.Text = "";

            ArrayList flights = tt.flightsForOriginDest(origin, dest, start, day, tolerance);

            if (checkBox1.Checked)
            {
                ArrayList returnFlights = tt.flightsForOriginDest(dest, origin, returnTime, dayReturn, tolerance);
                flights.AddRange(returnFlights);
            }

            bindingSource1.DataSource = flights;

            result.Text = bindingSource1.Count + " path(s) found";

            ArrayList routesForFlightPath = new ArrayList(0);
            String[] flightPath;
            String merged;
            for (int i = 0; i < bindingSource1.Count; i++)
            {
                routesForFlightPath.Clear();
                merged = "";

                for (int q = 0; q < ((Flight[])bindingSource1[i]).Length; q++)
                {
                    routesForFlightPath.Add(((Flight[])bindingSource1[i])[q].Route);
                }
                flightPath = Timetable.destinationsForRoute((Route[])routesForFlightPath.ToArray(typeof(Route)));

                for (int z = 0; z < flightPath.Length; z++)
                {
                    merged += flightPath[z];

                    if (z != flightPath.Length - 1)
                    {
                        merged += "-";
                    }
                }

                toolStripComboBox1.Items.Add(merged);
            }

            if (bindingSource1.Count > 0)
            {
                toggleFlightDetails(true);
                toolStripComboBox1.SelectedIndex = 0;
            }
            else
            {
                flightsBindingSource.DataSource = typeof(Flight);
                toggleFlightDetails(false);
            }
        }

        private void toggleFlightDetails(bool visible)
        {
            label7.Visible = visible;
            label8.Visible = visible;
            label9.Visible = visible;
            label10.Visible = visible;
        }

        private void checkBox1_CheckedChanged(object sender, EventArgs e)
        {
            dateTimePicker2.Enabled = checkBox1.Checked;
        }

        private void toolStripComboBox1_selectionChanged(object sender, EventArgs e)
        {
            flightsBindingSource.DataSource = bindingSource1[toolStripComboBox1.SelectedIndex];
            path.Text = toolStripComboBox1.SelectedItem.ToString();
        }

        private void toolStripButton1_Click(object sender, EventArgs e)
        {
            xmlDB.ShowDialog(this);
            if ((xmlDB.CheckFileExists) && (!xmlDB.SafeFileName.Equals("")))
            {
                toolStripComboBox1.Items.Clear();
                result.Text = "";

                tt = new Timetable(xmlDB.FileName);
                timetableBindingSource.DataSource = tt;

                tabControl1.Enabled = true;
                checkBox1_CheckedChanged(sender, e);
                file.Text = xmlDB.SafeFileName;

                checkOriginDest(sender, e);
                flightsBindingSource.DataSource = typeof(Flight);
                toggleFlightDetails(false);

                checkDestinationParameters();
            }
        }

        private void destOrigin_SelectedIndexChanged(object sender, EventArgs e)
        {
            checkDestinationParameters();
        }

        private void destDest_SelectedIndexChanged(object sender, EventArgs e)
        {
            checkDestinationParameters();
        }

        private void checkDestinationParameters()
        {
            if ((destDest.SelectedValue != null) && (destOrigin.SelectedValue != null))
            {
                if (destDest.SelectedValue == destOrigin.SelectedValue)
                {
                    destReturn.Enabled = true;
                    destReturnLabel.Enabled = true;
                }
                else
                {
                    destReturn.Enabled = false;
                    destReturnLabel.Enabled = false;
                }

                checkedListBox1.Items.Clear();
                foreach (String destination in destinationsBindingSource)
                {
                    if ((!destination.Equals(destOrigin.SelectedValue.ToString())) && (!destination.Equals(destDest.SelectedValue.ToString())))
                        checkedListBox1.Items.Add(destination);
                }

            }
        }

        private void button2_Click_1(object sender, EventArgs e)
        {
            String dayStart = destDepart.Value.DayOfWeek.ToString().Substring(0, 2);
            String dayReturn = null;
            if (destReturn.Enabled)
            {
                dayReturn = destReturn.Value.DayOfWeek.ToString().Substring(0, 2);
            }

            String origin = destOrigin.SelectedValue.ToString();
            String dest = destDest.SelectedValue.ToString();

            ArrayList checkedStops = new ArrayList(0);

            foreach (object obj in checkedListBox1.CheckedItems)
            {
                checkedStops.Add(obj.ToString());
            }
            checkedStops.Add(dest);

            String[] otherStops = (String[])checkedStops.ToArray(typeof(String));

            ArrayList multiStopRoutes = tt.routesForMultiStop(origin, dest, otherStops, dayStart, dayReturn);

            ArrayList conversion;
            ArrayList conversionArray = new ArrayList(0);
            foreach (String[] dests in multiStopRoutes)
            {
                conversion = new ArrayList(0);
                conversion.Add(new Destination(origin));
                for (int i = 0; i < dests.Length; i++)
                {
                    conversion.Add(new Destination(dests[i]));
                }
                conversionArray.Add(conversion);
            }


            multicitypaths.Items.Clear();
            for (int i = 0; i < multiStopRoutes.Count; i++)
            {
                multicitypaths.Items.Add(i + 1);
            }

            multicityPathsFound.Text = multiStopRoutes.Count + " path(s) found";

            bindingSource2.DataSource = conversionArray;

            if (bindingSource2.Count > 0)
            {
                multicitypaths.SelectedIndex = 0;
            }
            else
            {
                destinationBindingSource.DataSource = typeof(Destination);
            }
            
        }

        private void multicitypaths_selectionChanged(object sender, EventArgs e)
        {
            destinationBindingSource.DataSource = bindingSource2[multicitypaths.SelectedIndex];
        }
    }
}
