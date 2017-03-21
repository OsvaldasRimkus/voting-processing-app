var React = require('react');
var ReactRouter = require('react-router');
var Link = ReactRouter.Link;
var axios = require('axios');
var spring = require('../config/SpringConfig');

var styles = {
    "active-location1": {backgroundColor: '#9D9FB5', color:"white"},
    "active-location2": {backgroundColor: '#B8A668', color:"white"},
    "active-location3": {backgroundColor: '#77A0AA', color:"white"},
    "active-location4": {backgroundColor: '#8AB28D', color:"white"},
    "active-location5": {backgroundColor: '#C47752', color:"white"},
    "passive": {backgroundColor: '#D8DADB', color:"#006B96"},
    "image": {width: 60, height: 60, marginBottom: 10}
}

var AdminPanelComponent = React.createClass({
    getInitialState() {
        return ({ tagIds: this.setBackgroundsByLocation(), currentUser: this.props.currentUser, admin: false });
    },
    contextTypes: {
        router: React.PropTypes.object.isRequired
    },
    componentDidMount() {
        const _this = this;
        let fd = new FormData();
        fd.append("role", "ROLE_ADMIN");
        axios.post(spring.localHost.concat('/api/auth/role'), fd)
            .then(resp => {
                if (resp.data == false) {
                    _this.context.router.push('/')
                } else {
                    _this.setState({ admin: true });
                }
            })
            .catch(err => {
                console.log(err);
            });
    },
    componentWillReceiveProps(newProps) {
        let loggedOut = Object.keys(newProps.currentUser).length == 0;
        if (newProps.currentUser != this.state.currentUser || loggedOut) {
            this.context.router.push('/');
            this.setState({ admin: false });
        }
    },
    resetButtonBackgrounds: function() {
        this.setState({
            tagIds: {
                location1: styles.passive,
                location2: styles.passive,
                location3: styles.passive,
                location4: styles.passive,
                location5: styles.passive
            }
        });
    },
    setBackgrounds: function(tag) {
        var stateObj = this.state;
        var newState = {};

        Object.keys(stateObj.tagIds).forEach(key => {
            var value = (key === tag.id) ? styles["active-" + key] : styles.passive;
            newState[key] = value;
        });
        this.setState({ tagIds: newState });
    },
    componentWillReceiveProps: function(nextProps) {
        if (nextProps.location.pathname === '/administravimas') {
            this.resetButtonBackgrounds();
        }
        if (nextProps.location.pathname === '/administravimas/teritorinis-suskirstymas') {
            this.setBackgrounds(location1);
        }
        if (nextProps.location.pathname === '/administravimas/apygardu-kandidatai') {
            this.setBackgrounds(location2);
        }
        if (nextProps.location.pathname === '/administravimas/apylinkiu-atstovai') {
            this.setBackgrounds(location3);
        }
        if (nextProps.location.pathname === '/administravimas/politinis-suskirstymas') {
            this.setBackgrounds(location4);
        }
        if (nextProps.location.pathname === '/administravimas/apylinkiu-rezultatai') {
            this.setBackgrounds(location5);
        }
    },
    setBackgroundsByLocation: function() {
        var tagIds = {
            location1: styles.passive,
            location2: styles.passive,
            location3: styles.passive,
            location4: styles.passive,
            location5: styles.passive
        }

        switch (this.props.location.pathname) {
            case '/administravimas/teritorinis-suskirstymas':
                tagIds.location1 = styles["active-location1"];
                break;
            case '/administravimas/apygardu-kandidatai':
                tagIds.location2 = styles["active-location2"];
                break;
            case '/administravimas/apylinkiu-atstovai':
                tagIds.location3 = styles["active-location3"];
                break;
            case '/administravimas/politinis-suskirstymas':
                tagIds.location4 = styles["active-location4"];
                break;
            case '/administravimas/apylinkiu-rezultatai':
                tagIds.location5 = styles["active-location5"];
        }

        return tagIds;

    },
    render: function() {
        var displayer;
        if (this.state.admin) {
            displayer = (
				<div>
					<div className="menu">
						<ul className="nav nav-tabs" id="bootstrap-overrides-nav-tabs">
							<li className="tab">
								<Link
									to="administravimas/teritorinis-suskirstymas"
									className="adminPanelButton"
									id="location1"
									style={this.state.tagIds.location1}>
                                    <span className="glyphicon glyphicon-globe big-glyph" aria-hidden="true" style={{ marginBottom: 10 }}></span>
                                    <p style={{ marginBottom: 0 }}>Teritoriniai</p>
                                    <p>vienetai</p>
								</Link>
							</li>
							<li className="tab">
								<Link
									to="administravimas/apygardu-kandidatai"
									className="adminPanelButton"
									id="location2"
									style={this.state.tagIds.location2}>
                                    <span className="glyphicon glyphicon-apple big-glyph" aria-hidden="true" style={{ marginBottom: 10 }}></span>
                                    <p style={{ marginBottom: 0 }}>Apygardų</p>
                                    <p>kandidatai</p>
								</Link>
							</li>
							<li className="tab">
								<Link
									to="administravimas/apylinkiu-atstovai"
									className="adminPanelButton"
									id="location3"
									style={this.state.tagIds.location3}>
                                    <span className="glyphicon glyphicon-grain big-glyph" aria-hidden="true" style={{ marginBottom: 10 }}></span>
                                    <p style={{ marginBottom: 0 }}>Apylinkių</p>
                                    <p>atstovai</p>
								</Link>
							</li>
							<li className="tab">
								<Link
									to="administravimas/politinis-suskirstymas"
									className="adminPanelButton"
									id="location4"
									style={this.state.tagIds.location4}>
                                    <span className="glyphicon glyphicon-piggy-bank big-glyph" aria-hidden="true" style={{ marginBottom: 10 }}></span>
                                    <p style={{ marginBottom: 0 }}>Politiniai</p>
                                    <p>vienetai</p>
								</Link>
							</li>
							<li className="tab">
								<Link
									to="administravimas/apylinkiu-rezultatai"
									className="adminPanelButton"
									id="location5"
									style={this.state.tagIds.location5}>
                                    <span className="glyphicon glyphicon-stats big-glyph" aria-hidden="true" style={{ marginBottom: 10 }}></span>
                                    <p style={{ marginBottom: 0 }}>Apylinkių</p>
                                    <p>rezultatai</p>
								</Link>
							</li>
						</ul>
					</div>
					<div className="main-layout">
                        {this.props.children}
					</div>
				</div>
            );
        } else {
            displayer = <div></div>
        }

        return displayer;
    }
});

module.exports = AdminPanelComponent;
