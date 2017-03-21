var React = require('react');

var SpoiledBallotsInputComponent = React.createClass({
    render: function() {
        return (
            <div className="unit">
                <div className="list-group-item active location6">
                    <div id="header-title-spoiled-votes">
                        Apylinkės sugadintų biuletenių skaičius
                    </div>
                </div>
                <div>
                    <div className="list-group-item">
                        <p className="county-results">
                            Sugadintų biuletinių skaičius:
                            <input id="input-field-spoiled-votes" type="text" className="form-control county-results-input" onChange={this.props.changeSpoiled} value={this.props.spoiled}/>
                        </p>
                    </div>
                </div>

            </div>
        );
    }
});

module.exports = SpoiledBallotsInputComponent;
