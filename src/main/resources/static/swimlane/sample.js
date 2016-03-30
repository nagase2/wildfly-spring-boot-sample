	var test = { };
	test.scale = 1;
	test.translate = [0,0];
	test.brushing = false;
	test.shiftKey = false;

	$("#graph-view").html('');
	var chartWidth = $("#graph-view").width();
	var chartHeight = $("#graph-view").height();

	var graph = {"nodes":[{"size":10},{"size":5},{"size":2},{"size":3},{"size":30},{"size":40}],"links":[{"source":0,"target":1},{"source":0,"target":2},{"source":1,"target":0},{"source":3,"target":0},{"source":4,"target":1}]};

	test.force = d3.layout.force()
		.nodes(graph.nodes)
		.links(graph.links)
		.size([chartWidth, chartHeight])
		.linkDistance(50)
		.charge(-300)
		.on("tick", onTick)
		.start();

	var x_scale = d3.scale.linear().domain([0, chartWidth]).range([0, chartWidth]);
	var y_scale = d3.scale.linear().domain([0, chartHeight]).range([0, chartHeight]);

	test.zoom = d3.behavior.zoom()
		.scaleExtent( [0.1, 10] )
		.x( x_scale ).y( y_scale )
		.translate( test.translate )
		.scale( test.scale )
		.on( "zoom", onZoom );

	test.drag = d3.behavior.drag()
		.on( "dragstart", onDragstart )
		.on( "drag",      onDragmove );

	test.svg = d3.select("#graph-view")
		.append( "svg:svg" )
			.attr( "width", chartWidth )
			.attr( "height", chartHeight )
			.style( "background", "orange" )
		.append( "svg:g" );

	// rectangle behind the graph
	var rect = test.svg
		.append( "svg:rect" )
			.attr( "width", chartWidth)
			.attr( "height", chartHeight)
			.style( { "fill": "transparent", "cursor": "move" } )
		.call(test.zoom);

	test.container = test.svg
		.append( "svg:svg" )
			.attr( "width", chartWidth)
			.attr( "height", chartHeight)
			.style( "outline", "1px solid red" )
		.append( "svg:g" )
			.attr( "transform", "translate(" + test.translate + ") scale(" + test.scale + ")" );

	test.brush = test.svg
		.append("g")
			.datum( function() { return { selected: false, previouslySelected: false }; } )
			.attr( "class", "brush" );

	test.link = test.container.selectAll(".link")
		.data(test.force.links())
		.enter().append("line")
		.attr( "class", "link" );

	test.node = test.container.selectAll(".node")
		.data(test.force.nodes())
		.enter().append("circle")
		.attr( "class", "node" )
		.attr( "r", 5 )
		.call( test.drag )
		.on( "mouseover", onMouseOver )
		.on( "mouseout", onMouseOut );

	function onTick()
	{
		test.link
			.attr( "x1", function (d) { return d.source.x; })
			.attr( "y1", function (d) { return d.source.y; })
			.attr( "x2", function (d) { return d.target.x; })
			.attr( "y2", function (d) { return d.target.y; });

		test.node
			.attr( "transform", function (d) { return "translate(" + d.x + "," + d.y + ")"; } );
	}

	function onMouseOver() { d3.select(this).attr( "r", 10 ); }

	function onMouseOut() { d3.select(this).attr( "r", 5 ); }

	function nudgeSelection(dx, dy)
	{
		test.node.filter( function(d) { return d.selected; } )
				.attr( "transform", function (d)
				{
					d.x += dx; d.y += dy;
					return "translate(" + d.x + "," + d.y + ")";
				} );

		test.link.filter( function(d) { return d.source.selected; } )
				.attr( "x1", function(d) { return d.source.x; } )
				.attr( "y1", function(d) { return d.source.y; } );

		test.link.filter( function(d) { return d.target.selected; } )
				.attr( "x2", function(d) { return d.target.x; } )
				.attr( "y2", function(d) { return d.target.y; } );
	}

	test.onKeydown = function(event)
	{
		switch (event.keyCode)
		{
			case 16: test.shiftKey = true; break;

			case 38: nudgeSelection( 0, -1); break; // UP
			case 40: nudgeSelection( 0,  1); break; // DOWN
			case 37: nudgeSelection(-1,  0); break; // LEFT
			case 39: nudgeSelection( 1,  0); break; // RIGHT
		}
	}

	test.onKeyup = function(event)
	{
		switch (event.keyCode) { case 16: test.shiftKey = false; break; } 
	}

	function onDragstart( d, i ) { test.force.stop(); }

	function onDragmove( d, i )
	{
		d.px += d3.event.dx;
		d.py += d3.event.dy;
		d.x += d3.event.dx;
		d.y += d3.event.dy;

		onTick();
	}

	function onZoom()
	{
		test.container.attr( "transform", "translate(" + d3.event.translate + ") scale(" + d3.event.scale + ")" );
	}

function toggleBrushing()
{
console.log("toggle brushing.")

	test.brushing = ! test.brushing;

	if (test.brushing)
	{
		test.brush
			.call( d3.svg.brush()
				.x(test.zoom.x())
				.y(test.zoom.y())

				.on( "brushstart", function(d) {
					test.node.each( function(d) { d.previouslySelected = test.shiftKey && d.selected; } );
				} )

				.on( "brush", function() {
					var extent = d3.event.target.extent();
					test.node.classed( "selected", 
						function(d) {
							return d.selected = d.previouslySelected ^
								(extent[0][0] <= d.x && d.x < extent[1][0] &&
								 extent[0][1] <= d.y && d.y < extent[1][1]);
					});
				} )

				.on( "brushend", function() {
					d3.event.target.clear();
					d3.select(this).call(d3.event.target);
				} )
			);
console.log("Currently in Brush mode. Click to switch to Pan/Zoom mode.")

		$("#brushOption").html("Currently in Brush mode. Click to switch to Pan/Zoom mode.");
	}
	else
	{
  
		// clear the brush
		d3.selectAll(".brush").remove();

		// init it again
		test.brush = test.svg
			.append("g")
				.datum( function() { return { selected: false, previouslySelected: false }; } )
				.attr( "class", "brush" );

		test.node.classed("selected", false);
console.log("Currently in Pan/Zoom mode. Click to switch to Brush mode.")
		$("#brushOption").html("Currently in Pan/Zoom mode. Click to switch to Brush mode.");
	}
}
//クリックされたら
$( "#brushOption" ).click( function() { toggleBrushing(); } );

window.addEventListener ("keydown",	test.onKeydown, false);
window.addEventListener ("keyup",	test.onKeyup, false);
