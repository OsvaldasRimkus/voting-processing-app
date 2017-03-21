var React = require('react');

function RootErrorWrapper(props) {
    return (
      <div className="alert alert-danger root-error" style={props.inlineStyle}>
          <span className="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
          <small className="root-message">&nbsp;{props.message}</small>
          {props.children}
      </div>
    );
}

module.exports = RootErrorWrapper;
