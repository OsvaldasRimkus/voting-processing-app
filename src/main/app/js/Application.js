var React = require('react');
var axios = require('axios');
var NavigationBarComponent = require('./components/NavigationBarComponent');
var spring = require('./config/SpringConfig');

var representative = {
    "id": 1,
    "firstName": "Rep",
    "lastName": "Rep",
    "username": "rep.rep",
    "email": "email@email.lt",
    "county": { "id": 1, "name": "TEST COUNTY", "voterCount": 10000, "address": "TEST ADDRESS" },
    "district": { "id": 1, "name": "TEST DISTRICT", "link": "http://dummy" },
    "roles": ["ROLE_REPRESENTATIVE"]
};

var admin = {
    "id": 1,
    "firstName": "Admin",
    "lastName": "Admin",
    "username": "admin.admin",
    "roles": ["ROLE_ADMIN"]
};

var Application = React.createClass({
    contextTypes: {
        router: React.PropTypes.object.isRequired
    },
    getInitialState() {
        return ({ currentUser: {} });
    },
    getInitialState() {
        return ({ currentUser: {} });
    },
    getPrincipal() {
        const _this = this;
        axios.post(spring.localHost.concat('/api/auth/principal'))
            .then(resp => {
                if (resp.data != "") _this.setState({ currentUser: resp.data });
            })
            .catch(err => {
                console.log(err);
            });
    },
    componentDidMount() {
        this.getPrincipal();
    },
    manageUser(command) {
        if (command === "LOGOUT") {
            this.setState({ currentUser: {} });
        } else if (command === "LOGIN") {
            this.getPrincipal();
        }
    },
    render() {
        return (
			<div className="container" id="app">
				<NavigationBarComponent
					currentUser={ this.state.currentUser }
					manageUser={this.manageUser}
				/>
                {React.cloneElement(
                    this.props.children,
                    {manageUser: this.manageUser, currentUser: this.state.currentUser}
                )}
			</div>
        );
    }
});

module.exports = Application;
