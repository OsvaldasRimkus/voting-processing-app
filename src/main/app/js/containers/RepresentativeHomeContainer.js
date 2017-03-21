var React = require('react');
var RepresentativePanelComponent = require('../components/RepresentativePanelComponent');
var axios = require('axios');
var spring = require('../config/SpringConfig');

var RepresentativeHomeContainer = React.createClass({
    propTypes: {

    },
    contextTypes: {
        router: React.PropTypes.object.isRequired
    },
    getInitialState() {
        return {
            representative: false, currentUser: this.props.currentUser
        };
    },
    componentDidMount() {
        const _this = this;
        let fd = new FormData();
        fd.append("role", "ROLE_REPRESENTATIVE");

        axios.post(spring.localHost.concat('/api/auth/role'), fd)
            .then(resp => {
                if (resp.data == false) {
                    _this.context.router.push('/')
                } else {
                    _this.setState({ representative: true });
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
            this.setState({ representative: false });
        }
    },
    render: function() {
        let displayer;
        let childrenWithProps = React.Children.map(this.props.children, (child) =>
            React.cloneElement(child, {
                representative: this.props.currentUser,
                county: this.props.currentUser.county,
                district: this.props.currentUser.district
            })
        );
        if (this.state.representative) {
            displayer = (
                <div>
                    <RepresentativePanelComponent location={this.props.location} />
                    <div className="main-layout">
                        {childrenWithProps}
                    </div>
                </div>
            );
        } else {
            displayer = <div></div>
        }

        return displayer;
    }
});

module.exports = RepresentativeHomeContainer;