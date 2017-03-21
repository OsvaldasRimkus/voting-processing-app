var React = require('react');
var styles = {"image": {width: 20, height: 20}}

var SearchCandidateComponent = React.createClass({
    showDetails: function() {
        this.props.showDetails(this.props.candidate);
    },
    render: function() {

        var partyName = (this.props.candidate.party == null) ? "Išsikėlęs pats" : this.props.candidate.party.name;
        return (
            <div className="unit">
                  <div className="list-group-item" style={{ cursor: "pointer" }} onClick={this.showDetails}>
                      <div style={{height: 20}}>
                        <div id={"candidate-searh-list-name-" + this.props.candidate.personId} className="col-md-5">
                            <img src="app/imgs/boss.png" style={ styles.image }/> &nbsp;
                            {this.props.candidate.firstName} &nbsp; {this.props.candidate.lastName}
                        </div>
                        <div id={"candidate-searh-list-party-dependency-" + this.props.candidate.personId} className="col-md-6">
                            <img src="app/imgs/political_party.png" style={ styles.image }/> &nbsp;
                            {partyName}
                        </div>
                        <div id={"candidate-searh-list-info-field-" + this.props.candidate.personId} className="col-md-1">----------</div>
                      </div>
                  </div>
            </div>
        );
    }
});

module.exports = SearchCandidateComponent;
