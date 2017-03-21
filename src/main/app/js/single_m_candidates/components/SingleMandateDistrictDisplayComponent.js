var React = require('react');

function SingleMandateDistrictDisplayComponent(props) {
    return (
        <div className="unit">
            <div className="list-group-item active location2" style={{position: "relative", zIndex: "0" }}>
                <div id={"district-title-" + props.name} onClick={props.toggleShow} style={{ cursor: 'pointer' }}>
                    {props.name}
                </div>
            </div>
            <div style={ props.display }>
                <div className="list-group-item">
                    {props.actions}
                </div>
                {props.candidates}
            </div>
        </div>
    );
}

module.exports = SingleMandateDistrictDisplayComponent;
