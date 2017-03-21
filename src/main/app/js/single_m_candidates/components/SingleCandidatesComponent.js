var React = require('react');
var Helper = require('../../utils/Helper');

var timer = null;

var SingleCandidatesComponent = React.createClass({
    getInitialState() {
        return ({ ASC: true, needToSort: false, query: '' });
    },
    toggleSortOrder() {
        this.setState({ ASC: !this.state.ASC });
    },
    sortDistricts() {
        return (this.state.needToSort) ? Helper.sort(this.props.districts.filter(this.filterByQuery), this.state.ASC) : this.props.districts.filter(this.filterByQuery);
    },
    onKeyUp() {
        var value = document.getElementById("representatives-search").value;
        var _this = this;

        $('#representatives-search').keyup(function() {
            clearTimeout(timer);
            timer = setTimeout(_this.handleChangeQuery.bind(_this, value), 500);
        })
    },
    handleChangeQuery: function(value) {
        this.setState({ query: value.toLowerCase() });
    },
    filterByQuery(district) {
        return district.props.unit.name.toLowerCase().includes(this.state.query);
    },
    render() {
        var rotation = (this.state.ASC) ? " Z-A" : "A-Z";
        return (
            <div className="container">
                <div className="row grayed">
                    <div className="col-md-8 units-list-area">
                        <div className="list-group-item location2" style={{display: "inline-block", verticalAlign: "middle", width: "100%", color: 'white', position: "relative", zIndex: "0" }}>
                            <span id="district-list-header">Apygardų sąrašas</span>
                            <span
                                id="sort-districts-button"
                                className="btn btn-sm btn-success no-background"
                                style={{ color: '#FFFFFF', borderColor: '#FFFFFF'}}
                                onClick={this.toggleSortOrder}>
                                Rūšiuoti {rotation}
                            </span>
                            <div className className="inline-search">
                                <form>
                                    <div className="input-group">
                                        <input
                                            className="form-control"
                                            id="representatives-search"
                                            placeholder="Ieškoti apygardos"
                                            onChange={this.onKeyUp}
                                        />
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div className="list-group-item" style={{ height: 'auto' }}>
                            {this.sortDistricts()}
                        </div>
                    </div>

                    <div className="col-md-4 units-create-area"></div>
                </div>
            </div>
        );
    }
});

module.exports = SingleCandidatesComponent;
