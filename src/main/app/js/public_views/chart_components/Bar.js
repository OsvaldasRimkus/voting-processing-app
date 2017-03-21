var React = require('react');
var ReactDOM = require('react-dom');
var d3 = require('d3');

var Bar = React.createClass({
    propTypes: {
        data: React.PropTypes.object.isRequired,
        label: React.PropTypes.string,
        x: React.PropTypes.number.isRequired,
        y: React.PropTypes.number.isRequired,
        width: React.PropTypes.number.isRequired,
        height: React.PropTypes.number.isRequired,
        onMouseEnter: React.PropTypes.func,
        onMouseLeave: React.PropTypes.func
    }, 
    getInitialState() {
        return { x: this.props.x, y: 0 }
    },
    componentWillMount() {
        this.transition = d3.transition()
           .duration(1000)
           .ease(d3.easeCubicInOut)
    },
    componentWillReceiveProps(nextProps) {
        if (this.state.y != nextProps.y) {
            let node = d3.select(ReactDOM.findDOMNode(this));

            node.transition(this.transition)
                .attr('transform', this.computeTranslate(nextProps.x, nextProps.y))
                .on('end', () => this.setState({y: nextProps.y}));
        }
    },
    computeTranslate(x, y) {
        return 'translate(' + x + ',' + y + ')'
    },
    shouldComponentUpdate(nextProps, nextState) {
        return this.state.y != nextState.y
    },
    render() {
        let { data, label, x, y, width, height, onMouseEnter, onMouseLeave } = this.props;
        let translate = this.computeTranslate(this.state.x, this.props.y)

        return (
            <g transform={translate} className='bar'>
                <rect 
                    onMouseEnter = {onMouseEnter}
                    onMouseLeave = {onMouseLeave}
                    data-key = {data.key}
                    data-value = {data.value}
                    width = {width}
                    height = {height}>
                </rect>
                { label !== '' && label != null && this.props.width > 50
                    && 
                    <text 
                        fill = 'white'
                        textAnchor = 'end'
                        dominantBaseline = 'middle'
                        x = {width - 8 }
                        y = {height / 2}>
                        {label}
                    </text>
                }
            </g>
        );
    }
});

module.exports = Bar;