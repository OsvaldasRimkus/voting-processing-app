var React = require('react');
var Fields = require('.//Fields');
var styles = {
		"image": {width: 20, height: 20}
}

var Helpers = {
    dateTimeFormatWithMessage: function(millis, message) {
        if (millis == null) {
            return (
                <div>
                    <div className="list-group-item active location6" style={{'marginTop': 10}}>
                        {message}
                    </div>
                    <div className="list-group-item">
                        <span>Nėra duomenų</span>
                    </div>
                </div>
            );
        }

        var timeStamp = new Date(millis);
        var month = timeStamp.getMonth() + 1;
        var date = timeStamp.getDate();
        var hours = timeStamp.getHours();
        var mins = timeStamp.getMinutes();
        var secs = timeStamp.getSeconds();

        if (month < 10) month = "0" + month;
        if (date < 10) date = "0" + date;
        if (hours < 10) hours = "0" + hours;
        if (mins < 10) mins = "0" + mins;
        if (secs < 10) secs = "0" + secs;

        return (
            <div>
                <div className="list-group-item active location6" style={{'marginTop': 10}}>
                    {message}
                </div>
                <div className="list-group-item">
                    <img src="app/imgs/time.png" style={ styles.image }/> &nbsp;
                    <span>{timeStamp.getFullYear()}</span>
                    <span>/{month}</span>
                    <span>/{date} </span> &nbsp;
                    <span>{hours}</span>
                    <span>:{mins}</span>
                    <span>:{secs}</span>
                </div>
            </div>
        );
    },
    currentTime: function() {
        var timeStamp = new Date();
        var hours = timeStamp.getHours();
        var mins = timeStamp.getMinutes();
        var secs = timeStamp.getSeconds();

        if (hours < 10) hours = "0" + hours;
        if (mins < 10) mins = "0" + mins;
        if (secs < 10) secs = "0" + secs;

        return (
            <span id="current-time">
                <img src="app/imgs/alarm_clock.png" style={ styles.image }/> &nbsp;
                <span>{hours}</span>
                <span>:{mins}</span>
                <span>:{secs}</span>
            </span>
        );
    },
    sort(collection, ascending) {
        collection.sort(function(a, b) {
            let aa = a.props.unit.name;
            let bb = b.props.unit.name;

            //return aa.localeCompare(bb);
            if (aa > bb) {
                return 1;
            } else if (aa < bb) {
                return -1;
            }
            return 0;
        });
        return (ascending) ? collection : collection.reverse();
    },
    sortRepresentatives(collection, state) {
        const isSortingByName = state.field === Fields.name;

        collection.sort(function(a, b) {
            let aa; let bb;
            if (isSortingByName) {
                aa = a.props.unit.firstName;
                bb = b.props.unit.firstName;
            } else {
                aa = a.props.unit.county.name;
                bb = b.props.unit.county.name;
            }

            //return aa.localeCompare(bb);
            if (aa > bb) {
                return 1;
            } else if (aa < bb) {
                return -1;
            }
            return 0;
        });

        if (isSortingByName) {
            return (state.ASCname) ? collection : collection.reverse();
        } else {
            return (state.ASCcounty) ? collection : collection.reverse();
        }
    },
    sortSMresultDesc(collection) {
        collection.sort(function(a, b) {
            let aa; let bb;
            aa = a.voteCount;
            bb = b.voteCount;

            if (aa < bb) {
                return 1;
            } else if (aa > bb) {
                return -1;
            }
            return 0;
        });

        return collection;
    }
};

module.exports = Helpers;
