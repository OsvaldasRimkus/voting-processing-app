var React = require('react');
var axios = require('axios');
var CountyDisplayComponent = require('../components/CountyDisplayComponent');
var AdminViewCandidateComponent = require('../components/tiny_components/AdminViewCandidateComponent');
var ConfirmButton = require('../components/tiny_components/ConfirmButton');
var VoteListRow = require('../../counties_results/components/VoteListRow');
var spring = require('../../config/SpringConfig');
var ConfirmAction = require('../../components/tiny_components/ConfirmAction');

var CountyDisplayContainer = React.createClass({
    getInitialState: function() {
        return ({
            showResults: false,
            smDisplay: undefined,
            county: this.props.unit,
            smResult: this.props.unit.smResult,
            mmResult: this.props.unit.mmResult,
            activeResultId: undefined
        });
    },
    componentWillReceiveProps: function(newProps) {
        if (newProps.unit != this.state.county) {
            this.setState({ county: newProps.unit });
        }
    },
    toggleShowResults: function() {
        this.setState({ showResults: !this.state.showResults });
    },
    displaySMresults: function() {
        activeResultId = this.state.smResult ? this.state.smResult.id : undefined
        this.setState({ 
            smDisplay: true, 
            activeResultId: activeResultId
        });
    },
    displayMMresults: function() {
        activeResultId = this.state.mmResult ? this.state.mmResult.id : undefined
        this.setState({ 
            smDisplay: false, 
            activeResultId: activeResultId
         });
    },
    prepareResult: function() {
        if (this.state.smDisplay == undefined) return undefined;
        var result = (this.state.smDisplay) ? this.state.smResult : this.state.mmResult
        if (result == undefined) return result;

        var preparedResult = [];

        preparedResult.push(
            <div className="list-group-item" key={result.votes.length}>
                <p>Sugadinti biuleteniai: {result.spoiledBallots}</p>
            </div>
        );
        result.votes.forEach((vote, idx) => {
            preparedResult.push(
                <VoteListRow
                    key={idx}
                    vote={vote}
                />
            );
        });
        return preparedResult;
    },
    determineConfirmButton: function() {
        var smDisplay = this.state.smDisplay;
        var activeResultId = this.state.activeResultId;

        if (smDisplay === undefined || activeResultId === undefined) {
            return undefined
        } 

        var result = smDisplay ? this.state.smResult : this.state.mmResult

        if (result.confirmed) {
            confirmBtn =

                    <ConfirmButton
                        id={"confirmed-results-button-" + this.state.county.name + "-" + this.state.county.district.name}
                        text="Patvirtinta "
                        spanClass="glyphicon glyphicon-ok-sign"
                    />
        } else {
            confirmBtn =
                <ConfirmAction
                    title={"Norite patvirtinti įkeltus rezultatus? Apygarda: " + this.state.county.district.name + ". Apylinkė: " + this.state.county.name +"."}
                    body="Patvirtintų duomenų atšaukimas neįmanomas."
                    onConfirm={this.confirmResults.bind(this, result.id)}
                    confirmText={"Patvirtinti"}
                >

                    <button
                        id={"confirm-results-button-" + this.state.county.name + "-" + this.state.county.district.name}
                        className="btn btn-default btn-sm floaters-right"
                    >
                        Patvirtinti rezultatus&nbsp;
                        <span className="glyphicon glyphicon-exclamation-sign"></span>
                    </button>
                </ConfirmAction>
        }
        return confirmBtn;
    },
    determineDeleteButton: function() {
        if (this.props.unit.smResult != null) {
            if (this.state.smDisplay && this.props.unit.smResult.confirmed) return undefined;
        }
        if (this.props.unit.mmResult != null) {
            if (!this.state.smDisplay && this.props.unit.mmResult.confirmed) return undefined;
        }

        if (this.state.activeResultId) {
            deleteBtn = <ConfirmAction
                            title={"Norite pašalinti įkeltus rezultatus? Apygarda: " + this.state.county.district.name + ". Apylinkė: " + this.state.county.name +"."}
                            body="Duomenų atstatymas bus neįmanomas."
                            onConfirm={this.handleDelete.bind(this, this.state.activeResultId)}
                            confirmText={"Pašalinti"}
                            >

                            <button
                                id={"delete-results-button-" + this.state.county.name + "-" + this.state.county.district.name}
                                className="btn btn-default btn-sm floaters-right"
                            >
                                Pašalinti rezultatus&nbsp;
                                <span className="glyphicon glyphicon-remove"></span>
                            </button>

                        </ConfirmAction>
        } else {
            deleteBtn = undefined
        }

        return deleteBtn;
    },
    confirmResults: function(resultId) {
        var resultType = this.state.smDisplay ? 'smResult' : 'mmResult';
        var result = (resultType === 'smResult') ? this.state.smResult : this.state.mmResult;

        var _this = this;
        var params = new URLSearchParams();
        params.append('resultId', resultId);

        axios.post(spring.localHost.concat('/api/results/confirm'), params)
            .then(function(resp) {
                result['confirmed'] = true
                _this.setState({ [resultType]: result });
            })
            .catch(function(err) {
                console.log(err);
            });
    },
    handleDelete: function(resultId) {
        var resultType = this.state.smDisplay ? 'smResult' : 'mmResult';
        var _this = this;

        axios
            .delete(spring.localHost.concat('/api/results/' + resultId))
            .then(function(resp) {
                _this.setState({ 
                    [resultType] : undefined,
                    activeResultId: undefined
                 })
            })
            .catch(function(err) {
                console.log(err);
            })
    },
    determineAllConfirmedButton: function() {
        var btn = (
            <span className="btn-sm confirmed-floaters">
                Patvirtinta &nbsp; <span className="glyphicon glyphicon-flag"></span>
            </span>
        );
        let smResult = this.state.smResult,
            mmResult = this.state.mmResult;
        return (smResult && mmResult && smResult.confirmed && mmResult.confirmed) ? btn : undefined;
    },
    render: function() {
        return (
            <CountyDisplayComponent
                index={this.props.index}
                show={this.state.showResults}
                toggleShow={this.toggleShowResults}
                county={this.state.county}
                displaySM={this.displaySMresults}
                displayMM={this.displayMMresults}
                results={this.prepareResult()}
                smDisplay={this.state.smDisplay}
                confirmBtn={this.determineConfirmButton()}
                deleteBtn={this.determineDeleteButton()}
                allConfirmedBtn={this.determineAllConfirmedButton()}
            />
        );
    }
});

module.exports = CountyDisplayContainer;
