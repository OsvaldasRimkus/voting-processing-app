var React = require('react');
var Helper = require('../../utils/Helper');

var CurrentTimeComponent = React.createClass({
		getInitialState: function() {
				return { time: '' };
		},
		componentWillMount: function() {
				setInterval(this.tick, 1000);
		},
		tick: function() {
				this.setState({ time: Helper.currentTime() });
		},
		render: function() {
				return (
						<div style={{color: 'white'}}>{this.state.time}</div>
				);
		}
});

module.exports = CurrentTimeComponent;