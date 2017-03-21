/**
 * Created by osvaldas on 17.1.20.
 */

var React = require('react');
var ReactRouter = require('react-router');
var Link = ReactRouter.Link;
var axios = require('axios');
var spring = require('../config/SpringConfig');
var CurrentTimeComponent = require('./tiny_components/CurrentTimeComponent');

var NavigationBarComponent = React.createClass ({
    getInitialState: function () {
        return ({
            pageTitle : 'LIETUVOS RESPUBLIKOS SEIMO RINKIMAI',
            logout : ' Atsijungti',
            userName: ' Lankytojas',
            homeButtonText : 'Pradžia',
            currentUser: this.props.currentUser
        })
    },
    componentWillReceiveProps(newProps) {
        if (newProps.currentUser != this.state.currentUser) this.setState({ currentUser: newProps.currentUser });
    },
    contextTypes: {
        router: React.PropTypes.object.isRequired
    },
    doLogout() {
        const _this = this;
        axios.post(spring.localHost.concat('/logout'))
            .then(resp => {
                _this.props.manageUser("LOGOUT");
                _this.context.router.push('/')
            })
            .catch(err => {
                console.log(err);
            })
    },
    formCredentials() {
        return this.state.currentUser.firstName.concat(" ").concat(this.state.currentUser.lastName);
    },
    dashboardLink() {
        let dashboardLink = undefined;
        if (this.props.currentUser.roles.includes("ROLE_REPRESENTATIVE")) {
            dashboardLink = '/atstovui';
        } else if (this.props.currentUser.roles.includes("ROLE_ADMIN")) {
            dashboardLink = '/administravimas';
        }

        return dashboardLink;
    },
    determineCurrentUserDisplay() {
        if (Object.keys(this.state.currentUser).length > 0) {
            return (
                <li className="dropdown">
                    <Link to="#"
                          id="user-menu"
                          className="dropdown-toggle"
                          data-toggle="dropdown"
                          role="button"
                          aria-haspopup="true"
                          aria-expanded="false"
                    >
                        <span className="glyphicon glyphicon-user" aria-hidden="true"></span> &nbsp;
                        {this.formCredentials()}
                        <span className="caret"></span>
                    </Link>
                    <ul className="dropdown-menu">
                        <li>
                            <Link id="user-menu-option1" to={this.dashboardLink()} style={{ cursor: 'pointer' }}>
                                <span className="glyphicon glyphicon-tasks" aria-hidden="true"></span> &nbsp;
                                Darbo aplinka
                            </Link>
                        </li>
                        <li>
                            <Link id="user-menu-option2-logout" to="" onClick={this.doLogout} style={{ cursor: 'pointer' }}>
                                <span className="glyphicon glyphicon-log-out" aria-hidden="true"></span> &nbsp;
                                {this.state.logout}
                            </Link>
                        </li>
                    </ul>
                </li>
            );
        } else {
            return <li className="dropdown"><Link to="/prisijungti">PRISIJUNGTI</Link></li>
        }
    },
    render: function () {
        var b1 = (this.props.mainMenu == '') ? {display : 'none'} : {display : 'inline'};
        var b2 = (this.props.secondMenu == '') ? {display : 'none'} : {display : 'inline'};
        var b3 = (this.props.thirdMenu == '') ? {display : 'none'} : {display : 'inline'};

        return (
            <nav className="navbar navbar-default" id="navigation">
                <div className="container-fluid">

                    <div className="navbar-header">

                        {/*<span className="navbar-brand" ><div>PAVADINIMAS</div></span>*/}
                        {/*<span className="navbar-brand" >{this.state.pageTitle}</span>*/}

                        <ul className="nav navbar-nav">
                            {/*<li><a id="homePageButton" onClick={this.props.homePageButtonPressed} href="#"><span className="glyphicon glyphicon-home" aria-hidden="true"></span> Pradžia </a></li>*/}
                            <li><div id="website-page-title" className="navBarTitle">{this.state.pageTitle}</div></li>
                            <Link to="/">
                                <button
                                    id="homeButton"
                                    style={{ backgroundColor: '#273B51' }}
                                    onClick={this.props.homePageButtonPressed}>
                                    <span className="glyphicon glyphicon-home" aria-hidden="true"></span>
                                    {this.state.homeButtonText}
                                </button>
                            </Link>
                        </ul>

                        {/*<button className="navBarButton" style={b1}> {this.props.mainMenu}</button>
                         <button className="navBarButton" style={b2}> {this.props.secondMenu}</button>
                         <button className="navBarButton" style={b3}> {this.props.thirdMenu}</button>*/}

                        {/*<span className="navbar-brand" >{this.props.mainMenu}</span>*/}
                        {/*<span className="navbar-brand" >{this.props.secondMenu}</span>*/}
                        {/*<span className="navbar-brand" >{this.props.thirdMenu}</span>*/}
                    </div>

                    <div className="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                        <ul className="nav navbar-nav navbar-right">
                            {this.determineCurrentUserDisplay()}
                        </ul>
                        <div style={{margin:"15px"}}>
                            <CurrentTimeComponent/>
                        </div>
                    </div>
                </div>
            </nav>
        );
    }
});

module.exports = NavigationBarComponent;
