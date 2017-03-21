var React = require('react');

var SearchBarComponent = React.createClass({
    clear() {
        document.getElementById('system-search').value = '';
        this.props.clearQuery();
    },
    render: function() {
        return (
          <div className="row narrowed" style={{ margin: '10px 0px' }}>
              <div className="col-md-3">
                   <form>
                       <div className="input-group">
                           <input
                              className="form-control"
                              id="system-search"
                              placeholder="Ieškoti kandidato"
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
        );
    }
});

module.exports = SearchBarComponent;
