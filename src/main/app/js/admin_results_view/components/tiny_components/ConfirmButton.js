var React = require('react');

var ConfirmButton = function(props){
    return (
        <button id={props.id} className="btn btn-default btn-sm floaters-right" onClick={props.onClick}>
            {props.text}
            <span className={props.spanClass}></span>
        </button>
    );
}

ConfirmButton.propTypes = {
    text: React.PropTypes.string.isRequired,
    onClick: React.PropTypes.func,
    spanClass: React.PropTypes.string.isRequired
}


module.exports = ConfirmButton;