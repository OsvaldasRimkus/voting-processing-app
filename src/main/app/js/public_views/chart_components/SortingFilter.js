var React = require('react');

var SortingFilter = function(props) {
    return(
        <div className="sorting-filter">
            <form>
                <label> Rikiuoti pagal: &nbsp;</label>
                <label className="radio-inline">
                    <input checked={props.orderBy==='value'} onChange={props.onChange} value="value" type="radio" name="optradio"/>balsus
                </label>
                <label className="radio-inline">
                    <input checked={props.orderBy==='key'} onChange={props.onChange} value="key" type="radio" name="optradio"/>vardą
                </label>
            </form>
        </div>
    );
}

SortingFilter.propTypes = {
    orderBy: React.PropTypes.string,
    onChange: React.PropTypes.func.isRequired
}

module.exports = SortingFilter;