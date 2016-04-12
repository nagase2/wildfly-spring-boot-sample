$.fn.datechart = function(options) {
	var target = this;
	var id = "#" + target.attr("id");
	var item = options.item;
	var dimname = item + "_date";
	var title = options.title || "Date";
	var chartid = "date-chart";
	dashboard.dim[dimname] = dashboard.data.dimension(oh.utils.getdate(item));
	dashboard.groups[dimname] = dashboard.dim[dimname].group();
	var mydiv = $("<div/>").addClass("chart").attr("id", chartid);
	var titlediv = $("<div/>").addClass("title").appendTo(mydiv);
	$("<span/>").text(title).appendTo(titlediv);
	titlediv.append("&nbsp;");
	$("<a/>").addClass("reset").attr("href", "#")
			.attr("style", "display:none;").text("(reset)").appendTo(titlediv)
			.on("click", function(e) {
				e.preventDefault();
				mychart.filterAll();
				dc.redrawAll();
				return false
			});
	mydiv.appendTo(target);
	var mindate = oh.utils.getdate(item)(dashboard.dim[dimname].bottom(1)[0]);
	var maxdate = oh.utils.getdate(item)(dashboard.dim[dimname].top(1)[0]);
	mindate = new Date(mindate.getFullYear(), mindate.getMonth(), mindate
			.getDate() - 2);
	maxdate = new Date(maxdate.getFullYear(), maxdate.getMonth(), maxdate
			.getDate() + 2);
	var ndays = (maxdate - mindate) / (24 * 60 * 60 * 1e3);
	if (ndays < 71) {
		maxdate = new Date(mindate.getFullYear(), mindate.getMonth(), mindate
				.getDate() + 72)
	}
	if (ndays > 180) {
		mindate = new Date(maxdate.getFullYear(), maxdate.getMonth(), maxdate
				.getDate() - 181)
	}
	var ndays = Math.round((maxdate - mindate) / (24 * 60 * 60 * 1e3));
	var remainder = (750 - 30) % ndays;
	if (remainder > 90) {
		var remainder = (750 - 30) % Math.floor(ndays / 2)
	}
	var mychart = dc.barChart("#" + mydiv.attr("id")).width(750).height(130)
			.transitionDuration(200).margins({
				top : 10,
				right : remainder,
				bottom : 20,
				left : 30
			}).dimension(dashboard.dim[dimname]).group(
					dashboard.groups[dimname]).centerBar(false).gap(1)
			.elasticY(true).
			
			x(
					d3.time.scale().domain([ mindate, maxdate ]).rangeRound(
							[ ndays ]))
			.round(d3.time.day.round)
			.xUnits(d3.time.days)
			.renderHorizontalGridLines(true)
			.renderVerticalGridLines(true);
	dashboard.renderlet.init(mychart.renderlet);
	return target
}