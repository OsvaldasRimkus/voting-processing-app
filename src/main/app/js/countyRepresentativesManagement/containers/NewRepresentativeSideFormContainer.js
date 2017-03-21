/**
 * Created by osvaldas on 17.2.7.
 */
var React = require('react');
var NewRepresentativeSideFormComponent = require('../components/NewRepresentativeSideFormComponent');

var OnlyDistricts = [];
var AllRepresentativesEmails = [];

var NewRepresentativeSideFormContainer = React.createClass ({

    getAllDistricts: function () {
        OnlyDistricts = [];
        self = this;
        var DistrictsInformation = this.props.districtsData;
        DistrictsInformation.map(function(district, index) {
            var currentDistrictName = district.name;
            var isDistrictRequiredInDistrictList = false;
            district.counties.map(function (county, index){
                var currentCountyName = county.name;
                var uniqueCombinationOfDistrictAndCounty = currentDistrictName.concat(currentCountyName);
                if (!self.props.uniqueDistrictAndCountyNameCombinationArray.includes(uniqueCombinationOfDistrictAndCounty)){
                    isDistrictRequiredInDistrictList = true;
                }
            });
            if (isDistrictRequiredInDistrictList){
                OnlyDistricts.push(district)
            }
        });
    },

    render: function () {
        {this.getAllDistricts()}
        return (
            <div>
                <NewRepresentativeSideFormComponent
                    newRep={this.props.newRep}
                    OnlyDistricts={OnlyDistricts}
                    CountyRepresentativesEmailsArray={this.props.CountyRepresentativesEmailsArray}
                    uniqueDistrictAndCountyNameCombinationArray={this.props.uniqueDistrictAndCountyNameCombinationArray}
                    springErrors={this.props.springErrors}
                    popupAlert={this.props.popupAlert}
                />
            </div>
        )
    }
});

module.exports = NewRepresentativeSideFormContainer;
