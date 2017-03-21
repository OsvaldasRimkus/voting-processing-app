var React = require('react');

var SearchCandidatesHeaderComponent = React.createClass({
    render: function() {
        return (
            <div className="unit">
                  <div className="list-group-item location1">
                    <div>
                      <div style={{height: "20px"}}>
                        <div className="col-md-6">
                            Vardas Pavardė
                        </div>
                        <div className="col-md-5">
                            Partija
                        </div>
                        <div className="col-md-1">Info</div>
                      </div>
                    </div>
                  </div>
            </div>
        );
    }
});

module.exports = SearchCandidatesHeaderComponent;
