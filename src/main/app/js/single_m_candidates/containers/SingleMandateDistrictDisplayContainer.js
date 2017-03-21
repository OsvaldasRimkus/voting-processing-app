var React = require('react');
var axios = require('axios');
var SingleMandateDistrictDisplayComponent = require('../components/SingleMandateDistrictDisplayComponent');
var CandidateCardComponent = require('../../components/CandidateCardComponent');
var InlineCsvUploadForm = require('../../components/tiny_components/InlineCsvUploadForm');
var ConfirmAction = require('../../components/tiny_components/ConfirmAction');
var spring = require('../../config/SpringConfig');

var SingleMandateDistrictDisplayContainer = React.createClass({
    getInitialState: function() {
        return ({ showCandidates: false,
                  springErrors: [],
                  district: this.props.unit });
    },
    componentWillReceiveProps: function(newProps) {
        if (newProps.unit != this.state.district) {
            this.setState({ district: newProps.unit })
        }
    },
    prepareCandidates: function() {
        var cand = [];
        if (this.state.showCandidates) {
            this.state.district.candidates.forEach((c, index) => {
                cand.push(
                      <CandidateCardComponent
                          key={index}
                          candidate={c}
                      />
                )
            });
        }
        return cand;
    },
    uploadCandidates: function(fd, districtId) {
        var _this = this;
        var errors = [];
        var uploadPath = spring.localHost.concat("/api/district/") + districtId + "/candidates";
        axios.post(uploadPath, fd, { headers: { 'Content-Type': 'multipart/form-data' } })
            .then(function(resp) {
                _this.setState({ springErrors: [], district: resp.data });
            })
            .catch(function(err) {
                console.log(err);
                errors.push(err.response.data.rootMessage);
                _this.setState({ springErrors: errors.concat(err.response.data.errorsMessages) });
            });
    },
    deleteCandidates: function() {
        this.props.deleteCandidates(this.state.district.id);
    },
    toggleShowCandidates: function() {
        this.setState({ showCandidates: !this.state.showCandidates });
    },
    emptyFunction: function () {},
    determineActions: function() {
        var actions;
        if (this.state.district.candidates.length > 0) {
            actions =
                <ConfirmAction
                    title="Ar tikrai norite pašalinti apygardos kandidatų sąrašą?"
                    body="Duomenų atstatymas neįmanomas."
                    onConfirm={this.deleteCandidates}
                >
                    <p className="remove-units-element">
                        <span className="glyphicon glyphicon-remove-sign" >
                        </span> &nbsp;
                        <span id={"remove-candidates-" + this.state.district.name}>Šalinti kandidatus</span>
                    </p>
                </ConfirmAction>
        } else {
            actions = <InlineCsvUploadForm
                          upload={this.uploadCandidates}
                          associationId={this.state.district.id}
                          springErrors={this.state.springErrors}
                          openModal={this.emptyFunction}
                          name={this.state.district.name}
                      />
        }
        return actions;
    },
    determineDisplay: function() {
        var display;
        if (!this.state.showCandidates) display = {display: 'none'};
        return display;
    },
    render: function() {
        return (
            <SingleMandateDistrictDisplayComponent
                toggleShow={this.toggleShowCandidates}
                name={this.state.district.name}
                candidates={this.prepareCandidates()}
                actions={this.determineActions()}
                display={this.determineDisplay()}
            />
        );
    }
});

module.exports = SingleMandateDistrictDisplayContainer;
