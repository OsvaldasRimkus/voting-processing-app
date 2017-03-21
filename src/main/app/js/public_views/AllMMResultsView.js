/**
 * Created by osvaldas on 17.3.15.
 */

var React = require('react');
var ReactRouter = require('react-router');
var Link = ReactRouter.Link;
var axios = require('axios');
var ReactTable = require('react-table').default;
var spring = require('../config/SpringConfig');
var Helper = require('../utils/Helper');

var hide = { display: 'none' };

var AllMMResultsView = React.createClass({
    getInitialState() {
        return ({ collection: [] });
    },
    componentWillMount() {
        axios.get(spring.localHost.concat('/api/results/multi-mandate/'))
            .then(function(resp) {
                const calcs = this.calcHeaderData(resp.data.districtResults);
                this.setState({
                    collection: resp.data.districtResults,
                    districts: resp.data.districtResults.length,
                    counties: calcs[0],
                    voters: calcs[1],
                    totalBallots: calcs[2],
                    percentTotalBallots: calcs[3],
                    spoiledBallots: calcs[4],
                    percentSpoiledBallots: calcs[5],
                    validBallots: calcs[6],
                    percentValidBallots: calcs[7]
                });
            }.bind(this))
            .catch(err => {
                console.log(err);
            });
    },
    calcHeaderData(collection) {
        let counties = 0;
        let voters = 0;
        let totalBallots = 0;
        let spoiledBallots = 0;
        let validBallots = 0;

        // collection.forEach(d => {
        Array.from(collection).forEach(d=>{
            counties += d.totalCounties;
            voters += d.voterCount;
            totalBallots += d.totalBallots;
            spoiledBallots += d.spoiledBallots;
            validBallots += d.validBallots;

        });

        const percentTotalBallots = ((totalBallots / (voters * 1.0)) * 100).toFixed(2);
        const percentSpoiledBallots = ((spoiledBallots / (totalBallots * 1.0)) * 100).toFixed(2);
        const percentValidBallots = ((validBallots / (totalBallots * 1.0)) * 100).toFixed(2);

        return [
            counties,
            voters,
            totalBallots,
            percentTotalBallots,
            spoiledBallots,
            percentSpoiledBallots,
            validBallots,
            percentValidBallots
        ];
    },
    prepareData() {
        if (this.state.collection.length == 0) return [];

        let totalConfirmedCounties = 0;
        let rows = [];

        // this.state.collection.forEach(d => {
        Array.from(this.state.collection).forEach(d=>{
            const percentTotalVoters = (d.totalBallots / (d.voterCount * 1.0) * 100).toFixed(2);
            const percentSpoiledBallots = (d.spoiledBallots / (d.totalBallots * 1.0) * 100).toFixed(2);
            const percentValidBallots = (d.validBallots / (d.totalBallots * 1.0) * 100).toFixed(2);
            const topParty = (d.topParty == null) ? 'Nėra' : d.topParty.name;

            totalConfirmedCounties += d.confirmedCountyResults;

            rows.push(
                {
                    district: <Link to={"apygardos-daugiamandaciai-rezultatai/" + d.district.id}>{d.district.name}</Link>,
                    counties: d.totalCounties,
                    confirmedCounties: d.confirmedCountyResults,
                    voterCount: d.voterCount,
                    totalBallots: d.totalBallots + " (" + percentTotalVoters + "%)",
                    spoiledBallots: d.spoiledBallots + " (" + percentSpoiledBallots + "%)",
                    validBallots: d.validBallots + " (" + percentValidBallots + "%)",
                    topParty: topParty,
                    votesForTopParty: d.votesForTopParty
                }
            );
        });

        let sortedRows = Helper.sortSMresultDesc(rows);

        sortedRows.push(
            {
                district: <strong style={{ float: 'right', marginRight: 10 }}>Iš viso:</strong>,
                counties: <strong>{this.state.counties}</strong>,
                confirmedCounties: <strong>{totalConfirmedCounties}</strong>,
                voterCount: <strong>{this.state.voters}</strong>,
                totalBallots: <strong>{this.state.totalBallots} ({this.state.percentTotalBallots}%)</strong>,
                spoiledBallots: <strong>{this.state.spoiledBallots} ({this.state.percentSpoiledBallots}%)</strong>,
                validBallots: <strong>{this.state.validBallots} ({this.state.percentValidBallots}%)</strong>,
                topCandidate: '',
                votesForTopParty: ''
            }
        );

        return rows;
    },
    getColumns() {
        return (
            [
                {
                    header: 'Apygarda',
                    accessor: 'district',
                    headerStyle: { fontWeight: 'bold' },
                    style: { marginLeft: 5 },
                    id: 1
                },
                {
                    header: 'Apylinkių',
                    accessor: 'counties',
                    headerStyle: { fontWeight: 'bold' },
                    style: { textAlign: 'center' },
                    width: 100,
                    id: 2
                },
                {
                    header: 'Pateikė rezultatus',
                    accessor: 'confirmedCounties',
                    headerStyle: { fontWeight: 'bold' },
                    style: { textAlign: 'center' },
                    id: 3
                },
                {
                    header: 'Rinkėjų skaičius',
                    accessor: 'voterCount',
                    headerStyle: { fontWeight: 'bold' },
                    style: { textAlign: 'center' },
                    id: 4
                },
                {
                    header: 'Dalyvavo',
                    accessor: 'totalBallots',
                    headerStyle: { fontWeight: 'bold' },
                    style: { textAlign: 'center' },
                    width: 100,
                    id: 5
                },
                {
                    header: 'Negaliojantys biuleteniai',
                    accessor: 'spoiledBallots',
                    headerStyle: { fontWeight: 'bold' },
                    style: { textAlign: 'center' },
                    id: 6
                },
                {
                    header: 'Galiojantys biuleteniai',
                    accessor: 'validBallots',
                    headerStyle: { fontWeight: 'bold' },
                    style: { textAlign: 'center' },
                    id: 7
                },
                {
                    header: 'TOP partija',
                    accessor: 'topParty',
                    headerStyle: { fontWeight: 'bold' },
                    style: { textAlign: 'center' },
                    width: 200,
                    id: 8
                },
                {
                    header: 'Balsai',
                    accessor: 'votesForTopParty',
                    headerStyle: { fontWeight: 'bold' },
                    style: { textAlign: 'center' },
                    width: 50,
                    id: 9
                }
            ]
        );
    },
    getOptions() {
        const array = [5, 10, 20];
        if (this.state.collection.length > 0) {
            const max = this.state.collection.length + 1;
            array.push(max);
            return Array.from(new Set(array.filter(i => { return i <= max })));
        } else {
            return array;
        }
    },
    render() {
        var rows = (this.state.collection.length > 0) ? this.state.collection.length + 1 : 5;
        return (
            <div>
                <h4 className="h4-election">2017 m. kovo 16 d. Lietuvos Respublikos Seimo rinkimai</h4>
                <h3>Balsavimo rezultatai vienmandatėse apygardose</h3>
                <div className="row narrowed" style={{ margin: '30px 0px 30px 0px' }}>
                    <div className="col-md-4"></div>
                    <div className="col-md-5">
                        <p className="small-p">Apylinkių skaičius - <strong>{this.state.counties}</strong></p>
                        <p className="small-p">Apygardų skaičius - <strong>{this.state.districts}</strong></p>
                        <p className="small-p">Pagal gautus iš apylinkių duomenis:</p>
                        <p className="small-p indented">
                            rinkėjų sąraše įrašyta rinkėjų – <strong>{this.state.voters}</strong>,
                            rinkimuose dalyvavo – <strong>{this.state.totalBallots}&nbsp;
                            ({this.state.percentTotalBallots}%)</strong>
                        </p>
                        <p className="small-p indented">
                            negaliojančių biuletenių – <strong>{this.state.spoiledBallots}&nbsp;
                            ({this.state.percentSpoiledBallots}%)</strong>,
                            galiojančių biuletenių – <strong>{this.state.validBallots}&nbsp;
                            ({this.state.percentValidBallots}%)</strong>
                        </p>
                    </div>
                    <div className="col-md-12">
                        <p className="centered-p">
                            Sąraše įrašytų ir rinkimuose dalyvavusių rinkėjų skaičiai skelbiami pagal duomenis
                            perdavusių rinkimų apylinkių protokolus.
                        </p>
                    </div>
                    <div className="col-md-12">
                        <h3>Balsavimo rezultatai apygardose</h3>
                    </div>
                </div>
                <ReactTable
                    data={this.prepareData()}
                    columns={this.getColumns()}
                    defaultPageSize={6}
                    showPageJump={false}
                    pageSizeOptions={this.getOptions()}
                    previousText='Ankstesnis'
                    nextText='Kitas'
                    loadingText='Kraunama...'
                    noDataText='Duomenų nėra'
                    pageText='Puslapis'
                    ofText='iš '
                    rowsText='eilučių'
                />
            </div>
        );
    }
});

module.exports = AllMMResultsView;