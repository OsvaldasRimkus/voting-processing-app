var React = require('react');

function CountyDisplayComponent(props) {
    var display = (!props.show) ? {display: 'none'} : {};
    var smBtn = 'btn btn-default btn-sm';
    var mmBtn = 'btn btn-default btn-sm';
    var deleteBtn = undefined;
    var results = (props.smDisplay == undefined) ? "PASIRINKITE MANDATO TIPĄ" : "REZULTATŲ NĖRA";
    var confirmBtn = undefined;

    if (props.smDisplay == undefined) {
        // btns remain the same
    } else if (props.smDisplay) {
        smBtn += ' active';
    } else {
        mmBtn += ' active';
    }

    if (props.results != undefined) {
        deleteBtn = props.deleteBtn;
        confirmBtn = props.confirmBtn;
        results = props.results;
    }

    return (
        <div className="unit">
            <div className="list-group-item active location5">
                <div id={"county-tab-" + props.county.name + "-" + props.county.district.name} onClick={props.toggleShow} style={{ cursor: 'pointer' }}>
                    <strong>{props.county.name}</strong> (Apygarda: {props.county.district.name})
                    {props.allConfirmedBtn}
                </div>
            </div>
            <div style={ display }>
                <div className="list-group-item">
                    <button id={props.county.name + "-" + props.county.district.name + "-single-mandate"} onClick={props.displaySM} className={smBtn}>1-MANDATINIAI</button>
                    <button id={props.county.name + "-" + props.county.district.name + "-multi-mandate"} onClick={props.displayMM} className={mmBtn}>M-MANDATINIAI</button>
                    {deleteBtn}
                    {confirmBtn}
                </div>
                {results}
            </div>

        </div>
    );
}

module.exports = CountyDisplayComponent;
