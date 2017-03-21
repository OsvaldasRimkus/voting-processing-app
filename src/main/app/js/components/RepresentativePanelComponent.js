var React = require('react');
var ReactRouter = require('react-router');
var Link = ReactRouter.Link;

var styles = {
    "active-location6": {backgroundColor: '#77A0AA', color:"white"},
    "active-location7": {backgroundColor: '#77A0AA', color:"white"},
    "active-location8": {backgroundColor: '#77A0AA', color:"white"},
    "passive": {backgroundColor: '#D8DADB', color:"#006B96"},
    "image": {width: 60, height: 60, marginBottom: 10}
}

var RepresentativesPanelComponent = React.createClass({
    getInitialState() {
        return ({ tagIds: this.setBackgroundsByLocation() });
    },
    contextTypes: {
        router: React.PropTypes.object.isRequired
    },
    resetButtonBackgrounds: function() {
        this.setState({
            tagIds: {
                location6: styles.passive,
                location7: styles.passive,
                location8: styles.passive
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
        if (nextProps.location.pathname === '/atstovui') this.resetButtonBackgrounds();
        if (nextProps.location.pathname === '/atstovui/rezultatai/vienmandaciai') this.setBackgrounds(location6);
        if (nextProps.location.pathname === '/atstovui/rezultatai/daugiamandaciai') this.setBackgrounds(location7);
        if (nextProps.location.pathname === '/atstovui/profilis') this.setBackgrounds(location8);
    },
    setBackgroundsByLocation: function() {
        var tagIds = {
            location6: styles.passive,
            location7: styles.passive,
            location8: styles.passive
        };

        switch (this.context.router.location.pathname) {
            case '/atstovui/rezultatai/vienmandaciai':
                tagIds.location6 = styles["active-location6"];
                break;
            case '/atstovui/rezultatai/daugiamandaciai':
                tagIds.location7 = styles["active-location7"];
                break;
            case '/atstovui/profilis':
                tagIds.location8 = styles["active-location8"];
                break;
        }

        return tagIds;

    },
    render: function() {
        return (
			<div className="menu">
				<ul className="nav nav-tabs narrow" id="bootstrap-overrides-nav-tabs">
					<li className="tab centered widened">
						<Link
							to={"atstovui/rezultatai/vienmandaciai"}
							className="adminPanelButton"
                            id="location6"
							style={this.state.tagIds.location6}>
                            <span className="glyphicon glyphicon-stats big-glyph" aria-hidden="true" style={{ marginBottom: 10 }}></span>
							<p>Apylinkės rezultatai (VIENMANDAČIAI)</p>
						</Link>
					</li>
					<li className="tab centered widened">
						<Link
							to={"atstovui/rezultatai/daugiamandaciai"}
							className="adminPanelButton"
                            id="location7"
							style={this.state.tagIds.location7}>
                            <span className="glyphicon glyphicon-stats big-glyph" aria-hidden="true" style={{ marginBottom: 10 }}></span>
							<p>Apylinkės rezultatai (DAUGIAMANDAČIAI)</p>
						</Link>
					</li>
					<li className="tab centered widened">
						<Link
							to={"atstovui/profilis"}
							className="adminPanelButton"
                            id="location8"
							style={this.state.tagIds.location8}>
							<img src="app/imgs/user.png" style={ styles.image }/>
							<p style={{ marginBottom: 0 }}>Atstovo</p>
							<p style={{ marginBottom: 0 }}>profilis</p>
						</Link>
					</li>
				</ul>
			</div>
        );
    }
});

module.exports = RepresentativesPanelComponent;
