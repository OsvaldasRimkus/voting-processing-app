var React = require('react');

function ErrorWrapper(props) {
    return (
      <div className="alert alert-danger error" style={ props.inlineStyle }>
          <small>{props.message}</small>
      </div>
    );
}

module.exports = ErrorWrapper;
