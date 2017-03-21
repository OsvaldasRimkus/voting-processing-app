var React = require('react');
var axios = require('axios');
var fileDownload = require('react-file-download');
var ReactTable = require('react-table').default;
var spring = require('./SpringConfig');

var Tester = React.createClass({
    downloadCSV() {
        axios.get('http://localhost:8080/api/download/testers')
             .then(resp => {
                fileDownload(resp.data, "testData.csv");
                console.log(resp);
             })
             .catch(err => {
                console.log(err);
             });
    },
    render: function() {
        return (
            <div>
                CONSOLIDATED
            </div>
        );
    }
});

module.exports = Tester;