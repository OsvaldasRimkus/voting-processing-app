var React = require('react');
var ReactRouter = require('react-router');
var Link = ReactRouter.Link;

var HomePageComponent = React.createClass({
    render: function () {
        return (
            <div id="homePageButtonsRow">

                <div className="firstDivAfterHeader"></div>

                <div className="homePageButtonsRow">
                    <div className="col-sm-3">
                        <Link to="paieska" className="NoUnderlineLink" title="title">
                            <button className="frontPageButton" id="candidate-search-button"><span className="glyphicon glyphicon-search"></span><div className="innerButtonDiv">{this.props.search}</div></button>
                        </Link>
                    </div>
                    <div className="col-sm-3">
                        <Link to="bendri-vienmandaciai-rezultatai" className="NoUnderlineLink" title="title">
                            <button className="frontPageButton" id="single-mandate-results-button"><span className="glyphicon glyphicon-knight"></span><div className="innerButtonDiv">{this.props.smResults}</div></button>
                        </Link>
                    </div>
                    <div className="col-sm-3">
                        <Link to="bendri-daugiamandaciai-rezultatai" className="NoUnderlineLink" title="title">
                            <button className="frontPageButton" id="multi-mandate-results-button"><span className="glyphicon glyphicon-bishop"></span><div className="innerButtonDiv">{this.props.mmResults}</div></button>
                        </Link>
                    </div>
                    <div className="col-sm-3">
                        <Link to="konsoliduoti-rezultatai" className="NoUnderlineLink" title="title">
                            <button className="frontPageButton" id="combined-results-button"><span className="glyphicon glyphicon-king"></span><div className="innerButtonDiv">{this.props.finalResults}</div></button>
                        </Link>
                    </div>
                </div>
            </div>
        )
    }
});

module.exports = HomePageComponent;
