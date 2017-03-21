var React = require('react');
var ReactRouter = require('react-router');
var Router = ReactRouter.Router;
var Route = ReactRouter.Route;
var IndexRoute = ReactRouter.IndexRoute;
var ReactDOM = require('react-dom');

var Application = require('./js/Application');
var HomePageContainer = require('./js/containers/HomePageContainer');
var AdminPanelComponent = require('./js/components/AdminPanelComponent');
var RepresentativeHomeContainer = require('./js/containers/RepresentativeHomeContainer');
var RepresentativeProfileComponent = require('./js/components/RepresentativeProfileComponent');

var TerritorialBreakdownContainer = require('./js/territorial_units/containers/TerritorialBreakdownContainer');
var PoliticalUnitsContainer = require('./js/political_units/containers/PoliticalUnitsContainer');
var SingleCandidatesContainer = require('./js/single_m_candidates/containers/SingleCandidatesContainer');
var NoMatch = require('./js/config/NoMatch');
var CountyRepresentativesDisplayContainer = require('./js/countyRepresentativesManagement/containers/CountyRepresentativesDisplayContainer');
var CountyResultsContainer = require('./js/counties_results/containers/CountyResultsContainer');
var AdminResultsViewContainer = require('./js/admin_results_view/containers/AdminResultsViewContainer');
var SearchCandidatesListContainer = require('./js/search/SearchCandidatesListContainer');
var Tester = require('./js/config/Tester');
const Login = require('./js/auth/Login');

var CountySMresultView = require('./js/public_views/CountySMresultView');
var DistrictSMresultView = require('./js/public_views/DistrictSMresultView');

var AllSMresultView = require('./js/public_views/AllSMresultView');
var AllMMResultsView = require('./js/public_views/AllMMResultsView');
var ConsolidatedResultsView = require('./js/public_views/ConsolidatedResultsView');

var CountyMMResultsView = require('./js/public_views/CountyMMResultsView');
var DistrictMMResultsView = require('./js/public_views/DistrictMMResultsView');

ReactDOM.render((
    <Router history={ReactRouter.hashHistory}>
        <Route path="/" component={Application}>
            <IndexRoute component={HomePageContainer}/>
            <Route path="administravimas" component={AdminPanelComponent}>
                <Route path="teritorinis-suskirstymas" component={TerritorialBreakdownContainer}/>
                <Route path="apygardu-kandidatai" component={SingleCandidatesContainer}/>
                <Route path="apylinkiu-atstovai" component={CountyRepresentativesDisplayContainer}/>
                <Route path="politinis-suskirstymas" component={PoliticalUnitsContainer}/>
                <Route path="apylinkiu-rezultatai" component={AdminResultsViewContainer}/>
            </Route>
            <Route path="atstovui" component={RepresentativeHomeContainer}>
                <IndexRoute component={RepresentativeProfileComponent}/>
                <Route path="profilis" component={RepresentativeProfileComponent}/>
                <Route path="rezultatai">
                    <Route path="vienmandaciai" component={CountyResultsContainer}/>
                    <Route path="daugiamandaciai" component={CountyResultsContainer}/>
                </Route>
            </Route>
            <Route path="paieska" component={SearchCandidatesListContainer}/>
            <Route path="prisijungti" component={Login}/>
            <Route path="apylinkes-vienmandaciai-rezultatai/:id" component={CountySMresultView}/>
            <Route path="apygardos-vienmandaciai-rezultatai/:id" component={DistrictSMresultView}/>

            <Route path="bendri-vienmandaciai-rezultatai" component={AllSMresultView}/>
            <Route path="bendri-daugiamandaciai-rezultatai" component={AllMMResultsView}/>
            <Route path="apylinkes-daugiamandaciai-rezultatai/:id" component={CountyMMResultsView}/>
            <Route path="apygardos-daugiamandaciai-rezultatai/:id" component={DistrictMMResultsView}/>
            <Route path="konsoliduoti-rezultatai" component={ConsolidatedResultsView}/>

            <Route path="*" component={NoMatch} />
        </Route>
    </Router>
), document.getElementById('root'));
