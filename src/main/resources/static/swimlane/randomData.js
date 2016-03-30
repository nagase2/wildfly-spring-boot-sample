(function() {

    "use strict";

	var RandomData = function() {

		var addToLane = function (chart, item) {
		    var name = item.lane;

		    if (!chart.lanes[name])
		        chart.lanes[name] = [];

		    var lane = chart.lanes[name];
		    
		    var sublane = 0;
		    while(isOverlapping(item, lane[sublane]))
		    	sublane++;

		    if (!lane[sublane]) {
		    	lane[sublane] = [];
			}
		    
		    lane[sublane].push(item);	
		};

		var isOverlapping = function(item, lane) {
			if (lane) {
		        for (var i = 0; i < lane.length; i++) {
		            var t = lane[i];
		            if (item.start < t.end && item.end > t.start) {
		                return true;                      
		            }
		        }
			}
		    return false;
		};

        var parseData = function (data) {
            var i = 0, length = data.length, node;
            chart = { lanes: {} };
            
            for (i; i < length; i++) {
                var item = data[i];

                addToLane(chart, item);


            }

            return collapseLanes(chart);
        };

        var collapseLanes = function (chart) {
        	var lanes = [], items = [], laneId = 0;
        	var now = new Date();

        	for (var laneName in chart.lanes) {
        		var lane = chart.lanes[laneName];

        		for (var i = 0; i < lane.length; i++) {
        			var subLane = lane[i];

        			lanes.push({
	        			id: laneId, 
	        			label: i === 0 ? laneName : ''
	        		});

	        		for (var j = 0; j < subLane.length; j++) {
	        			var item = subLane[j];

	        			items.push({
	        				id: item.id,
	        				lane: laneId,
	        				start: item.start,
	        				end: item.end,
	        				class: item.end > now ? 'future' : 'past',
	        				desc: item.desc
	        			});
	        		}

	        		laneId++;
        		}
        	}

        	return {lanes: lanes, items: items};
        }

		var randomNumber = function(min, max) {
			return Math.floor(Math.random(0, 1) * (max - min)) + min;
		};

		var generateRandomWorkItems = function () {
			var data = [];
			var laneCount = randomNumber(5,7)
			  , totalWorkItems = randomNumber(220,400) //データの数
			  , startMonth = randomNumber(0,1)
			  , startDay = randomNumber(12,28)
			  , totalMonths = randomNumber(1,2);

			for (var i = 0; i < laneCount; i++) {
				var dt = new Date(2016, startMonth, startDay);
				for (var j = 0; j < totalWorkItems; j++) {
					//開始時間
					var dtS = new Date(dt.getFullYear(), dt.getMonth(), dt.getDate() + randomNumber(1,2), randomNumber(8, 16), 0, 0);

					var dateOffset = 0; // randomNumber(0,7);
					
					//終了時間
					var dt = new Date(dtS.getFullYear(), dtS.getMonth(), dtS.getDate() + dateOffset, dtS.getHours()+randomNumber(1, 6), randomNumber(1, 59), 0);

					var workItem = {
						id: i * totalWorkItems + j,
						name: 'work item xxx ' + j,
						lane: '会議室 ' + i,
						start: dtS,
						end: dt,
						desc: 'This is a description.'
					};

					data.push(workItem);
				}
			}
			return data;
		};

		return parseData(generateRandomWorkItems());
	};

    /**
    * Allow library to be used within both the browser and node.js
    */
    var root = typeof exports !== "undefined" && exports !== null ? exports : window;
    root.randomData = RandomData;

}).call(this);
