var React = require('react');

function AddedCountyDisplayComponent(props) {
    var remove = function() { props.remove((props.index)) };
    return (
        <div style={{ padding: 3 }}>
            <p id={"remove-county-" + props.county.name} style={{ float: 'right', clear: 'both', cursor: 'pointer' }} onClick={remove}>
                <span className="glyphicon glyphicon-remove-sign">
                </span> &nbsp; Pašalinti
            </p>
            <p style={{ }}>
                <span className="glyphicon glyphicon-list-alt"></span>&nbsp;{props.county.name}
            </p>
            <p style={{ }}>
                <span className="glyphicon glyphicon-user"></span>&nbsp;{props.county.voterCount}
            </p>
            <p style={{ }}>
                <span className="glyphicon glyphicon-map-marker"></span>&nbsp;{props.county.address}
            </p>
            <p style={{ }}>
                <hr id="hr-narrow-around"/>
            </p>

        </div>
    )
}

module.exports = AddedCountyDisplayComponent;
