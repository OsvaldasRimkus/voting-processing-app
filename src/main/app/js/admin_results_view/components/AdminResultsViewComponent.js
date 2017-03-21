var React = require('react');
var Helper = require('../../utils/Helper');

var AdminResultsViewComponent = React.createClass({
    getInitialState() {
        return ({ ASC: true, needToSort: false });
    },
    componentWillReceiveProps(props) {
        if (props.activeDistrict.length > 0) document.getElementById('territorial-search').value = '';
    },
    toggleSortOrder() {
        this.setState({ ASC: !this.state.ASC, needToSort: true });
    },
    sortCounties() {
        return (this.state.needToSort) ? Helper.sort(this.props.counties, this.state.ASC) : this.props.counties;
    },
    clear() {
        document.getElementById('territorial-search').value = '';
        this.props.clearQuery();
    },
    render() {
        var display = {display: 'none'};
        if (this.props.countiesSelect != undefined) display={};
        var rotation = (this.state.ASC) ? " Z-A" : "A-Z";
        return (
            <div className="container">
                <div className="row grayed">
                    <div className="col-md-8 units-list-area">
                        <div className="list-group-item active location5" style={{display: "inline-block", verticalAlign: "middle", width: "100%"}}>
                            <span>Apylinkių rezultatai</span>
                            <span
                                id="sort-districts-button"
                                className="btn btn-sm btn-success no-background"
                                style={{ color: '#FFFFFF', borderColor: '#FFFFFF' }}
                                onClick={this.toggleSortOrder}>
                                Rušiuoti {rotation}
                            </span>
                        </div>
                        <div className="list-group-item" style={{ height: 'auto' }}>
                            {this.sortCounties()}
                        </div>
                    </div>
                    <div className="col-md-4 units-create-area">
                        <div className="col-md-11">
                            <div className="list-group-item active location5">
                                Susiaurinti rezultatus
                            </div>
                            <div className="list-group-item">
                                <p>Pasirinkite apygardą</p>
                                <p id="sort-select-district">{this.props.districtsSelect}</p>
                                <div className="counties" style={ display }>
                                    <p>Pasirinkite apylinkę</p>
                                    <p id="sort-select-county">{this.props.countiesSelect}</p>
                                </div>
                            </div>
                            <div className="list-group-item">
                                <div className="row narrowed" style={{ backgroundColor: 'white' }}>
                                    <div className="col-md-12" style={{ padding: 0 }}>
                                        <p>Ieškoti apylinkės/apygardos</p>
                                        <form>
                                            <div className="input-group">
                                                <input
                                                    className="form-control"
                                                    id="territorial-search"
                                                    onChange={this.props.onKeyUp}
                                                />
                                                <span className="input-group-addon" style={{ padding: 0 }} onClick={this.clear}>
                                                   <button className="btn btn-secondary" type="button">
                                                       <span className="glyphicon glyphicon-remove-circle"></span>
                                                   </button>
                                               </span>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});

module.exports = AdminResultsViewComponent;
