var React = require('react');
var ReactDOM = require('react-dom');
var d3 = require('d3');

var Axis = React.createClass({
    propTypes: {
        x: React.PropTypes.number.isRequired,
        y: React.PropTypes.number.isRequired,
        scale: React.PropTypes.func.isRequired,
        orientation: React.PropTypes.oneOf(['left','top','right','bottom']).isRequired,
        className: React.PropTypes.string,
        ticks: React.PropTypes.number,
        tickSize: React.PropTypes.number, 
        tickPadding: React.PropTypes.number,
        tickFormat: React.PropTypes.string
    }, 
    getDefaultProps() {
        return {
            tickSize: 0,
            tickPadding: 10,
            tickFormat: null
        };
    },
    componentWillMount: function () {
        this.update_d3(this.props);
    },
    componentWillReceiveProps: function (newProps) {
        this.update_d3(newProps);
    },
    componentDidUpdate: function () { 
        this.renderAxis(); 
    },
    componentDidMount: function () { 
        this.renderAxis(); 
    },
    renderAxis: function () {
        let node = ReactDOM.findDOMNode(this);
        d3.select(node).call(this.axis);
    },
    update_d3: function (props) {
        switch(props.orientation) {
            case 'top':
                this.axis = d3.axisTop(props.scale)
                break;
            case 'right':
                this.axis = d3.axisRight(props.scale)
                break;
            case 'left':
                this.axis = d3.axisLeft(props.scale)
                break;
            case 'bottom':
                this.axis = d3.axisBottom(props.scale)
                break;        
        }
        this.axis
            .ticks(props.ticks)
            .tickSize(props.tickSize)
            .tickPadding(props.tickPadding)
        if (props.tickFormat) {
            this.axis.tickFormat(d3.format(props.tickFormat));
        }
    },
    render: function () {
        let translate = 'translate(' + this.props.x + ', ' + this.props.y + ')';
        return (
            <g className={this.props.className} transform={translate}>
            </g>
        );
    }
});


module.exports = Axis;
