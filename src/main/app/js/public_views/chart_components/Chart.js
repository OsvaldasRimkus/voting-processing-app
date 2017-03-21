var React = require('react');
var d3 = require('d3');
var SortingFilter = require('./SortingFilter')

var SortableBarChart = React.createClass({
    propTypes: {
        width: React.PropTypes.number.isRequired,
        height: React.PropTypes.number.isRequired,
        // margin: React.PropTypes.object.isRequired
    }, 
    getInitialState() {
        return {
            data: [],
            orderBy: 'abc'
        };
    },
    componentWillMount() {
        this.setState({
            data: this.props.data,
            orderBy: 'abc'
        })
    },
    sortData(data, orderBy) {
        if (orderBy === 'abc') {
            return data.sort(function(x, y) {
                return d3.ascending(x.key, y.key);
            })
        } else {
            return data.sort(function(x, y) {
                return d3.descending(x.value, y.value);
            })
        }
    },
    handleSortingOptionChange: function(e) {
        if (this.state.orderBy !== e.target.value) {
            this.setState({
                orderBy: e.target.value,
                data: this.sortData(this.state.data, e.target.value)
            });
        }
    },
    render: function() {
        console.log("RENDERING CHART")
        console.log(this.state.data)
        let { width, height, margin } = this.props
        // let transform = 'translate(' + margin.left + ',' + margin.top + ')'
        let transform = ''
        return (
            <div>
                <SortingFilter onChange={this.handleSortingOptionChange} orderBy={this.state.orderBy}/>
                <svg ref="svg" width={width} height={height}>
                    <g transform={transform}>
                        {this.props.children}
                    </g>
                </svg>
            </div>
        );
    }
});

module.exports = SortableBarChart;