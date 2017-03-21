var React = require('react');
var Validations = require('../../utils/Validations');
var NewPartyAsideFormComponent = require('../components/NewPartyAsideFormComponent');

var NewPartyAsideForm = React.createClass({
    getInitialState: function() {
        return ({ jsErrors: [], springErrors: [], partyName: "" });
    },
    componentWillReceiveProps: function(newProps) {
        if (newProps.springErrors != this.state.springErrors) {
            this.setState({ springErrors: newProps.springErrors })
        }
    },
    create: function(refs) {
        var file = refs.fileCSV.files[0];
        var errors = Validations.checkErrorsPartyAsideForm(this.state.partyName, file);

        if (errors.length > 0) {
            this.setState({ jsErrors: this.prepareJSerrors(errors, this.state.partyName), springErrors: [] });
        } else {
            var fd = new FormData();
            fd.append('file',file);
            refs.fileCSV.value = "";

            this.setState({ jsErrors: [], partyName: "" });
            this.props.create(fd, this.state.partyName);
        }
    },
    handleNameChange: function(e) {
        this.setState({ partyName: e.target.value });
    },
    prepareSpringErrors: function() {
        var errors = [];
        if (this.state.springErrors.length > 0) {
            errors = Validations.prepareSpringErrors(this.state.springErrors);
        }
        return errors;
    },
    prepareJSerrors: function(errors, name) {
        return Validations.prepareJSerrors(errors, "Klaida registruojant partiją " + name);
    },
    render: function() {
        return (
            <NewPartyAsideFormComponent
                name={this.state.partyName}
                changeName={this.handleNameChange}
                create={this.create}
                jsErrors={this.state.jsErrors}
                springErrors={this.prepareSpringErrors()}
                popupAlert={this.props.popupAlert}
            />
        );
    }
});

module.exports = NewPartyAsideForm;
