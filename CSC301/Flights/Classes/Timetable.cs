using System;
using System.Collections.Generic;
using System.Collections;
using System.Linq;
using System.Text;
using System.Xml;

namespace Flights.Classes
{
    class Timetable
    {
        private String xmlpath;
        private ArrayList __flights;
        private ArrayList __routes;
        private Graph<String> __routeGraph;
        private ArrayList __destinations;

        public Flight[] Flights
        {
            get
            {
                return (Flight[])this.__flights.ToArray(typeof(Flight));
            }
        }
        public Route[] Routes
        {
            get
            {
                return (Route[])this.__routes.ToArray(typeof(Route));
            }
        }
        public String Xmlpath
        {
            get
            {
                return this.xmlpath;
            }

            set
            {
                this.xmlpath = value;
            }
        }
        public Graph<String> RouteGraph
        {
            get
            {
                return __routeGraph;
            }
        }
        public String[] Destinations
        {
            get
            {
                return (String[])this.__destinations.ToArray(typeof(String));
            }
        }

        public Timetable(String xmlpath)
        {
            this.xmlpath = xmlpath;
            this.__flights = new ArrayList(0);
            this.__routes = new ArrayList(0);
            this.__destinations = new ArrayList(0);
            this.__routeGraph = new Graph<String>();

            this.parse();
        }

        private void parse()
        { // xml parser that builds object graph
            if (!this.xmlpath.Equals(""))
            {
                XmlDocument doc = new XmlDocument();
                Route newRoute;
                doc.Load(this.xmlpath);

                XmlNodeList routes = doc.DocumentElement.SelectNodes("/Timetable/Route");
                foreach (XmlNode route in routes)
                {
                    newRoute = new Route(route.OuterXml);
                    this.__routes.Add(newRoute);

                    if (!this.__destinations.Contains(newRoute.Origin))
                        this.__destinations.Add(newRoute.Origin);

                    if (!this.__destinations.Contains(newRoute.Destination))
                        this.__destinations.Add(newRoute.Destination);

                    __routeGraph.AddDirectedEdge(newRoute.Origin, newRoute.Destination);

                    foreach (Flight flight in newRoute.Flights)
                    {
                        this.__flights.Add(flight);
                    }

                    newRoute = null;
                }
            } 
        }

        public Route[] routesForParameters(String origin, String dest)
        { // returns a route or routes for given parameters
            ArrayList rtrarr = new ArrayList(0);

            foreach (Route route in this.__routes)
            {
                if ((route.Origin.ToLower().Equals(origin.ToLower())) && (route.Destination.ToLower().Equals(dest.ToLower()))) {
                    rtrarr.Add(route);
                }
                else if ((route.Origin.ToLower().Equals(origin.ToLower())) && (dest.ToLower().Equals("")))
                {
                    rtrarr.Add(route);
                }
                else if ((origin.Equals("")) && (route.Destination.ToLower().Equals(dest.ToLower())))
                {
                    rtrarr.Add(route);
                }
            }

            return (Route[])rtrarr.ToArray(typeof(Route));
        }

        #region Flights finding

        public ArrayList flightsForOriginDest(String origin, String dest, FlightTime start, String day, int tolerance)
        {
            // return paths that match the parameters
            ArrayList temp = pathsForOriginDest(origin, dest, start, day, tolerance);
            ArrayList flights = new ArrayList(0);

            // build path using route objects
            foreach (String[] route in temp) {
                // find arraylist of arrays of flights for this route;
                flights.AddRange(flightsForRoute(routeForPath(route), start, day, tolerance));
            }

            return flights;
        }

        public ArrayList flightsForRoute(Route[] routes, FlightTime start, String day, int tolerance)
        {
            ArrayList flights = new ArrayList(0);

            if (routes.Length == 1)
            {
                Flight[] test;
                foreach (Flight flt in routes[0].Flights)
                {
                    test = new Flight[] { flt };
                    flights.Add(test);
                }

            }
            else if (routes.Length > 1)
            {
                // do the tree thing
                TreeNode<Flight> root = flightsTree(routes, start, day, tolerance);
                Stack<Flight> stack = new Stack<Flight>(0);


                buildFlightsArray(root, ref stack, ref flights);
            }

            return flights;
        }

        private void buildFlightsArray(TreeNode<Flight> node, ref Stack<Flight> stack, ref ArrayList flights)
        {
            if (node.Children.Count == 0)
            {
                Flight[] path = stack.ToArray<Flight>();
                Array.Reverse(path);
                flights.Add(path);
                return;
            }
            else
            {
                
                foreach (TreeNode<Flight> child in node.Children)
                {
                    stack.Push(child.Value);
                    buildFlightsArray(child, ref stack, ref flights);
                    stack.Pop();
                }
                return;
            }
        }

        // private function to return root node for flight paths for a particular begin node
        private TreeNode<Flight> flightsTree(Route[] routes, FlightTime start, String day, int tolerance)
        {
            TreeNode<Flight> root = new TreeNode<Flight>();
            TreeNode<Flight> currentNode;

            Flight[] flights = routes[0].flightsForTime(start, day, tolerance);
            foreach (Flight flt in flights)
            {
                // do resursive shit then add to root

                currentNode = new TreeNode<Flight>(flt);
                buildFlightTree(ref currentNode, null, routes, routes[routes.Length - 1], 1, day, tolerance);
                if (currentNode.Children.Count > 0)
                    root.Children.Add(currentNode);
            }

            return root;
        }

        // recursive magic to build the tree of flight paths
        private bool buildFlightTree(ref TreeNode<Flight> node, TreeNode<Flight> previous, Route[] routes, Route dest, int nextDestIndex, String day, int tolerance)
        {
            if (node.Value.Route == dest)
            {
                // success
                return true;
            }
            else
            {
                TreeNode<Flight> currentNode;
                bool something = false;
                Flight[] connecting = connectingForFlight(node.Value, routes[nextDestIndex].Destination, day, tolerance);
                String currentDay = (String)day.Clone();

                if (node.Value.Arrival.nextDay)
                {
                    currentDay = FlightTime.dayByAddingDays(day, 1);
                }

                foreach (Flight flt in connecting)
                {
                    currentNode = new TreeNode<Flight>(flt);
                    int ndi = nextDestIndex + 1;

                    if (ndi >= routes.Length)
                    {
                        ndi = routes.Length - 1;
                    }

                    if (buildFlightTree(ref currentNode, node, routes, dest, ndi, currentDay, tolerance))
                    {
                        node.Children.Add(currentNode);
                        if (!something)
                        {
                            something = true;
                        }
                    }

                }

                return something;
            }
        }

        public Flight[] connectingForFlight(Flight flight, String day, int tolerance)
        {
            return connectingForFlight(flight, "", day, tolerance);
        }
        public Flight[] connectingForFlight(Flight flight, String nextDest, String day, int tolerance)
        {
            ArrayList flights = new ArrayList(0);
            Route[] connectingRoutes = routesForParameters(flight.Route.Destination, nextDest);

            foreach (Route route in connectingRoutes)
            {
                flights.AddRange(route.flightsForTime(flight.Arrival, day, tolerance));
            }

            return (Flight[])flights.ToArray(typeof(Flight));
        }

        #endregion

        #region Route Finding

        public ArrayList routesForMultiStop(String origin, String dest, String[] otherStops, String startDay, String endDay)
        {
            // build root node which links to other nodes
            TreeNode<String> root = new TreeNode<string>(origin);

            buildMultiStopTree(origin, dest, otherStops, endDay, startDay, ref root, null);

            Stack<String> stack = new Stack<String>(0);
            ArrayList multiStopRoutes = new ArrayList(0);

            if (root.Children.Count > 0)
                buildDestinationsArray(root, ref stack, ref multiStopRoutes);

            return multiStopRoutes;
        }

        private void buildDestinationsArray(TreeNode<String> node, ref Stack<String> stack, ref ArrayList destinations)
        {
            if (node.Children.Count == 0)
            {
                String[] path = stack.ToArray<String>();
                Array.Reverse(path);
                destinations.Add(path);
                return;
            }
            else
            {

                foreach (TreeNode<String> child in node.Children)
                {
                    stack.Push(child.Value);
                    buildDestinationsArray(child, ref stack, ref destinations);
                    stack.Pop();
                }
                return;
            }
        }

        private bool buildMultiStopTree(String origin, String dest, String[] remainingNodes, String endDay, String day, ref TreeNode<String> current, TreeNode<String> previous)
        {
            if (remainingNodes.Length == 0)
            {
                if ((pathsForOriginDest(previous.Value, current.Value, day, 0).Count > 0) && 
                    (current.Value.ToLower().Equals(dest.ToLower())))
                {

                    if (endDay == null)
                    {
                        return true;
                    }
                    else if (day.ToLower().Equals(endDay.ToLower()))
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
            if (previous != null)
            {
                if (pathsForOriginDest(previous.Value, current.Value, day, 0).Count == 0)
                {
                    return false;
                }
            }


            // obviously if those fail, something has to be done
            bool something = false;

            String currentDay = (String)day.Clone();

            if (previous != null)
            {
                Route[] rtes = routesForParameters(previous.Value, current.Value);
                if (rtes.Length > 0)
                {
                    Route rte = rtes[0];
                    Flight[] flts = rte.flightsForTime(new FlightTime(0, 0, false), day, 0);

                    foreach (Flight flt in flts)
                    {
                        if (flt.Arrival.nextDay)
                        {
                            currentDay = FlightTime.dayByAddingDays(currentDay, 1);
                            break;
                        }
                    }

                    currentDay = FlightTime.dayByAddingDays(currentDay, 1);
                }
                else
                {

                    return false;
                }
            }



            TreeNode<String> node;

            for (int i = 0; i < remainingNodes.Length; i++)
            {

                node = new TreeNode<string>(remainingNodes[i]);

                if (buildMultiStopTree(origin, dest, remainingByTakingOut(remainingNodes, remainingNodes[i]), endDay, currentDay, ref node, current))
                {
                    current.Children.Add(node);
                    if (!something)
                    {
                        something = true;
                    }
                }
            }

            return something;
        }

        private String[] remainingByTakingOut(String[] nodes, String toTakeout)
        {
            ArrayList final = new ArrayList(0);

            foreach (String str in nodes)
            {
                if (!str.ToLower().Equals(toTakeout.ToLower()))
                {
                    final.Add(str);
                }
            }
            
            return (String[])final.ToArray(typeof(String));
        }

        // converts array of destinations that paths return to array of routes
        public Route[] routeForPath(String[] destinations)
        {
            ArrayList routes = new ArrayList(0);
            for (int i = 0; i < destinations.Length - 1; i++)
            {
                routes.Add(routesForParameters(destinations[i], destinations[i + 1])[0]);
            }
            return (Route[])routes.ToArray(typeof(Route));
        }
        public static String[] destinationsForRoute(Route[] routes)
        {
            ArrayList destinations = new ArrayList(0);
            destinations.Add(routes[0].Origin);
            destinations.Add(routes[0].Destination);

            if (routes.Length > 1)
            {
                for (int i = 1; i < routes.Length; i++)
                {
                    destinations.Add(routes[i].Destination);
                }
            }

            return (String[])destinations.ToArray(typeof(String));
        }

        // returns an arraylist of paths that are possible from the origin to dest.
        public ArrayList pathsForOriginDest(String origin, String dest)
        {
            GraphNode<String> originNode = (GraphNode<String>)__routeGraph.Nodes.FindByValue(origin);
            Stack<String> stack = new Stack<String>(1);
            ArrayList routes = new ArrayList(0);

            stack.Push(originNode.Value);

            findPath(origin, dest, originNode, null, ref stack, ref routes);

            return routes;
        }
        public ArrayList pathsForOriginDest(String origin, String dest, FlightTime start, String day, int tolerance)
        {
            ArrayList routes = pathsForOriginDest(origin, dest);
            ArrayList matchedRoutes = new ArrayList(0);

            foreach (String[] path in routes)
            {
                if (validPath(path, start, day, tolerance))
                {
                    matchedRoutes.Add(path);
                }
            }

            return matchedRoutes;
        }
        public ArrayList pathsForOriginDest(String origin, String dest, String day, int tolerance)
        {
            return pathsForOriginDest(origin, dest, new FlightTime(0, 0, false), day, tolerance);
        }

        // checks to see if a path is valid. used by route finding
        private bool validPath(String[] path, FlightTime start, String day, int tolerance)
        {
            Route rte;
            FlightTime currentTime = (FlightTime)start.Clone();
            String currentDay = day;
            Flight[] flts;

            for (int i = 0; i < (path.Length-1); i++)
            {
                rte = routesForParameters(path[i], path[i + 1])[0];
                if (rte.hasFlightsForTime(currentTime, currentDay, tolerance))
                {
                    flts = rte.flightsForTime(currentTime, currentDay, tolerance);
                    currentTime = (FlightTime)flts[0].Arrival.Clone();
                    if (currentTime.nextDay)
                    {
                        currentTime.nextDay = false;
                        currentDay = FlightTime.dayByAddingDays(day, 1);
                    }
                }
                else
                {
                    return false;
                }
            }

            return true;
        }

        #endregion

        // hunts for a patch and stores it in arraylist of routes.
        private void findPath(String origin, String dest, GraphNode<String> graphnode, GraphNode<String>previous, ref Stack<String> stak, ref ArrayList routes)
        {
            // one true condition
            if (graphnode.Value.ToLower().Equals(dest.ToLower()))
            {
                String[] path = stak.ToArray<String>();
                Array.Reverse(path);
                routes.Add(path);
                return;
            }

            // only can do this stuff if the current node is not the root node
            if (previous != null)
            {
                // check if its a cyclic graph back to the origin
                if (graphnode.Value.ToLower().Equals(origin.ToLower()))
                {
                    return;
                }

                // check if the current node only has on vertex back to its parent
                if ((graphnode.Neighbors.Count == 1) && (graphnode.Neighbors[0].Value.ToLower().Equals(previous.Value.ToLower())))
                {
                    return;
                }

                // deal with cyclic nodes by using history to detect. 
                // remove newly added node and root node in temp list
                // for detection

                List<String> temp = stak.ToList<String>();

                temp.RemoveAt(0);
                temp.RemoveAt((temp.Count - 1));
                
                if (temp.Contains(graphnode.Value))
                {
                    return;
                }
            }

            // checks to see if node has no connections to any other nodes
            if ((graphnode.Neighbors.Count == 0) && (!graphnode.Value.ToLower().Equals(dest.ToLower())))
            {
                return;
            }



            foreach (GraphNode<String> node in graphnode.Neighbors)
            {
                // push stack to maintain history
                stak.Push(node.Value);
                if ((previous == null) || (!node.Value.ToLower().Equals(previous.Value.ToLower())))
                {
                    findPath(origin, dest, node, graphnode, ref stak, ref routes);
                }
                stak.Pop();


            }
            return;
        }
    }
}
