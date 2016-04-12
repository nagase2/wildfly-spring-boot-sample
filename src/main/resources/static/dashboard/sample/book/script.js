// Code goes here

var units = "DTs";

// var datafile = "testdata.csv";
// var datafile = "OneCustomer.csv";
var datafile = "Data_AR.csv";
// var datafile = "data_detail.csv";
var original_height = 1000;

var margin = {
	top : 10,
	right : 10,
	bottom : 10,
	left : 10
}



var formatNumber = d3.format(",.0f"), // zero decimal places
format = function(d) {
	return formatNumber(d) + " " + units;
}, color = d3.scale.category20();

// ツールチップ tooltip
var tooltip = d3.select("body").append("div").attr("class", "arrow_box") // ????
.style("position", "absolute").style("z-index", "10").style("visibility",
		"hidden")
// .text("a simple tooltip");



// set the sankey diagram properties



setFileName(datafile,original_height);


function update() {
	
	width = 1300 - margin.left - margin.right, height = original_height
	- margin.top - margin.bottom;
	
	var sankey = d3.sankey().nodeWidth(36).nodePadding(10).size([ width, height ]);
	var path = sankey.link(); // load the data
	
	d3.select("#svgfield").remove();
	
	// append the svg canvas to the page
	var svg = d3.select("#chart").append("svg")
			.attr("id","svgfield").attr("width",
			width + margin.left + margin.right).attr("height",
			height + margin.top + margin.bottom).append("g").attr("transform",
			"translate(" + margin.left + "," + margin.top + ")");
	
	d3.csv(datafile, function(error, data) {
		// set up graph in same style as original example but empty
		graph = {
			"nodes" : [],
			"links" : []
		};

		data.forEach(function(d) {
			graph.nodes.push({
				"name" : d.source
			});
			graph.nodes.push({
				"name" : d.target
			});

			graph.links.push({
				"source" : d.source,
				"target" : d.target,
				"value" : +d.value
			});
		});

		// thanks Mike Bostock
		// https://groups.google.com/d/msg/d3-js/pl297cFtIQk/Eso4q_eBu1IJ
		// this handy little function returns only the distinct / unique nodes
		graph.nodes = d3.keys(d3.nest().key(function(d) {
			return d.name;
		}).map(graph.nodes));

		// it appears d3 with force layout wants a numeric source and target
		// so loop through each link replacing the text with its index from node
		graph.links.forEach(function(d, i) {
			graph.links[i].source = graph.nodes.indexOf(graph.links[i].source);
			graph.links[i].target = graph.nodes.indexOf(graph.links[i].target);
		});

		// now loop through each nodes to make nodes an array of objects rather
		// than
		// an array of strings
		graph.nodes.forEach(function(d, i) {
			graph.nodes[i] = {
				"name" : d
			};
		});

		sankey.nodes(graph.nodes).links(graph.links).layout(22);

		// add in the links
		var link = svg.append("g").selectAll(".link").data(graph.links).enter()
				.append("path").attr("class", "link") // Specify mouse over
				// movement with CSS
				.attr("d", path).attr("title", "test tile").attr(
						"data-content", "content").attr("rel", "pop").style(
						"stroke-width", function(d) {
							return Math.max(1, d.dy);
						}).sort(function(a, b) {
					return b.dy - a.dy;
				}).on(
						"mouseover",
						function(d) {
							// Linkへのマウスオーバ
							console.log("log:", d.source.name, "  "
									+ d.target.name);
							console.log(d.value + " " + d.dy + " " + d.sy);
							// linkの座標
							tooltip.html("Source : "+ d.source.name + "<br>Target : "+ d.target.name+ "<br><span class='glyphicon glyphicon-exclamation-sign' style='vertical-align:middle;'></span>" + d.source.value + " "
									);
							tooltip.style("visibility", "visible");
							tooltip.style("top", (event.pageY - 10) + "px")
									.style("left", (event.pageX + 10) + "px");
						}).on(
						"mousemove",
						function() {
							tooltip.style("top", (event.pageY -70 ) + "px")
									.style("left", (event.pageX -65) + "px");
						}).on("mouseout", function() {
					tooltip.style("visibility", "hidden");
				});

		// add the link titles
		// link.append("title") .text(function(d) {
		// return "L:"+ d.source.name + " R:" + d.target.name + "\n" +
		// format(d.value); }); //ToolTip

		// add in the nodes
		var node = svg.append("g").selectAll(".node").data(graph.nodes).enter()
				.append("g").attr("class", "node").attr("transform",
						function(d) {
							return "translate(" + d.x + "," + d.y + ")";
						}).call(d3.behavior.drag().origin(function(d) {
					return d;
				}).on("dragstart", function() {
					this.parentNode.appendChild(this);
				}).on("drag", dragmove));

		// Nodeの設定はここ
		node.append("rect").attr("data-content", "recttile").attr("rel", "pop")
				.attr("class", "tip").attr("height", function(d) {
					return d.dy;
				}).attr("rx", 2).attr("ry", 2).on(
						"mouseover",
						function(d) {
							return svg.selectAll(".link").style(
									"stroke-opacity", changecol(d.name));
						}).attr("width", sankey.nodeWidth()).style("fill",
						function(d) {
							return d.color = color(d.name.replace(/ .*/, ""));
						}).style("stroke", function(d) {
					return d3.rgb(d.color).darker(2);
				}).append("title").text(function(d) {
					return d.name + "\n" + format(d.value);
				});
		// add in the title for the nodes
		node.append("text").attr("x", -6).attr("y", function(d) {
			return d.dy / 2;
		}).attr("dy", ".35em").attr("text-anchor", "end").attr("transform",
				null).text(function(d) {
			return d.name;
		}).filter(function(d) {
			return d.x < width / 2;
		}).attr("x", 6 + sankey.nodeWidth()).attr("text-anchor", "start");
		// the function for moving the nodes
		function dragmove(d) {
			d3.select(this).attr(
					"transform",
					"translate("
							+ (d.x = Math.max(0, Math.min(width - d.dx,
									d3.event.x)))
							+ ","
							+ (d.y = Math.max(0, Math.min(height - d.dy,
									d3.event.y))) + ")");
			sankey.relayout();
			link.attr("d", path);
		}
	});

	// Tooltip

	$('path').tooltip({
		'container' : 'svg',
		'placement' : 'top',
		trigger : 'hover'
	});
	$(".tip").tooltip({
		'container' : 'body',
		'placement' : 'top',
		trigger : 'hover'
	});
	$('body').popover({
		selector : '[rel=pop]',
		html : true,
		trigger : 'hover',
		placement : 'top'
	});
	$('body').popover({
		selector : '[class=link]',
		html : true,
		trigger : 'hover',
		placement : 'top'
	});

	$("svg rect").popover({
		'container' : 'body',
		'placement' : 'top',
		trigger : 'hover'
	});
	$('.node').popover({
		html : true
	});
}

// コールバック関数に引数を渡すための関数
function changecol(msg1) {
	return function(d) {
		// console.log(msg1);
		// console.log(d.source.name+d.target.name);
		if (msg1 == d.source.name || msg1 == d.target.name) {
			return 0.5;
		} else {
			return 0.1;
		}
	};
}
function setFileName(fname,height) {
	datafile = fname;
	original_height = height;
	update();
	console.log(datafile);
}
