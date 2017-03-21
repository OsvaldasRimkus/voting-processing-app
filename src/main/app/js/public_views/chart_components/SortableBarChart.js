var React = require('react');
var d3 = require('d3');
var SortingFilter = require('./SortingFilter')
var BarChart = require('./BarChart')

var SortableBarChart = React.createClass({
    propTypes: {
        width: React.PropTypes.number.isRequired,
        height: React.PropTypes.number.isRequired,
        orderBy: React.PropTypes.string,
        data: React.PropTypes.arrayOf(React.PropTypes.object).isRequired,
        metadata: React.PropTypes.object,
        showPercent: React.PropTypes.bool.isRequired,
        showTooltip: React.PropTypes.bool.isRequired,
        width: React.PropTypes.number.isRequired,
        height: React.PropTypes.number.isRequired,
        margin: React.PropTypes.object.isRequired,
        padding: React.PropTypes.number.isRequired
    }, 
    getDefaultProps() {
        return {
            orderBy: 'value'
        }
    },
    getInitialState() {
        return {
            data: [],
            metadata: { valid: 0, total: 0 },
            orderBy: 'value'
        };
    },
    componentWillMount() {
        this.computeInnerDimensions(this.props);
        this.setState({
            data: this.sortData(this.props.data, this.props.orderBy),
            metadata: this.props.metadata,
            orderBy: this.props.orderBy
        })
    },
    componentWillReceiveProps(newProps) {
        this.computeInnerDimensions(newProps);
        this.setState({
            data: this.sortData(newProps.data, newProps.orderBy),
            metadata: newProps.metadata,
            orderBy: newProps.orderBy
        })
    },
    computeInnerDimensions(props) {
        this.innerWidth = props.width - props.margin.left - props.margin.right
        this.innerHeight = props.height - props.margin.top - props.margin.bottom
    },
    sortData(data, orderBy) {
        if (orderBy === 'key') {
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

        let { data, metadata, showPercent, showTooltip, width, height, margin, padding } = this.props

        let params = {
            data: data,
            metadata: metadata,
            showPercent: showPercent,
            showTooltip: showTooltip,
            width: this.innerWidth,
            height: this.innerHeight,
            margin: margin,
            padding: 0.1
        }

        return (
            <div>
                <SortingFilter onChange={this.handleSortingOptionChange} orderBy={this.state.orderBy}/>
                <svg ref="svg" width={width} height={height}>
                    <BarChart {...params} />
                </svg>
            </div>
        );
    }
});

module.exports = SortableBarChart;