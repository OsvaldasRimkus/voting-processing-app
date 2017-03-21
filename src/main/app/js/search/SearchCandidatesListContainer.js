var React = require('react');
var axios = require('axios');
var SearchCandidateComponent = require('./SearchCandidateComponent');
var SearchBarComponent = require('./SearchBarComponent');
var SearchCandidatesHeaderComponent = require('./SearchCandidatesHeaderComponent');
var SearchCandidateCardComponent = require('./SearchCandidateCardComponent');
var EmptySearchCandidateCardComponent = require('./EmptySearchCandidateCardComponent');
var spring = require('../config/SpringConfig');

var timer = null;

var SearchCandidatesListContainer = React.createClass({
    getInitialState: function() {
        return ({ candidates: [], query: '', activeCandidate: undefined });
    },
    componentDidMount: function() {
        var _this = this;
        axios.get(spring.localHost.concat('/api/candidate'))
             .then(resp => {
                _this.setState({ candidates: resp.data });
             })
             .catch(err => {
                console.log(err);
             });
    },
    setActiveCandidate: function(candidate) {
        this.setState({ activeCandidate: candidate });
    },
    clearActiveCandidate: function(candidate) {
        this.setState({ activeCandidate: undefined });
    },
    prepareCandidates: function() {
        var query = this.state.query.toLowerCase();
        var candidates = [];
        this.state.candidates.forEach((c, idx) => {
            var match = false;

            if ((c.firstName + " " + c.lastName).toLowerCase().includes(query)) match = true;
            else if (c.firstName.toLowerCase().includes(query)) match = true;
            else if (c.lastName.toLowerCase().includes(query)) match = true;
            else if (c.party != null) {
              if (c.party.name.toLowerCase().includes(query)) match = true;
            }
            else if (c.party == null) {
                if ("išsikėlęs pats".includes(query)) match = true;
            }

            if (match) {
                candidates.push(
                    <SearchCandidateComponent
                        key={idx}
                        candidate={c}
                        showDetails={this.setActiveCandidate}
                    />
                );
            }
        });
        candidates.unshift(<SearchCandidatesHeaderComponent key={this.state.candidates.length}/>);
        return candidates;
    },
    handleChangeQuery: function(value) {
        this.setState({ query: value });
    },
    onKeyUp() {
        var value = document.getElementById("system-search").value;
        var _this = this;

        $('#system-search').keyup(function() {
            clearTimeout(timer);
            timer = setTimeout(_this.handleChangeQuery.bind(_this, value), 1000);
        })
    },
    clearQuery: function() {
        this.setState({ query: '' });
    },
    render() {
        var activeCandidate = (this.state.activeCandidate != undefined) ?
        (
            <SearchCandidateCardComponent
                candidate={this.state.activeCandidate}
                hideDetails={this.clearActiveCandidate}
            />
        )
        : <EmptySearchCandidateCardComponent/>;
        return (
            <div>
                <SearchBarComponent
                    changeQuery={this.handleChangeQuery}
                    clearQuery={this.clearQuery}
                    onKeyUp={this.onKeyUp}
                />
                <div className="col-md-8">
                    {this.prepareCandidates()}
                </div>
                <div className="col-md-4">
                    {activeCandidate}
                </div>
            </div>
        );
    }
});

module.exports = SearchCandidatesListContainer;
