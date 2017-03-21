var React = require('react');

function AdminViewCandidateComponent(props) {
    return (
        <div className="list-group-item">
            <div className="row narrowed">
                <div className="col-md-4">
                    {props.firstName}
                    &nbsp;
                    {props.lastName}
                </div>
                <div className="col-md-3">
                    {props.partyName}
                </div>
                <div className="col-md-3">
                    Surinkti balsai:
                </div>
                <div className="col-md-2">
                    {props.votes}
                </div>
            </div>
        </div>
    );
}

module.exports = AdminViewCandidateComponent;
