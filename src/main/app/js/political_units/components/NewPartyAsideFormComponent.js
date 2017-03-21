var React = require('react');

var NewPartyAsideFormComponent = React.createClass({
    componentWillReceiveProps: function(newProps) {
        // denotes that axios POST returned 201
        if (newProps.popupAlert) {
            var element = document.getElementById("party-success-alert");
            element.classList.remove('hide');
            setTimeout(function() { element.classList.add('hide') }, 3000);
        }
    },
    render() {
        return (
            <div>
                <form>
                    <div className="form-group">
                        <label htmlFor="inputCounty">Partijos pavadinimas</label>
                        <input type="text" className="form-control" id="input-county-name" onChange={this.props.changeName} value={this.props.name}/>
                    </div>
                    <div className="form-group">
                        <label htmlFor="exampleInputFile">Įkelti partijos sąrašą</label>
                        <input ref="fileCSV" type="file" id="input-file-attach"/>
                    </div>
                    <div className="form-group">
                        <button id="create-party-button" type="submit" onClick={this.props.create.bind(null, this.refs)} className="btn btn-primary btn-md">Sukurti</button>
                    </div>
                </form>
                <div className="form-group errors-area">
                    {this.props.jsErrors}
                    {this.props.springErrors}
                </div>
                <div className="alert alert-success hide" id="party-success-alert">
                    <span>Partija sukurta!</span>
                </div>
            </div>
        )
    }
});

module.exports = NewPartyAsideFormComponent;
