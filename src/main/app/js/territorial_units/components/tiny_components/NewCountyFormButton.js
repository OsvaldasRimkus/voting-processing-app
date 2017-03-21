var React = require('react');

function NewCountyFormButton(props) {
    if (props.districtName != null){
        return (
            <div>
            <span
                id={"add-new-county-button-" + props.districtName}
                onClick={props.renderCountyForm}
                style={{ cursor: 'pointer' }}>
                <span className="glyphicon glyphicon-plus"></span>
                &nbsp;PRIDĖTI APYLINKĘ
            </span>
            </div>
        )
    } else {
        return (
            <div>
            <span
                id="add-new-county-button"
                onClick={props.renderCountyForm}
                style={{ cursor: 'pointer' }}>
                <span className="glyphicon glyphicon-plus"></span>
                &nbsp;PRIDĖTI APYLINKĘ
            </span>
            </div>
        )
    }

}

module.exports = NewCountyFormButton;
