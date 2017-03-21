var React = require('react');
var ResultsFormComponent = require('./ResultsFormComponent');
var RepresentativeCard = require('./CountyDetailsCard');
var Validations = require('../../utils/Validations');

var CountyResultsComponent = React.createClass({
    getInitialState: function() {
        return ({ jsErrors: [] });
    },
    submitResults: function(e) {
        e.preventDefault();

        var errors = Validations.checkErrorsResultForm(this.props.dictionary, this.props.spoiled, this.props.representative.county.voterCount);
        if (errors.length > 0) {
            this.setState({ jsErrors: errors });
        } else {
            this.setState({ jsErrors: [] });
            this.props.submitResults();
        }
    },
    clearForm: function() {
        this.setState({ jsErrors: [] });
        this.props.clearForm();
    },
    prepareJSerrors: function() {
        return Validations.prepareJSerrors(this.state.jsErrors, "Klaida rezultatuose", {marginTop: 15});
    },
    prepareSpringErrors: function() {
        return Validations.prepareSpringErrors(this.props.springErrors, {marginTop: 10});
    },
    render: function() {
        var jsErrors = (this.state.jsErrors.length > 0) ? this.prepareJSerrors() : [];
        var springErrors = (this.props.springErrors.length > 0) ? this.prepareSpringErrors() : [];

        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-8 units-list-area">
                        <ResultsFormComponent
                            header={this.props.header}
                            spoiled={this.props.spoiled}
                            changeSpoiled={this.props.changeSpoiled}
                            votees={this.props.votees}
                        />
                    </div>
                    <div className="col-md-4 units-create-area" style={{ textAlign: 'center' }}>
                        <div className="col-md-12">
                            <RepresentativeCard representative={this.props.representative} />
                            <div style={{ marginTop: 30 }}>
                                <button id="send-county-results-button" className="btn btn-default btn-md county-results-form-btns" onClick={this.submitResults}>
                                    SIŲSTI REZULTATUS
                                </button>
                                <button id="clear-form-button" className="btn btn-default btn-md county-results-form-btns" onClick={this.clearForm}>
                                    IŠVALYTI FORMĄ
                                </button>
                            </div>
                            {jsErrors}
                            {springErrors}
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});

module.exports = CountyResultsComponent;
