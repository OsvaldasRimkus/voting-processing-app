var React = require('react');
var axios = require('axios');
var PoliticalUnitsComponent = require('../components/PoliticalUnitsComponent');
var PartyDisplayContainer = require('./PartyDisplayContainer');
var CandidateCardComponent = require('../../components/CandidateCardComponent');
var PleaseWaitModal = require('../../components/tiny_components/PleaseWaitModal');
var spring = require('../../config/SpringConfig');

var PoliticalUnitsContainer = React.createClass({
    getInitialState: function() {
        return ({ parties: [],
                  springErrors: [],
                  popupAlert: false
        });
    },
    componentDidMount: function() {
        var _this = this;
        axios.get(spring.localHost.concat('/api/party'))
            .then(function(resp) {
                _this.setState({ parties: resp.data });
            })
            .catch(function(err) {
                console.log(err);
            });
    },
    prepareParties() {
        var parties = [];
        this.state.parties.forEach((p, idx) => {
            parties.push(

                <PleaseWaitModal
                    key={p.id + '.' + p.name}
                    raktas={idx}
                    index={idx}
                    unit={p}
                    delete={this.deleteParty}
                    deleteCandidates={this.deleteCandidates}
                    updateParties={this.updateParties}
                />
            );
        });
        return parties;
    },
    deleteCandidates: function(party_id) {
        var _this = this;
        var deleteUrl = spring.localHost.concat("/api/party/") + party_id + "/candidates";
        var stateParties = this.state.parties;
        axios.delete(deleteUrl)
            .then(function(resp) {
                for (var i = 0; i < stateParties.length; i++) {
                    if (stateParties[i].id == party_id) {
                        var party = stateParties[i];
                        party.candidates = [];
                        stateParties[i] = party;
                    }
                }
                _this.setState({ parties: stateParties });
            })
            .catch(function(err) {
                console.log(err);
            });
    },
    updateParties: function(party) {
        var _this = this;
        var stateParties = this.state.parties;
        for (var i = 0; i < stateParties.length; i++) {
            if (stateParties[i].id == party.id) {
                stateParties[i] = party;
                return;
            }
        }
        this.setState({ parties: stateParties });
    },
    handlePartySubmit: function(fd, name) {
        var party = { name: name };
        fd.append("party", JSON.stringify(party));
        var _this = this;
        var parties = this.state.parties;
        var errors = [];

        axios.post(spring.localHost.concat('/api/party'), fd, { headers: { 'Content-Type': 'multipart/form-data' } })
            .then(function(resp) {
                parties.push(resp.data);
                _this.setState({ parties: parties, springErrors: [], popupAlert: true });
            })
            .catch(function(err) {
                console.log(err);
                errors.push(err.response.data.rootMessage);
                _this.setState({ springErrors: errors.concat(err.response.data.errorsMessages) });
            });
    },
    deleteParty(idx, party_id) {
        var _this = this;
        var deleteURL = spring.localHost.concat("/api/party/") + party_id + "";
        axios.delete(deleteURL)
            .then(function(resp) {
                var parties = _this.state.parties;
                parties.splice(idx, 1);
                _this.setState({ parties: parties });
            })
            .catch(function(err) {
               console.log(err);
            });

    },
    popupAlert() {
        if (this.state.popupAlert) this.setState({ popupAlert: false });
        return this.state.popupAlert;
    },
    render: function() {
        return <PoliticalUnitsComponent
                  parties={this.prepareParties()}
                  create={this.handlePartySubmit}
                  springErrors={this.state.springErrors}
                  popupAlert={this.popupAlert()}
               />
    }
});

module.exports = PoliticalUnitsContainer;
