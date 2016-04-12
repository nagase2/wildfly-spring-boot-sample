$(function () {
    $('#container').highcharts({
        title: {
            text: ' '
        },
        xAxis: {
            categories: ['Thailand', 'Taiwan', 'Indonesia', 'Austraria', 'Malaysia', 'China', 'Japan', 'US', 'Brazil', 'AR']
        },
        labels: {
            items: [{
                //html: 'Total fruit consumption',
                style: {
                    left: '50px',
                    top: '18px',
                    color: (Highcharts.theme && Highcharts.theme.textColor) || 'black'
                }
            }]
        },
        series: [{
            type: 'column',
            name: 'M3',
            data: [32339, 39238, 23931, 33443, 33444,12343, 12433, 23431, 32344, 44343]
        },
/*        {
            type: 'column',
            name: 'Jane',
            data: [3, 2, 1, 3, 4]
        }, */
        {
            //type: 'spline', //ぐにゃっとなる
            name: '平均',
            data: [34444, 22344, 33434, 43343, 33333,20000, 10000, 23333, 33333, 23444],
            marker: {
                lineWidth: 2,
                lineColor: Highcharts.getOptions().colors[5],
                fillColor: 'white'
            }
        }]
    });
});

