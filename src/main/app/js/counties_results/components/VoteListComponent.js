var React = require('react');
var VoteListRow = require('./VoteListRow')

var VoteListComponent = React.createClass({
    propTypes: {
        voteList: React.PropTypes.arrayOf(React.PropTypes.object).isRequired
    },
    render: function() {
        let rows = this.props.voteList.map((vote, idx) => { 
            return <VoteListRow key={idx} vote={vote} /> 
        })

        return (
            <div>{rows}</div>
        );
    }
});

module.exports = VoteListComponent;