var React = require('react');
var NewCountyAsideFormComponent = require('../../components/tiny_components/NewCountyAsideFormComponent');
var NewCountyFormButton = require('../../components/tiny_components/NewCountyFormButton');
var Validations = require('../../../utils/Validations');

var  NewCountyAsideFormContainer = React.createClass({
    getInitialState: function() {
        return ({ showAsideForm: false, countyName: "", voterCount: undefined, countyAddress: "" });
    },
    componentWillReceiveProps: function(newProps) {
        if (newProps.clearCountyForm) {
            this.setState({
                showAsideForm: false,
                countyName: "",
                voterCount: undefined,
                countyAddress: ""
            });
        }
    },
    handleSwitchState: function() {
        this.setState({ showAsideForm: !this.state.showAsideForm });
    },
    handleCountyCancel: function() {
      this.setState({
          showAsideForm: !this.state.showAsideForm,
          countyName: "",
          voterCount: undefined,
          countyAddress: ""
      });
      this.props.reportCountyErrors([]);
    },
    handleCountyAdd(e) {
        e.preventDefault();
        var lithuaniatedAddress = this.state.countyAddress.replace("Lithuania", "Lietuva");
        var errors = Validations.checkErrorsCountyForm(
            this.state.countyName,
            this.state.voterCount,
            lithuaniatedAddress
        );
        if (errors.length != 0) {
            this.props.reportCountyErrors(errors, this.state.countyName);
        } else {
            var body = {
                name: this.state.countyName,
                voterCount: this.state.voterCount,
                address: lithuaniatedAddress
            };
            this.props.addCounty(body);
            this.setState({ showAsideForm: false, countyName: "", voterCount: undefined, countyAddress: "" });
            this.props.reportCountyErrors([]);
        }
    },
    handleNameChange: function(e) {
        this.setState({ countyName: e.target.value});
    },
    handleVoterCountChange: function(e) {
        this.setState({ voterCount: e.target.value});
    },
    handleAddressChange: function(value) {
        this.setState({ countyAddress: value});
    },
    setSuggest: function(suggest) {
        this.setState({ countyAddress: suggest.label });
    },
    render: function() {
        if (this.state.showAsideForm) {
            return <NewCountyAsideFormComponent
                      cancel={this.handleCountyCancel}
                      add={this.handleCountyAdd}
                      changeName={this.handleNameChange}
                      changeCount={this.handleVoterCountChange}
                      changeAddress={this.handleAddressChange}
                      name={this.state.countyName}
                      count={this.state.voterCount}
                      address={this.state.countyAddress}
                      setSuggest={this.setSuggest}
                   />
        } else {
            return <NewCountyFormButton
                renderCountyForm={this.handleSwitchState}
            />
        }
    }
});

module.exports = NewCountyAsideFormContainer;
