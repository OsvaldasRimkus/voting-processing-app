var React = require('react')
var CountyDetailsCard = require('./CountyDetailsCard')
var VoteListComponent = require('./VoteListComponent')

var ResultsDisplayComponent = React.createClass({

    render: function() {
        const { header, representative, results, createdOn, confirmedOn } = this.props;
        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-8 units-list-area">
                        <div className="list-group-item active location6">
                            <span>{header}</span>
                        </div>
                        <div className="list-group-item">
                            <p className="county-results">
                                Sugadintų biuletinių skaičius: &nbsp; {results.spoiledBallots}
                            </p>
                        </div>
                        <div className="list-group-item" style={{ height: 'auto' }}>
                            <VoteListComponent voteList={results.votes}/>
                        </div>
                    </div>

                    <div className="col-md-4 units-create-area">
                        <div className="col-md-11">
                            <CountyDetailsCard representative={representative} />
                            {createdOn}
                            {confirmedOn}
                        </div>
                    </div>

                </div>
            </div>
        );
    }
});

module.exports = ResultsDisplayComponent;
