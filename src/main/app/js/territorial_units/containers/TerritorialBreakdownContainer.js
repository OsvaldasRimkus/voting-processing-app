var React = require('react');
var ReactRouter = require('react-router');
var axios = require('axios');
var DistrictDisplayContainer = require('./DistrictDisplayContainer');
var TerritorialBreakdownComponent = require('../components/TerritorialBreakdownComponent');
var AddedCountyDisplayComponent = require('../components/tiny_components/AddedCountyDisplayComponent');
var spring = require('../../config/SpringConfig');

var TerritorialBreakdownContainer = React.createClass({
    getInitialState: function() {
        return ({ districts: [],
                  districtName: "",
                  counties: [],
                  springErrors: [],
                  clearCountyForm: false,
                  popupAlert: false
        });
    },
    componentDidMount: function() {
        var _this = this;
        axios.get(spring.localHost.concat('/api/district/'))
            .then(function(resp) {
                _this.setState({ districts: resp.data });
            })
            .catch(function(err) {
                console.log(err);
            });
    },
    prepareDistricts() {
        var districts = [];
        this.state.districts.forEach((d, index) => {
            districts.push(
                <DistrictDisplayContainer
                    key={d.id + "-" + d.name}
                    index={index}
                    unit={d}
                    remove={this.handleDistrictRemove}
                />
            );
        });
        return districts;
    },
    prepareCounties() {
        var counties = [];
        this.state.counties.forEach((c, index) => {
            counties.push(
                <AddedCountyDisplayComponent
                    key={c.name + "" + index}
                    index={index}
                    county={c}
                    remove={this.handleAddedCountyRemove}
                />
            )
        });
        return counties;
    },
    handleNameChange: function(e) {
        this.setState({ districtName: e.target.value });
    },
    handleDistrictSubmit: function() {
        var _this = this;
        var errors = [];
        var body = {
            name: this.state.districtName,
            counties: this.state.counties
        }
        var districts = this.state.districts;
        axios.post(spring.localHost.concat('/api/district/'), body)
            .then(function(resp) {
                districts.push(resp.data);
                _this.setState({ districts: districts,
                                 districtName: "",
                                 counties: [],
                                 springErrors: [],
                                 clearCountyForm: true,
                                 popupAlert: true
                });
            })
            .catch(function(err) {
                console.log(err);
                errors.push(err.response.data.rootMessage);
                _this.setState({ springErrors: errors.concat(err.response.data.errorsMessages) });
            });
    },
    handleDistrictRemove(idx) {
        var districts = this.state.districts;
        districts.splice(idx, 1);
        this.setState({ districts:  districts});
    },
    handleAddCounty: function(body) {
        var counties = this.state.counties;
        counties.push(body);
        this.setState({ counties: counties });
    },
    handleAddedCountyRemove: function(idx) {
        var counties = this.state.counties;
        counties.splice(idx, 1);
        this.setState({ counties: counties });
    },
    popupAlert() {
        if (this.state.popupAlert) this.setState({ popupAlert: false });
        return this.state.popupAlert;
    },
    render: function() {
        return <TerritorialBreakdownComponent
                  districts={this.prepareDistricts()}
                  delete={this.handleDistrictRemove}
                  changeName={this.handleNameChange}
                  name={this.state.districtName}
                  create={this.handleDistrictSubmit}
                  addCounty={this.handleAddCounty}
                  counties={this.prepareCounties()}
                  springErrors={this.state.springErrors}
                  clearCountyForm={this.state.clearCountyForm}
                  popupAlert={this.popupAlert()}
               />
    }
});

module.exports = TerritorialBreakdownContainer;
