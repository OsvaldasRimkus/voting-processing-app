var React = require('react');
var NewPartyAsideForm = require('../containers/NewPartyAsideForm');
var Helper = require('../../utils/Helper');

var PoliticalUnitsComponent = React.createClass({
    getInitialState() {
        return ({ ASC: true });
    },
    toggleSortOrder() {
        this.setState({ ASC: !this.state.ASC });
    },
    sortParties() {
        return Helper.sort(this.props.parties, this.state.ASC);
    },
    render() {
        var rotation = (this.state.ASC) ? " Z-A" : "A-Z";
        return (
            <div className="container">
                <div className="row grayed">
                    <div className="col-md-8 units-list-area">
                        <div className="list-group-item active location4" style={{display: "inline-block", verticalAlign: "middle", width: "100%", position: "relative", zIndex: "0" }}>
                            <span id = "party-list-header">Partijų sąrašas</span>
                            <span
                                id="sorting-button"
                                className="btn btn-sm btn-success no-background"
                                style={{ color: '#FFFFFF', borderColor: '#FFFFFF'}}
                                onClick={this.toggleSortOrder}>
                                Rūšiuoti {rotation}
                            </span>
                        </div>
                        <div className="list-group-item" style={{ height: 'auto' }}>
                            {this.sortParties()}
                        </div>
                    </div>
                    <div className="col-md-4 units-create-area">
                        <div className="col-md-11">
                            <NewPartyAsideForm
                                create={this.props.create}
                                springErrors={this.props.springErrors}
                                popupAlert={this.props.popupAlert}
                            />
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});

module.exports = PoliticalUnitsComponent;
