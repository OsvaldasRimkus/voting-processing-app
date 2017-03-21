var React = require('react');

function EmptySearchCandidateCardComponent(props) {
    return (
        <div className="well well-sm candidate-card-wide">
            <div className="row candidate-card-low">
                <div className="col-sm-8 col-md-12" style={{ textAlign: 'center' }}>
                    <p style={{ margin: '10px 0px' }}>
                        <span className="glyphicon glyphicon-apple" aria-hidden="true"></span>&nbsp;
                        Pasirinkite kandidatą, kad matyti jo informaciją
                    </p>
                </div>
            </div>
        </div>
    );
};

module.exports = EmptySearchCandidateCardComponent;
