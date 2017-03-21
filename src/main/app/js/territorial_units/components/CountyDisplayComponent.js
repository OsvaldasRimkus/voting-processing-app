var React = require('react');
var axios = require('axios');
var ReactRouter = require('react-router');
var Link = ReactRouter.Link;
var truncate = require('truncate');
var ConfirmAction = require('../../components/tiny_components/ConfirmAction');
var NewCountyInlineForm = require('../components/tiny_components/NewCountyInlineForm');
var Validations = require('../../utils/Validations');
var spring = require('../../config/SpringConfig');
var truncateLength = 30;

var CountyDisplayComponent = React.createClass({
    getInitialState() {
        return ({
            newName: this.props.unit.name,
            newCount: this.props.unit.voterCount,
            newAddress: this.props.unit.address,
            jsErrors: [],
            springErrors: [],
            update: false
        });
    },
    handleCountyUpdate() {
        var _this = this;
        var errors = [];
        var body = {
            name: this.state.newName,
            voterCount: this.state.newCount,
            address: this.state.newAddress
        };
        var postUrl = spring.localHost.concat('/api/county/') + this.props.unit.id;
        axios.post(postUrl, body)
            .then(function(resp) {
                _this.setState({ springErrors: [], update: false });
                _this.props.updateCountiesState(resp.data, _this.props.index);
            })
            .catch(function(err) {
                console.log(err);
                var finalErrors = [];
                
                if (err.response == undefined) {
                    finalErrors.push("Tinklo klaida");
                } else {
                    errors.push(err.response.data.rootMessage);
                    errors.concat(err.response.data.errorsMessages);
                }

                _this.setState({ springErrors: finalErrors });
            });
    },
    handleNameChange(e) {
        this.setState({ newName: e.target.value });
    },
    handleVoterCount(e) {
        this.setState({ newCount: e.target.value });
    },
    handleAddressChange(value) {
        this.setState({ newAddress: value });
    },
    setSuggest(suggest) {
        this.setState({ newAddress: suggest.label });
    },
    cancelUpdate() {
        this.setState({
            update: !this.state.update,
            newName: this.props.unit.name,
            newCount: this.props.unit.voterCount,
            newAddress: this.props.unit.address,
            jsErrors: [],
            springErrors: []
        });
    },
    toggleUpdate() {
        this.setState({ update: !this.state.update });
    },
    popupAddress(e) {
        if (this.props.unit.address.length > truncateLength) {
            $("." + e.target.className).popover({ trigger: "click" })
        };
    },
    popup() { $('.popoverCounty').popover({ trigger: "hover" }) },
    handStyle() {
        return (this.props.unit.address.length > truncateLength) ? {cursor: 'pointer'} : {};
    },
    render() {
        var updateOrDisplay;
        if (this.state.update) {
            updateOrDisplay = (
                <NewCountyInlineForm
                    cancel={this.cancelUpdate}
                    submit={this.handleCountyUpdate}
                    changeName={this.handleNameChange}
                    changeVoterCount={this.handleVoterCount}
                    changeAddress={this.handleAddressChange}
                    setSuggest={this.setSuggest}
                    name={this.state.newName}
                    count={this.state.newCount}
                    address={this.state.newAddress}
                    springErrors={this.state.springErrors}
                    btnName="Atnaujinti"
                    districtName={this.props.unit.name}
                />
            ); 
        } else {
            updateOrDisplay = (
                <div>
                    <p style={{ display: 'inline-block', width: '30%' }} id={"county-title-" + this.props.unit.name}>
                        <span className="glyphicon glyphicon-list-alt"></span>&nbsp;{this.props.unit.name}
                    </p>
                    <p style={{ display: 'inline-block', width: '15%' }} id={"county-voters-count-" + this.props.unit.name}>
                        <span className="glyphicon glyphicon-user"></span>&nbsp;{this.props.unit.voterCount}
                    </p>
                    <p style={{ display: 'inline-block', width: '40%' }} id={"county-address-" + this.props.unit.name}>
                        <span className="glyphicon glyphicon-map-marker"></span>&nbsp;
                        <span
                            className={"popoverAddress-" + this.props.index}
                            onMouseOver={this.popupAddress}
                            data-content={this.props.unit.address}
                            rel="popover"
                            data-placement="top"
                            style={this.handStyle()}
                        >
                            {truncate(this.props.unit.address, truncateLength)}
                        </span>
                    </p>
                    <p className="unit-actions-area">
                        <span className="glyphicon glyphicon-edit popoverCounty remove-units-element"
                            id={"edit-county-button-" + this.props.unit.name}
                            style={{ cursor: 'pointer' }}
                            onClick={this.toggleUpdate}
                            onMouseOver={this.popup}
                            data-content="Redaguoti"
                            rel="popover"
                            data-placement="top"
                        >
                        </span>
                        <ConfirmAction
                            title="Ar tikrai norite pašalinti apylinę?"
                            body="Duomenų atstatymas neįmanomas."
                            onConfirm={this.props.delete.bind(null, this.props.index)}
                        >
                            <span className="glyphicon glyphicon-remove-sign popoverCounty remove-units-element"
                                id={"remove-county-button-" + this.props.unit.name}
                                onMouseOver={this.popup}
                                data-content="Ištrinti"
                                rel="popover"
                                data-placement="top"
                            >
                            </span>
                        </ConfirmAction>
                    </p>
                </div>
            );
        }

        return <div className="list-group-item">{updateOrDisplay}</div>
    },
 });

module.exports = CountyDisplayComponent;
