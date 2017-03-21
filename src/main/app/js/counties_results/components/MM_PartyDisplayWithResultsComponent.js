var React = require('react');

var MM_PartyDisplayWithResultsComponent = React.createClass({
    render: function() {
        return (
          <div className="unit">
                <div className="list-group-item location6">
                  <div>
                    <div style={{height: "20px"}}>
                      <div className="col-md-8">
                          <img src="app/imgs/political_party.png" style={{ width: 20, height: 20 }}/> &nbsp;
                          {this.props.party.name}
                      </div>
                      <div className="col-md-2" style={{ textAlign: 'right' }}><span>Balsai:</span></div>
                      <div className="col-md-2">{this.props.voteCount}</div>
                    </div>
                  </div>
                </div>
          </div>
        );
    }
});

module.exports = MM_PartyDisplayWithResultsComponent;
