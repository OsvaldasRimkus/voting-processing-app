var React = require('react');
var axios = require('axios');
var Validations = require('../../utils/Validations');
var spring = require('../../config/SpringConfig');

var InlineEditComponent = React.createClass({
    getInitialState() {
        return ({
            newName: this.props.unit.name,
            jsErrors: [],
            springErrors: [],
        });
    },
    componentWiilReceiveProps(newProps) {
        if (newProps.unit.name != newName) this.setState({ newName: newProps.unit.name });
    },
    handleUnitUpdate() {
        var errors = Validations.checkErrorsUnitEditForm(this.state.newName);

        if (errors.length > 0) {
            this.setState({ jsErrors: errors });
        } else {
            var _this = this;
            var errors = [];
            var body = { name: this.state.newName };
            var patchUrl;

            if (this.props.parent === "PARTY") {
                patchUrl = spring.localHost.concat('/api/party/') + this.props.unit.id + '/update/'
            } else if (this.props.parent === "DISTRICT") {
                patchUrl = spring.localHost.concat('/api/district/') + this.props.unit.id + '/update/'
            }

            axios.patch(patchUrl, body)
                .then(function(resp) {
                    _this.setState({ springErrors: [], jsErrors: [] });
                    _this.props.update(resp.data);
                })
                .catch(function(err) {
                    console.log(err);
                    var finalErrors = [];
                    
                    if (err.response == undefined) {
                        errors.push("Tinklo klaida");
                    } else {
                        errors.push(err.response.data.rootMessage);
                        errors = errors.concat(err.response.data.errorsMessages);
                    }

                    _this.setState({ springErrors: errors, jsErrors: [] });
                });
        }
        
    },
    handleNameChange(e) {
        this.setState({ newName: e.target.value });
    },
    cancelUpdate() {
        this.setState({
            newName: this.props.unit.name,
            jsErrors: [],
            springErrors: []
        });
        this.props.toggleUpdate();
    },
    jsErrors() {
        var style={ marginTop: 10, display: 'block', clear: 'both' };
        return Validations.prepareJSerrors(this.state.jsErrors, "Klaida registruojant apylinkę", style)
    },
    springErrors() {
        return Validations.prepareSpringErrors(this.state.springErrors);
    },
    render() {
        var jsErrors = (this.state.jsErrors.length > 0) ? this.jsErrors() : [];
        var springErrors = (this.state.springErrors.length > 0) ? this.springErrors() : [];
        return (
            <div>
                <div style={{ display: 'table', width: '100%' }}>
                    <form className="inline-add-county-form" style={{ minHeight: 45, marginBottom: 0, display: 'table-cell' }}>
                        <div className="form-group" style={{ width: '40%' }}>
                            <input type="text"
                                id={"edit-district-name-field-" + this.state.newName}
                                onChange={this.handleNameChange}
                                className="form-control"
                                value={this.state.newName}
                                placeholder="Pavadinimas"
                            />
                        </div>
                        <div className="form-group">
                            <button id={"inline-confirm-btn-" + this.state.newName} className="btn btn-default btn-sm" onClick={this.handleUnitUpdate}>
                                Atnaujinti
                            </button>
                            <button id={"inline-cancel-btn-" + this.state.newName} className="btn btn-default btn-sm" onClick={this.cancelUpdate}>
                                Atšaukti
                            </button>
                        </div>
                    </form>
                </div>
                <div className="inline-form-errors">
                        {jsErrors}
                        {springErrors}
                </div>
            </div>
        );
    }
});

module.exports = InlineEditComponent;