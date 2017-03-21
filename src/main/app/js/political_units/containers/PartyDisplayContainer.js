var React = require('react');
var axios = require('axios');
var PartyDisplayComponent = require('../components/PartyDisplayComponent');
var CandidateCardComponent = require('../../components/CandidateCardComponent');
var ConfirmAction = require('../../components/tiny_components/ConfirmAction');
var InlineCsvUploadForm = require('../../components/tiny_components/InlineCsvUploadForm');
var spring = require('../../config/SpringConfig');

var PartyDisplayContainer = React.createClass({
    getInitialState: function() {
        return ({ showCandidates: false,
                  springErrors: [],
                  party: this.props.unit,
                  displayLoadingIcon: {display: "none"}
               });
    },
    componentWillReceiveProps: function(newProps) {
        if (newProps.unit != this.state.party) {
            this.setState({ party: newProps.unit })
        }
    },
    prepareCandidates: function() {
        var cand = [];
        if (this.state.showCandidates) {
            this.state.party.candidates.forEach((c, index) => {
                cand.push(
                      <CandidateCardComponent
                          key={c.personId}
                          candidate={c}
                      />
                );
            });
        }
        return cand;
    },
    deleteCandidates: function() {
        this.props.deleteCandidates(this.state.party.id);
    },
    deleteParty: function(index, party_id) {
        this.toggleShowCandidates();
        this.props.delete(index, party_id);
    },
    toggleShowCandidates: function() {
        this.setState({ showCandidates: !this.state.showCandidates });
    },
    uploadCandidates: function(fd, partyID) {
        var _this = this;
        var errors = [];
        var uploadUrl = spring.localHost.concat("/api/party/") + partyID + "/candidates";
        this.setState({displayLoadingIcon: {display: "inline"}});
        axios.post(uploadUrl, fd, { headers: { 'Content-Type': 'multipart/form-data' } })
            .then(function(resp) {
                _this.props.closeModal();
                _this.setState({ springErrors: [], party: resp.data, displayLoadingIcon: {display: "none"} });
                _this.props.updateParties(resp.data);
            })
            .catch(function(err) {
                _this.props.closeModal();
                console.log(err);
                errors.push(err.response.data.rootMessage);
                _this.setState({ springErrors: errors.concat(err.response.data.errorsMessages),
                    displayLoadingIcon: {display: "none"}});
            });
    },
    determineActions: function() {
        var actions;
        if (this.state.party.candidates.length > 0) {
            actions =
                  <ConfirmAction
                      title="Ar tikrai norite pašalinti apygardos kandidatų sąrašą?"
                      body="Duomenų atstatymas neįmanomas."
                      onConfirm={this.deleteCandidates.bind(this, this.state.party.id)}
                  >
                      <p id ={ "delete-party-members-" + this.state.party.name} className="remove-units-element confirmation-buttons">
                          <span className="glyphicon glyphicon-remove-sign">
                          </span> &nbsp;
                          Šalinti narius
                      </p>
                  </ConfirmAction>
        } else {
            actions = <InlineCsvUploadForm
                          upload={this.uploadCandidates}
                          associationId={this.state.party.id}
                          springErrors={this.state.springErrors}
                          openModal={this.props.openModal}
                          name={this.state.party.name}
                      />
        }
        return actions;
    },
    determineDisplay: function() {
        var display;
        if (!this.state.showCandidates) display = {display: 'none'};
        return display;
    },
    confirmDeleteParty: function() {
        return (
            <ConfirmAction
                title="Ar tikrai norite pašalinti partiją?"
                body="Duomenų atstatymas neįmanomas."
                onConfirm={this.deleteParty.bind(this, this.props.raktas, this.state.party.id)}
            >
                <p id ={ "delete-party-" + this.state.party.name} className="remove-units-element confirmation-buttons">
                    <span className="glyphicon glyphicon-remove-sign">
                    </span> &nbsp;
                    Šalinti partiją
                </p>
            </ConfirmAction>
        );
    },
    render: function() {
        return (
            <PartyDisplayComponent
                toggleShow={this.toggleShowCandidates}
                delete={this.deleteParty}
                unit={this.state.party}
                candidates={this.prepareCandidates()}
                springErrors={this.state.springErrors}
                displayLoadingIcon={this.state.displayLoadingIcon}
                actions={this.determineActions()}
                confirmDeleteParty={this.confirmDeleteParty()}
                display={this.determineDisplay()}
            />
        );
    }
});

module.exports = PartyDisplayContainer;
