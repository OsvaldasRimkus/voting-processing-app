const React = require('react');
const axios = require('axios');
const LoginComponent = require('./LoginComponent');
const spring = require('../config/SpringConfig');

const Login = React.createClass({
    contextTypes: {
        router: React.PropTypes.object.isRequired
    },
    getInitialState() {
        return ({ username: "", password: "", loginError: false, currentUser: this.props.currentUser });
    },
    componentDidMount() {
        if (Object.keys(this.state.currentUser).length > 0) this.context.router.push('/');
    },
    componentWillReceiveProps(newProps) {
        if (newProps.currentUser != this.state.currentUser) this.setState({ currentUser: newProps.currentUser })
    },
    doLogin(e) {
        e.preventDefault();
        const _this = this;
        let fd = new FormData();
        fd.append("username", this.state.username);
        fd.append("password", this.state.password);

        axios.post(
                spring.localHost.concat('/login'),
                fd,
                { headers: { 'Content-Type': 'multipart/form-data' } }
            )
            .then(resp => {
                if (resp.data === "REPRESENTATIVE") {
                    _this.context.router.push("/atstovui");
                } else if (resp.data === "ADMIN") {
                    _this.context.router.push("/administravimas");
                } else {
                    _this.setState({ loginError: true });
                    //_this.context.router.push("/");
                }
                _this.props.manageUser("LOGIN");
            })
            .catch(err => {
                console.log(err);
                _this.setState({ loginError: true });
            })
    },
    handlePasswordChange(e) {
        this.setState({ password: e.target.value })
    },
    handleNameChange(e) {
        this.setState({ username: e.target.value })
    },
    prepareloginError() {
        let error = [];
        if (this.state.loginError) {
            error =(
                <div className="form-group alert alert-danger login">
                    <label>Neteisingi prisijungimo duomenys</label>
                </div>
            );
        }
        return error;
    },
    render: function() {
        return (
            <LoginComponent
                username={this.state.username}
                password={this.state.password}
                changeName={this.handleNameChange}
                changePassword={this.handlePasswordChange}
                doLogin={this.doLogin}
                loginError={this.prepareloginError()}
            />
        );
    }
});

module.exports = Login;