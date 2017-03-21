var React = require('react');
var styles = {
    "image": {width: 20, height: 20}
};

var VoteListRow = React.createClass({
    propTypes: {
        vote: React.PropTypes.object.isRequired
    }, 
    render: function() {
        let vote = this.props.vote;
        let unitHtml;
        if (vote.candidate) {
            let candidate = vote.candidate
            let partyName = (candidate.partyName == undefined) ? "Išsikėlęs pats" : candidate.partyName;
            unitHtml =  <div className="col-md-8">
                            <div className="col-md-6">
                                <img src="app/imgs/boss.png" style={ styles.image }/> 
                                &nbsp; {candidate.firstName} &nbsp; {candidate.lastName}
                            </div>
                            <div className="col-md-6">
                                <img src="app/imgs/political_party.png" style={ styles.image }/> &nbsp;
                                {(candidate.partyName == undefined) ? "Išsikėlęs pats" : candidate.partyName}
                            </div>
                        </div>
        } else {
            unitHtml =  <div className="col-md-8">
                            <img src="app/imgs/political_party.png" style={ styles.image }/> &nbsp;
                            {vote.party.name}
                        </div>
        }
        return (
                <div className="list-group-item">
                    <div className="row narrowed whitened">
                        <div>
                            <div style={{height: 20}}>
                                {unitHtml}
                                <div className="col-md-2" style={{ textAlign: 'right' }}>
                                    <span>Balsai:</span>
                                </div>
                                <div className="col-md-2">{vote.voteCount}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

        );
    }
});

module.exports = VoteListRow;