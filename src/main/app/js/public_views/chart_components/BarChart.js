var React = require('react')
var ReactDOM = require('react-dom')
var ReactTransitionGroup = require('react-addons-transition-group')
var Chart = require('./Chart')
var d3 = require('d3')
var Bar = require('./Bar')
var Axis = require('./Axis')
var Grid = require('./Grid')
var Tooltip = require('./Tooltip')
var SortingFilter = require('./SortingFilter')


var BarChart = React.createClass({
    propTypes: {
            data: React.PropTypes.array.isRequired,
            metadata: React.PropTypes.object,
            width: React.PropTypes.number.isRequired,
            height: React.PropTypes.number.isRequired,
            showTooltip: React.PropTypes.bool.isRequired,
            showPercent: React.PropTypes.bool,
            padding: React.PropTypes.number,
            margin: React.PropTypes.object
    },
    getDefaultProps() {
        return {
            showPercent: false,
            padding: 0.1,
            margin: {top: 0, bottom: 0, left: 0, right: 0},
        }
    },
    getInitialState() {
        return {
            tooltip: {
                display: false,
                label: '',
                data: []
            }
        }
    },
    componentWillMount() {
        this.makeScales(this.props)
    },
    componentWillReceiveProps: function (newProps) {
        this.makeScales(newProps)
    },

    makeScales: function (props) {
        this.maxVal = d3.max(props.data, function(d) {return d.value})
        this.total = props.metadata.total || d3.sum(props.data, function(d) {return d.value})
        this.totalValid = props.metadata.valid || this.total

        this.yScale = d3.scaleBand()
                        .rangeRound([0, props.height])
                        .domain(props.data.map(function(d, i) {return d.key}))
                        .padding(this.props.padding)

        this.xScale = d3.scaleLinear().range([0, props.width])

        if (props.showPercent) {
            this.xScale.domain([0, this.maxVal * 1.1 / this.totalValid])
        } else {
            this.xScale.domain([0, this.maxVal * 1.1])
        }
    },
    showTooltip: function(e) {
        let value = e.target.getAttribute('data-value')
        
        let chartBox = ReactDOM.findDOMNode(this).getBoundingClientRect()
        let barBox = e.target.getBoundingClientRect()

        let x = e.clientX - barBox.left
        let y = barBox.top - chartBox.top     

        this.setState({
            tooltip:{
                display: true,
                label: e.target.getAttribute('data-key') + '',
                data: [
                    {key: 'surinkta balsų', value: value},
                    {key: 'nuo galiojančių', value: d3.format('.2%')(value / this.totalValid)},
                    {key: 'nuo visų', value: d3.format('.2%')(value / this.total)},
                ],
                barHeight: this.yScale.bandwidth(),
                pos: {
                    x: x,
                    y: y
                }
            }
        })
    },
    hideTooltip: function(e) {
        this.setState({
            tooltip: { 
                display: false,
                label: '',
                data: []
            }
        })
    },
    makeBar: function (data) {
        let percent = data.value / this.totalValid
        let label = this.props.showPercent ? d3.format('.2%')(percent) : data.value + ''
        let params = {
            data: data,
            label: label,
            x: 0,
            y: this.yScale(data.key),
            width: this.props.showPercent ? this.xScale(percent) : this.xScale(data.value),
            height: this.yScale.bandwidth(),
            onMouseEnter: this.props.showTooltip ? this.showTooltip : undefined,
            onMouseLeave: this.props.showTooltip ? this.hideTooltip : undefined,
            key: 'bar-' + data.key
        }
        return (
            <Bar {...params} />
        )
    },
    render: function() {

        let {showPercent, data, width, height, padding, margin } = this.props

        let bars = this.props.data.map(this.makeBar)
        let transform = 'translate(' + this.props.margin.left + ', ' + this.props.margin.top + ')'

        let xAxisParams = {
            className: 'axis x',
            x: 0,
            y: height,
            scale: this.xScale,
            orientation: 'bottom',
            ticks: 4,
            tickSize: 5,
            tickFormat: this.props.showPercent ? '.0%' : null
        }
        let yAxisParams = {
            className: 'axis y',
            x: 0,
            y: 0,
            scale: this.yScale,
            orientation: 'left',
            ticks: 4,
            tickSize: 0
        }

        return (
            <g transform={transform}>
                <ReactTransitionGroup component="g">
                    { this.props.width > 0 &&
                        bars
                    }
                </ReactTransitionGroup>
                <Axis {...xAxisParams} />
                <Axis {...yAxisParams} />
                { this.props.showTooltip &&
                    <Tooltip tooltip={this.state.tooltip}/>
                }
            </g>
        )
    }
})

module.exports = BarChart
