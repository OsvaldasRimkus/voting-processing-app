var React = require('react');
var Validations = require('../../utils/Validations');

var InlineCsvUploadForm = React.createClass({
    getInitialState: function() {
        return ({ jsErrors: [], springErrors: [] });
    },
    componentWillReceiveProps: function(newProps) {
        if (newProps.springErrors != this.state.springErrors) {
            this.setState({ springErrors: newProps.springErrors })
        }
    },
    upload: function(e) {
        this.props.openModal();
        e.preventDefault();
        const file = this.refs.fileCSV.files[0];
        const errors = Validations.validateCsv(file);

        if (errors.length > 0) {
            this.setState({ jsErrors: Validations.prepareJSerrors(errors, "Failo klaida"), springErrors: [] });
        } else {
            const fd = new FormData();
            fd.append('file',file);
            if (this.state.springErrors == []) this.refs.fileCSV.value = "";
            this.setState({ jsErrors: [] });
            this.props.upload(fd, this.props.associationId);
        }
    },
    springErrors: function() {
        return Validations.prepareSpringErrors(this.props.springErrors, {marginBottom: 10});
    },
    render: function() {
        const springErrors = (this.props.springErrors.length > 0) ? this.springErrors() : [];
        return (
            <div>
                <form id="inline-csv-form">
                    <div className="form-group browse-button-area">
                        <input type="file" ref="fileCSV" id={"input-file-"+this.props.name}/>
                    </div>
                    <div className="form-group upload-button-area">
                        <button type="submit" onClick={this.upload} className="btn btn-primary btn-sm" id={"send-csv-file-button-" + this.props.name}>Siųsti</button>
                    </div>
                </form>
                <div className="inline-form-errors">
                    {this.state.jsErrors}
                    {springErrors}
                </div>
            </div>
        )
    }
});

module.exports = InlineCsvUploadForm;
