var React = require('react');
var axios = require('axios');
var DistrictDisplayComponent = require('../components/DistrictDisplayComponent');
var CountyDisplayComponent = require('../components/CountyDisplayComponent');
var NewCountyInlineForm = require('../components/tiny_components/NewCountyInlineForm');
var NewCountyFormButton = require('../components/tiny_components/NewCountyFormButton');
var spring = require('../../config/SpringConfig');
var Helper = require('../../utils/Helper');

var DistrictDisplayContainer = React.createClass({
    getInitialState() {
        return ({ showCounties: false,
                  showInlineForm: false,
                  countyName: "",
                  countyAddress: "",
                  voterCount: undefined,
                  counties: this.props.unit.counties,
                  springErrors: [],
                  ASC: true,
                  needToSort: false
                });
    },
    toggleSortOrder() {
        this.setState({ ASC: !this.state.ASC, needToSort: true });
    },
    toggleCountiesList() {
        this.setState({ showCounties: !this.state.showCounties });
    },
    prepareAndSortCounties() {
        var stateCounties = this.state.counties;
        var counties = [];

        stateCounties.forEach((c, index) => {
            counties.push(
                <CountyDisplayComponent
                    key={c.district.name + "-" + c.name}
                    index={index}
                    unit={c}
                    delete={this.handleCountyDelete}
                    updateCountiesState={this.updateCountiesState}
                />
            );
        });
        var sortedCounties = (this.state.needToSort) ? Helper.sort(counties, this.state.ASC) : counties;
        sortedCounties.push(
            <div className="list-group-item" key={stateCounties.length}>
                {this.toggleInlineForm()}
            </div>
        );
        return sortedCounties;
    },
    updateCountiesState(newCounty, index) {
        var counties = this.state.counties;
        counties.splice(index, 1, newCounty);
        this.setState({ counties: counties });
    },
    toggleShowInlineState() {
        this.setState({
            showInlineForm: !this.state.showInlineForm,
            countyName: "",
            voterCount: undefined,
            countyAddress: ""
        });
    },
    handleCountyCreate() {
        var _this = this;
        var errors = [];
        var body = {
            name: this.state.countyName,
            voterCount: this.state.voterCount,
            address: this.state.countyAddress
        };
        var postUrl = spring.localHost.concat('/api/district/') + this.props.unit.id + '/add-county'
        axios.post(postUrl, body)
            .then(function(resp) {
                _this.setState({
                    showInlineForm: false,
                    countyName: "",
                    voterCount: undefined,
                    countyAddress: "",
                    countyBackendErrors: [],
                    springErrors: [],
                    counties: resp.data.counties
                });
            })
            .catch(function(err) {
                console.log(err);
                var finalErrors = [];

                if (err.response == undefined) {
                    finalErrors.push("Tinklo klaida");
                } else {
                    finalErrors.push(err.response.data.rootMessage);
                    finalErrors = finalErrors.concat(err.response.data.errorsMessages);
                }
                _this.setState({ springErrors: finalErrors });
            });
    },
    handleCountyDelete(index) {
        var _this = this;
        var counties = this.state.counties;

        axios.delete(spring.localHost.concat('/api/county/') + counties[index].id)
		    .then(function(resp) {
		        counties.splice(index, 1);
		        _this.setState({ counties: counties });
		    })
		    .catch(function(err) {
		        console.log(err);
		    })
    },
    handleCountyCancel() {
        this.setState({ showInlineForm: !this.state.showInlineForm });
    },
    handleNameChange(e) {
        this.setState({ countyName: e.target.value })
    },
    handleVoterCount(e) {
        this.setState({ voterCount: e.target.value })
    },
    handleAddressChange(value) {
        this.setState({ countyAddress: value});
    },
    setSuggest(suggest) {
        this.setState({ countyAddress: suggest.label });
    },
    handleDistrictDestroy() {
        var _this = this;
        var deletePath = spring.localHost.concat("/api/district/") + this.props.unit.id + "";
        axios.delete(deletePath)
            .then(function(resp) {
                _this.setState({ showCounties: false });
                _this.props.remove(_this.props.index);
            })
            .catch(function(err) {
                console.log(err);
            });
    },
    toggleInlineForm() {
        if (this.state.showInlineForm) {
            return <NewCountyInlineForm
                      cancel={this.handleCountyCancel}
                      submit={this.handleCountyCreate}
                      changeName={this.handleNameChange}
                      changeVoterCount={this.handleVoterCount}
                      changeAddress={this.handleAddressChange}
                      setSuggest={this.setSuggest}
                      name={this.state.countyName}
                      count={this.state.voterCount}
                      address={this.state.countyAddress}
                      springErrors={this.state.springErrors}
                      districtName={this.props.unit.name}
                      btnName="Sukurti"
                   />
        } else {
            return <NewCountyFormButton
                renderCountyForm={this.toggleShowInlineState}
                districtName={this.props.unit.name}
            />
        }
    },
    render() {
        var counties = (this.state.showCounties) ? this.prepareAndSortCounties() : [];
        return <DistrictDisplayComponent
                    unit={this.props.unit}
                    counties={counties}
                    toggleCountiesList={this.toggleCountiesList}
                    toggleSortOrder={this.toggleSortOrder}
                    ASC={this.state.ASC}
                    show={this.state.showCounties}
                    delete={this.handleDistrictDestroy}
               />
    }
});

module.exports = DistrictDisplayContainer;
