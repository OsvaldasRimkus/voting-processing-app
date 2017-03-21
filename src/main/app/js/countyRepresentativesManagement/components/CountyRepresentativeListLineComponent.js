/**
 * Created by osvaldas on 17.2.7.
 */

var React = require('react');
var ConfirmAction = require('../../components/tiny_components/ConfirmAction');
var truncate = require('truncate');
var truncLength = 20;

var CountyRepresentativeListLineComponent = React.createClass ({
    territorialTitle() {
        return this.props.unit.county.name.concat(" (" + this.props.unit.district.name + ")");
    },
    unitCredentials() {
        return this.props.unit.firstName + " " + this.props.unit.lastName;
    },
    onRemoveRep() {
        this.props.onDeleteRepresentative(this.props.unit.id, this.props.index);
    },
    popupTruncated(title, clazz) {
        if (title.length > truncLength) {
            $("." + clazz).popover({ trigger: "click" })
        }
    },
    render() {
        var handStyle = this.territorialTitle().length > truncLength ? {cursor: 'pointer'} : {};
        return (
            <div className="list-group-item passive">
                <div>
                    <div style={{height: 20}}>

                        <div id={"representative-name-and-surname-" + this.unitCredentials()} className="col-md-4">{this.unitCredentials()}</div>

                        {/*<div className="col-md-2">{this.props.unit.firstName}</div>*/}
                        {/*<div className="col-md-2">{this.props.unit.lastName}</div>*/}
                        <div
                            id={"representative-location-" + this.unitCredentials()}
                            className={"col-md-4 popovering-" + this.props.index}
                            onClick={this.popupTruncated.bind(this, this.territorialTitle(), "popovering-" + this.props.index)}
                            data-content={this.territorialTitle()}
                            rel="popover"
                            data-placement="top"
                            style={ handStyle }
                        >
                            {this.territorialTitle().substring(0, truncLength+1)}
                        </div>

                        <div
                             id={"representative-email-" + this.unitCredentials()}
                             className={"col-md-3 popovering-" + this.props.index}
                             onClick={this.popupTruncated.bind(this, this.props.unit.email, "popovering-" + this.props.index)}
                             data-content={this.props.unit.email}
                             rel="popover"
                             data-placement="top"
                             style={ handStyle }
                        >
                            {truncate(this.props.unit.email.substring(0, truncLength+1))}
                        </div>
                        <div className="col-md-1" style={{ textAlign: 'center' }}>
                            <ConfirmAction
                                title={"Norite pašalinti " + this.unitCredentials() + " iš " + this.props.unit.county.name +" apylinkės atstovo pareigų ?"}
                                body="Duomenų atstatymas bus neįmanomas."
                                onConfirm={this.onRemoveRep}
                            >
                                <span
                                    className="glyphicon glyphicon-remove-sign remove-representative confirmation-buttons"
                                    id={"remove-representative-" + this.unitCredentials()}
                                >
                                </span>
                            </ConfirmAction>
                        </div>

                    </div>
                </div>
            </div>
        )
    }
});

module.exports = CountyRepresentativeListLineComponent;
