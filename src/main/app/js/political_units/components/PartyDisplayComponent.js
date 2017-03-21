var React = require('react');
var InlineEditComponent = require('../../components/tiny_components/InlineEditComponent');

var PartyDisplayComponent = React.createClass({
    getInitialState() {
        return ({ update: false, unit: this.props.unit });
    },
    scrollUp() {
        window.scrollTo(0, 0);
    },
    toggleEdit() {
        this.setState({ update: !this.state.update });
    },
    updateOrDisplay() {
        if (this.state.update) {
            return (
                <InlineEditComponent
                    update={this.updateSelf}
                    toggleUpdate={this.toggleUpdate}
                    unit={this.props.unit}
                    parent={"PARTY"}
                />
            );
        } else {
            return (
                <div>
                    <div onClick={this.props.toggleShow} style={{ cursor: 'pointer' }}>
                        {this.state.unit.name}
                    </div>
                </div>
            );
        }
    },
    updateSelf(updatedUnit) {
        this.setState({ unit: updatedUnit, update: false });
    },
    toggleUpdate() {
        this.setState({ update: !this.state.update });
    },
    render() {
        return (
            <div className="unit">
                <div className="list-group-item active location4" style={{position: "relative", zIndex: "0" }} id={"party-" + this.state.unit.name}>
                    {this.updateOrDisplay()}
                </div>
                <div style={ this.props.display }>
                    <div className="list-group-item">
                        {this.props.actions}
                        {this.props.confirmDeleteParty}
                        <p id = {"edit-party-" + this.state.unit.name} className="remove-units-element confirmation-buttons"
                            onClick={this.toggleEdit}    
                        >
                            <span className="glyphicon glyphicon-edit">
                            </span> &nbsp;
                            Redaguoti partiją
                        </p>
                        <b id = "wait-screen" style={this.props.displayLoadingIcon}>Prašome palaukti&nbsp;</b>
                        <img style={this.props.displayLoadingIcon} src="app/imgs/axios-loader.gif" alt="working-hard"/>
                    </div>
                    <div>
                        <buttton className="btn btn-default btn-sm" style={{ width: '100%' }}></buttton>
                        {this.props.candidates}
                        <buttton className="btn btn-default btn-sm" style={{ width: '100%' }}></buttton>
                    </div>
                    <buttton id = {"scroll-up-button-" + this.state.unit.name} className="btn btn-default btn-sm" onClick={this.scrollUp}>UP</buttton>
                </div>
            </div>
        );
    }
});


module.exports = PartyDisplayComponent;
