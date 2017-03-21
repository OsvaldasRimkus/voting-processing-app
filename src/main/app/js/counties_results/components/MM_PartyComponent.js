var React = require('react');
const styles = {"image": {width: 20, height: 20}};

var MM_PartyComponent = React.createClass({
    changeVotes: function(e) {
        this.props.changeVotes(this.props.party.id, e.target.value);
    },
    render: function() {

        return (
            <div className="unit">
                  <div className="list-group-item ">
                    <div>
                      <div style={{height: "20px"}}>
                        <div id={"party-name-" + this.props.party.name} className="col-md-8">
                            <img src="app/imgs/political_party.png" style={ styles.image }/> &nbsp;
                            {this.props.party.name}
                        </div>
                        <div className="col-md-2" style={{ textAlign: 'right' }}><span>Balsai:</span></div>
                        <div className="col-md-2 candidate-votes-input">
                            <input
                                id={"party-votes-input-" + this.props.party.name}
                                type="text"
                                className="form-control county-results-input"
                                onChange={this.changeVotes}
                                value={this.props.votes}
                            />
                        </div>
                      </div>
                    </div>
                  </div>
            </div>
        );
    }
});

module.exports = MM_PartyComponent;
