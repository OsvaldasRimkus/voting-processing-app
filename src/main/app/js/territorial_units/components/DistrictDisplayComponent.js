var React = require('react');
var ConfirmAction = require('../../components/tiny_components/ConfirmAction');
var InlineEditComponent = require('../../components/tiny_components/InlineEditComponent');

var DistrictDisplayComponent = React.createClass({
    getInitialState() {
        return ({ update: false, unit: this.props.unit });
    },
    toggleEdit() { this.setState({ update: !this.state.update }) },
    updateOrDisplay() {
        if (this.state.update) {
            return (
                <InlineEditComponent
                    update={this.updateSelf}
                    toggleUpdate={this.toggleUpdate}
                    unit={this.props.unit}
                    parent={"DISTRICT"}
                />
            );
        } else {
            return (
                <div>
                    <div className="unit-name-area- location1"
                        onClick={this.props.toggleCountiesList}
                        style={{ cursor: 'pointer' }}
                    >
                        {this.state.unit.name}
                    </div>
                </div>
            );
        }
    },
    updateSelf(updatedUnit) { this.setState({ unit: updatedUnit, update: false }) },
    toggleUpdate() { this.setState({ update: !this.state.update }) },
    rotation() { return (this.props.ASC) ? " Z-A" : "A-Z" },
    actions() {
        if (this.props.show) {
            return (
                <div>
                    <div className="list-group-item">
                        <ConfirmAction
                            title="Ar tikrai norite pašalinti apygardą?"
                            body="Duomenų atstatymas neįmanomas."
                            onConfirm={this.props.delete}
                        >
                            <p className="remove-units-element confirmation-buttons">
                                <span className="glyphicon glyphicon-remove-sign">
                                </span>&nbsp;
                                <span id={"remove-district-" + this.state.unit.name}>Šalinti apygardą</span>
                            </p>
                        </ConfirmAction>
                        <p  id={"edit-district-"+this.state.unit.name} className="remove-units-element confirmation-buttons"
                            onClick={this.toggleEdit}
                        >
                            <span className="glyphicon glyphicon-edit">
                            </span>&nbsp;
                            Redaguoti apygardą
                        </p>
                    </div>
                    <div
                        className="list-group-item"
                        style={{ textAlign: 'center' }}>
                        <strong id={"counties-header-" + this.state.unit.name}>Apylinkės</strong>
                        <span
                            id={"sorting-button-" + this.state.unit.name}
                            className="btn btn-sm btn-default"
                            style={{ marginLeft: 10 }}
                            onClick={this.props.toggleSortOrder}>
                            Rušiuoti {this.rotation()}
                        </span>
                    </div>
                </div>
            );
        } else {
            return [];
        }
    },
    render() {
        return (
            <div className="unit">
                <div className="list-group-item active location1" id={"district-" + this.state.unit.name}>
                    {this.updateOrDisplay()}
                </div>
                {this.actions()}
                {this.props.counties}
            </div>
        );
    }
}); 

module.exports = DistrictDisplayComponent;
