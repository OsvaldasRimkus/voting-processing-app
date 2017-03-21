/**
 * Created by osvaldas on 17.3.14.
 */
var React = require('react');
var ReactRouter = require('react-router');
var Link = ReactRouter.Link;
var axios = require('axios');
var ReactTable = require('react-table').default;
var spring = require('../config/SpringConfig');
var Helper = require('../utils/Helper');
var ChartContainer = require('./chart_components/ChartContainer');
var DataProcessor = require('./chart_components/DataProcessor');

var hide = {
    display: 'none'
};

var CountyMMResultsView = React.createClass({
    getInitialState() {
        return ({ collection: {}, chartData: undefined, chartMetadata: undefined });
    },
    componentWillMount() {
        var id = this.props.params.id;
        axios.get(
            spring.localHost
                .concat('/api/results/county/')
                .concat(id + '')
                .concat('/multi-mandate')
        )
            .then(function(resp) {
                this.setState({ 
                    collection: resp.data,
                    chartData: DataProcessor.cleanMultiMandateVotingDataForChart(resp.data.votes),
                    chartMetadata: { 
                        total: resp.data.totalBallots,
                        valid: resp.data.validBallots
                    }
                });
            }.bind(this))
            .catch(err => {
                console.log(err);
            });
    },

    prepareData() {
        if (Object.keys(this.state.collection).length == 0) return [];
        var rows = [];
        let totalPercentageOfTotalBallots = 0.0;
        var totalPercentageOfValidBallots = 0.0;

        this.state.collection.votes.forEach(v => {
            const partyName = <Link to="">{v.party.name}</Link>;
            const percFromValid = (isNaN(v.voteCount / (this.state.collection.validBallots * 1.0) * 100)) ? 0 : (v.voteCount / (this.state.collection.validBallots * 1.0) * 100);
            const percFromTotal = (isNaN(v.voteCount / (this.state.collection.totalBallots * 1.0) * 100)) ? 0 : (v.voteCount / (this.state.collection.totalBallots * 1.0) * 100);
            totalPercentageOfTotalBallots += (isNaN(percFromTotal)) ? 0 : percFromTotal;
            totalPercentageOfValidBallots += (isNaN(percFromValid)) ? 0 : percFromValid;

            rows.push(
                {
                    partyName: partyName,
                    voteCount: v.voteCount,
                    votesFromValid: percFromValid.toFixed(2),
                    votesFromTotal: percFromTotal.toFixed(2)
                }
            );
        });

        rows.push(
            {
                partyName: <strong style={{ float: 'right', marginRight: 10 }}>Iš viso:</strong>,
                voteCount: <strong>{this.state.collection.validBallots}</strong>,
                votesFromValid: <strong>{totalPercentageOfValidBallots}</strong>,
                votesFromTotal: <strong>{totalPercentageOfTotalBallots.toFixed(2)}</strong>
            }
        );

        return rows;
    },

    getColumns() {
        return (
            [
                {
                    header: 'Partija',
                    accessor: 'partyName',
                    headerStyle: { fontWeight: 'bold' },
                    style: { marginLeft: 5 },
                    id: 1
                },
                {
                    header: 'Balsai',
                    accessor: 'voteCount',
                    headerStyle: { fontWeight: 'bold' },
                    style: { textAlign: 'center' },
                    id: 2
                },
                {
                    header: '% nuo galiojančių biuletenių',
                    accessor: 'votesFromValid',
                    headerStyle: { fontWeight: 'bold' },
                    style: { textAlign: 'center' },
                    id: 3
                },
                {
                    header: '% nuo dalyvavusių rinkėjų',
                    accessor: 'votesFromTotal',
                    headerStyle: { fontWeight: 'bold' },
                    style: { textAlign: 'center' },
                    id: 4
                }
            ]
        );
    },

    // getDistrictName() {
    //     return (Object.keys(this.state.collection).length == 0) ?
    //         '' : this.state.collection.votes[0].candidate.district.name;
    // },
    getCountyName() {
        return (Object.keys(this.state.collection).length == 0) ?
            '' : this.state.collection.county.name;
    },
    getPercentOfTotal() {
        return (this.state.collection.totalBallots * 1.0 / this.state.collection.voterCount * 100).toFixed(2);
    },
    getPercentOfSpoiled() {
        return (this.state.collection.spoiledBallots * 1.0 / this.state.collection.voterCount * 100).toFixed(2);
    },


    render() {
        return(
            <div>
                <h4 className="h4-election">2017 m. kovo 16 d. Lietuvos Respublikos Seimo rinkimai</h4>
                {/*<h3>{this.getDistrictName()} apygarda</h3>*/}
                {/*<h3>KAZKAIP REIKIA APYGARDA GAUTI?</h3>*/}
                <h4 className="h4-election">apylinkė: {this.getCountyName()}</h4>
                <div className="row narrowed" style={{ margin: '30px 0px 30px 0px' }}>
                    <div className="col-md-4"></div>
                    <div className="col-md-5">
                        <p className="small-p">Rinkėjų skaičius apylinkėje: <strong>{this.state.collection.voterCount}</strong></p>
                        <p className="small-p">Iš viso rinkimų apylinkėje dalyvavusių rinkėjų skaičius: &nbsp;
                            <strong>{this.state.collection.totalBallots} ({this.getPercentOfTotal()}%)</strong></p>
                        <p className="small-p">Iš viso negaliojančių rinkimų apylinkėje biuletenių skaičius: &nbsp;
                            <strong>{this.state.collection.spoiledBallots} ({this.getPercentOfSpoiled()}%)</strong></p>
                    </div>
                    <div className="col-md-12">
                        <p className="centered-p">
                            Sąraše įrašytų ir rinkimuose dalyvavusių rinkėjų skaičiai skelbiami
                            pagal duomenis perdavusių rinkimų apylinkių protokolus.
                        </p>
                    </div>
                </div>
                {this.state.chartData 
                    && 
                    <ChartContainer 
                        data={this.state.chartData} 
                        metadata={this.state.chartMetadata}
                        showTooltip={true}
                        showPercent={true}
                    />
                }
                <div>
                    <ReactTable
                        data={this.prepareData()}
                        columns={this.getColumns()}
                        defaultPageSize={20}
                        showPageJump={false}
                        previousText='Ankstesnis'
                        nextText='Kitas'
                        loadingText='Kraunama...'
                        noDataText='Duomenų nėra'
                        pageText='Puslapis'
                        ofText='iš '
                        rowsText='eilučių'
                    />
                </div>
            </div>
        );
    }
});

module.exports = CountyMMResultsView;