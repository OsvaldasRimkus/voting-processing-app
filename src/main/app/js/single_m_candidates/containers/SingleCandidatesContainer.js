var React = require('react');
var axios = require('axios');
var SingleMandateDistrictDisplayContainer = require('./SingleMandateDistrictDisplayContainer');
var SingleCandidatesComponent = require('../components/SingleCandidatesComponent');
var spring = require('../../config/SpringConfig');

var SingleCandidatesContainer = React.createClass({
    getInitialState: function() {
        return ({ districts: []
                //  springErrors: []
                });
    },
    componentDidMount: function() {
        this.districtsAxiosGet();
    },
    districtsAxiosGet: function() {
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
            districts.push(<SingleMandateDistrictDisplayContainer
                                key={index}
                                index={index}
                                unit={d}
                                deleteCandidates={this.deleteCandidates}
                           />)
        });
        return districts;
    },
    deleteCandidates: function(district_id) {
        var _this = this;
        var deleteUrl = spring.localHost.concat("/api/district/") + district_id + "/candidates";
        axios.delete(deleteUrl)
            .then(function(resp) {
                _this.districtsAxiosGet();
            })
            .catch(function(err) {
                console.log(err);
            });
    },
    render: function() {
        return <SingleCandidatesComponent
                  districts={this.prepareDistricts()}
               />
    }
});

module.exports = SingleCandidatesContainer;
