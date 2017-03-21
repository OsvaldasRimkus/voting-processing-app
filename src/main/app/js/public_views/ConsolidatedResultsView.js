var React = require('react');
var ReactRouter = require('react-router');
var Link = ReactRouter.Link;
var axios = require('axios');
var ReactTable = require('react-table').default;
var spring = require('../config/SpringConfig');
var ChartContainer = require('./chart_components/ChartContainer');
var DataProcessor = require('./chart_components/DataProcessor');


var hide = { display: 'none' };

var ConsolidatedResultsView = React.createClass({
    getInitialState() {
        return ({ results: undefined });
    },
    componentDidMount() {
        axios.get(spring.localHost.concat('/api/results/consolidated/'))
            .then(function(response) {
                this.setState({
                    results: response.data,
                    chartData: DataProcessor.cleanConsolidatedResultsDataForChart(response.data.partyMandates),
                });
            }.bind(this))
            .catch(err => {
                console.log(err);
            });
    },
    generatePartyTable() {
        let results = this.state.results
        return (
            <div>
                <table className="table table-bordered">
                    <thead>
                    <tr>
                        <th className="lefters">Partija</th>
                        <th className="lefters">Laimėti mandatai</th>
                    </tr>
                    </thead>
                    <tbody>
                    { results &&
                    results.partyMandates.map(function(result, idx) {
                        return (
                            <tr key={idx + '-' + result.party.name}>
                                <td className='lefters'>{result.party.name}</td>
                                <td>{result.mandates}</td>
                            </tr>
                        )
                    })
                    }
                    </tbody>
                </table>
            </div>
        )
    },
    generateElecteesTable() {
        let results = this.state.results
        return (
            <table className="table table-bordered">
                <thead>
                    <tr>
                        <th>Kandidatas</th>
                        <th>Iškėlė</th>
                    </tr>
                </thead>
                <tbody>
                    { results &&
                        results.electedCandidates.map(function(candidate) {
                            return (
                                <tr key={candidate.id}>
                                    <td>{candidate.firstName + ' ' + candidate.lastName}</td>
                                    <td>{candidate.party != null ? candidate.party.name : 'Išsikėlė pats'}</td>
                                </tr>
                            )
                        })
                    }
                </tbody>
            </table>
        )
    },
    render() {
        let results = this.state.results
        let heading,
            partyTable,
            electedCandidatesTable

        if (results) {
            heading =
                <div className="row narrowed" style={{ margin: '30px 0px 30px 0px' }}>
                    <div className="col-md-4"></div>
                    <div className="col-md-5">
                        <p className="small-p">Iš viso rinkimuose dalyvauja - <strong>{results.totalDistricts}</strong> apygardų.</p>
                        <p className="small-p">Vienamdačių apygardų rezultatai gauti iš - <strong>{results.completedSmResults}</strong> apygardų.</p>
                        <p className="small-p">Daugiamandačių apygardų rezultatai gauti iš - <strong>{results.completedMmResults}</strong> apygardų.</p>
                    </div>
                </div>
            partyTable = this.generatePartyTable()
            electedCandidatesTable = this.generateElecteesTable()
        }

        return (
            <div>
                <h4 className="h4-election">2017 m. kovo 16 d. Lietuvos Respublikos Seimo rinkimai</h4>
                { heading }
                <h3> Partijų laimėti mandatai: </h3>
                {this.state.chartData && 
                    <ChartContainer 
                        data={this.state.chartData} 
                        showTooltip={false}
                        showPercent={false}
                    />
                }
                { partyTable }
                <h3> Į seimą išrinkti kandidatai: </h3>
                <div>{ electedCandidatesTable }</div>
            </div>
        );
    }
});

module.exports = ConsolidatedResultsView;