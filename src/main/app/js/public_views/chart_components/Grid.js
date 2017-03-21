var React = require('react');
var ReactDOM = require('react-dom');
var d3 = require('d3');

var Grid = React.createClass({
    propTypes: {
        x: React.PropTypes.number,
        y: React.PropTypes.number,
        scale: React.PropTypes.func,
        orientation: React.PropTypes.oneOf(['left','top','right','bottom']),
        className: React.PropTypes.string,
        ticks: React.PropTypes.number
    }, 
    // componentWillMount: function () {
    //     // this.update_d3(this.props);
    // },
    // componentWillReceiveProps: function (newProps) {
    //     this.update_d3(newProps);
    // },
    componentDidUpdate: function () { 
        this.renderGrid(); 
    },
    componentDidMount: function () { 
        this.renderGrid(); 
    },
    renderGrid: function () {
        this.grid = d3.axisTop()
        this.grid
            .scale(this.props.scale)
            .ticks(this.props.ticks)
            .tickSize(this.props.length)
            .tickFormat("");
 
        var node = ReactDOM.findDOMNode(this);
        d3.select(node).call(this.grid);
    },
    render: function () {
        let translate = "translate(" + this.props.x + ", " + this.props.y + ")";
        return (
            <g className={this.props.className} transform={translate}>
            </g>
        );
    }
});


module.exports = Grid;
