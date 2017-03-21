var React = require('react');
var truncate = require('truncate');
var truncateLength = 19;

var CountyDetailsCard = React.createClass({
    propTypes: {
        representative: React.PropTypes.object.isRequired
    },
    popupInfo(e) {
        if (e.target.textContent.length > truncateLength) {
            $("." + e.target.className).popover({ trigger: "click" })
        };
    },
    handStyle(value) {
        return (value.length > truncateLength) ? {cursor: 'pointer'} : {};
    },
    render: function() {
        let rep = this.props.representative;
        return (
            <div>
                <div className="list-group-item active location6">
                    Apylinkės duomenys
                </div>
                <div className="list-group-item">
                    <div className="row narrowed" style={{ backgroundColor: 'white' }}>
                        <div className="col col-md-5" style={{ padding: 0, textAlign: 'left' }}>
                            <span className="glyphicon glyphicon-list-alt"></span> &nbsp;
                            <span><u>Pavadinimas</u></span> <br />

                            <span className="glyphicon glyphicon-user"></span> &nbsp;
                            <span><u>Balsuotojų</u></span> <br />

                            <span className="glyphicon glyphicon-map-marker"></span> &nbsp;
                            <span><u>Adresas</u></span> <br />

                            <hr style={{ margin: 10 }} />

                            <span className="glyphicon glyphicon-list-alt"></span> &nbsp;
                            <span><u>Apygarda</u></span> <br />
                        </div>
                        <div className="col col-md-7" style={{ padding: 0, textAlign: 'left' }}>
                            <span
                                id="county-title-info"
                                className="popoverCountyName"
                                onMouseOver={this.popupInfo}
                                data-content={rep.county.name}
                                rel="popover"
                                data-placement="top"
                                style={this.handStyle(rep.county.name)}
                            >
                                {truncate(rep.county.name, truncateLength)}
                            </span><br />
                            <span id="voters-count-info">{rep.county.voterCount}</span> <br />
                            <span
                                id="county-address-info"
                                className="popoverAddress"
                                onMouseOver={this.popupInfo}
                                data-content={rep.county.address}
                                rel="popover"
                                data-placement="top"
                                style={this.handStyle(rep.county.address)}
                            >
                                {truncate(rep.county.address, truncateLength)}
                            </span> <br />

                            <hr style={{ margin: 10 }} />

                            <span
                                id="district-title-info"
                                className="popoverDistrictName"
                                onMouseOver={this.popupInfo}
                                data-content={rep.district.name}
                                rel="popover"
                                data-placement="top"
                                style={this.handStyle(rep.district.name)}
                            >
                                {truncate(rep.district.name, truncateLength)}
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});

module.exports = CountyDetailsCard;