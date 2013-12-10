// create a map in the "map" div, set te view to a given place and zoom
var map = L.map('map').setView([51.505, -0.09], 13);

// add an OpenStreetMap tile layer
L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

// add a marker in the given location, attach some popup content to it and open the popup
L.marker([51.5, -0.09]).addTo(map)
    .bindPopup('Click points to create a route <br> First clicked city is the start <br> limit currently at 50 points')
    .openPopup();

var $graphl_map_api = "localhost:3000";

// Statemachine Das Maps
var mapState = {

    state: "first-point",
    latlngs: [],
    nthLatLngStart: null,
    polyline: null,
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
	    else if (action === "request-weights") {
		mapState.state = "requesting-weights";
		client.transition("request-weights", mapState.latlngs);
	    }
	    else if (action === "recieve-weights" &&
		     e !== null) // e is data here
	    {
		mapState.state = "recieving-weights";
		mapState.latlngs = e;
		
		mapState.state = "closed-polyline";
	    }
	}
    }
}

// Server DAS GRAPH QUANTS
var client = {
    state: "waiting",
    transition: function(state, e, data) {
	client.state = state;
	$.post($graphl_map_api + "/route-finder", data) 
	    .done(function(data) {
		client.callback(data);
		client.state = "waiting";
	    });
    },
    callback: function(data) {
	var receiveWeights = mapState.transition("recieve-weights");
	recieveWeights(data);
    }
}

var addPolylineSegment = mapState.transition("add-polyline-segment");
var requestWeights = mapState.transition("request-weights");

map.on('click', addPolylineSegment);
$('#request').on('click', requestWeights);
