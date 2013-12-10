// Create a map in the "map" div, set te view to a given place and zoom
var map = L.map('map').setView([51.505, -0.09], 13);

// Add an OpenStreetMap tile layer
L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

// Add a marker in the given location, attach some popup content to it and open the popup
L.marker([51.5, -0.09]).addTo(map)
    .bindPopup('Click points to create a route <br> First clicked city is the start <br> limit currently at 50 points')
    .openPopup();

var $graphl_map_api = "localhost:3000";

// Statemachine Das Maps
var mapState = {

    // Populate the state to start the machine
    state: "first-point",
    
    // Array to contain a collection of nodes
    latlngs: [],
 
    // Variable to hold the nth line segment start
    nthLatLngStart: null,
    
    // Variable to hold the current polyline
    polyline: null,

    // @param<ACTION> transitions to the appropriate state based on ACTION
    transition: function(action) {

	return function(e) { 
	    if (action === "add-polyline-segment") {

		if (mapState.state === "first-point") {
		    mapState.polyline = L.polyline([e.latlng]).addTo(map);
		    mapState.latlngs.push(e.latlng);

		    mapState.state = "open-polyline";
		}
		else if (mapState.state === "open-polyline") {
		    mapState.polyline.addLatLng(e.latlng);
		    mapState.latlngs.push(e.latlng);

		    mapState.nthLatLngStart = e.latlng;      // Set start for nth point
		    mapState.state = "expecting-nth-point";
		}
		else if (mapState.state === "expecting-nth-point") {
		    mapState.polyline = L.polyline([mapState.nthLatLngStart]).addTo(map);
		    mapState.polyline.addLatLng(e.latlng);
		    mapState.latlngs.push(e.latlng);

		    mapState.state = "open-polyline";
		}
	    }
	    else if (action === "request-route") {
		 mapState.state = "requesting-route";
		 client.transition("request-route", e);
	    }
	    else if (action === "recieve-route" &&
		     e !== null) // e is data here
	    {
		mapState.state = "recieving-route";
		//draw
		mapState.state = "closed-polyline";
	    }
	}
    }
}

// Server DAS GRAPH QUANTS
// Client to communicate with graph server
// 
// 0.0.0 State is not currently used, but in the future when
// we have different read and write transitions this will be useful.
var client = {

    state: "waiting",

    transition: function(state, namespace, data) {
	client.state = state;
	$.post($graphl_map_api + "/nodes/" + namespace, data) 
	    .done(function(data) {
		client.callback(data);
		client.state = "waiting";
	    });
    },

    callback: function(data) {
	var receiveRoute = mapState.transition("recieve-route");
	recieveRoute(data);
    }
}

var addPolylineSegment = mapState.transition("add-polyline-segment");
var requestRoute = mapState.transition("request-route");


map.on('click', addPolylineSegment);
