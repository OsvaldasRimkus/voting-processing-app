var React = require('react');
var HomePageComponent = require('../components/HomePageComponent');

var HomePageContainer = React.createClass({
    getInitialState: function () {
        return ({
            search : "KANDIDATŲ PAIEŠKA",
            smResults : "VIENMANDAČIAI REZULTATAI",
            mmResults : "DAUGIAMANDAČIAI REZULTATAI",
            finalResults : "SUVESTINIAI REZULTATAI"
        })
    },
    render: function() {
        return <HomePageComponent
                    search={this.state.search}
                    smResults={this.state.smResults}
                    mmResults={this.state.mmResults}
                    finalResults={this.state.finalResults}
               />
    }
});

module.exports = HomePageContainer;
