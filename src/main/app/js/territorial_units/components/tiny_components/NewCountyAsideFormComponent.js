var React = require('react');
var Geosuggest = require('react-geosuggest').default;

function NewCountyAsideForm(props) {
    var hideOtherSuggestions = function() {
        $('.geosuggest__suggests').hide();
    };
    return (
        <form className="aside-county-form">
            <div className="form-group county-aside">
                <label htmlFor="inputCounty">Apylinkės pavadinimas</label>
                <input type="text" id="input-county-name" className="form-control" value={props.name} onChange={props.changeName}/>
            </div>
            <div className="form-group county-aside">
                <label>Gyventojų skaičius</label>
                <input type="number" id="input-county-voters" className="form-control" min={1} value={props.count} onChange={props.changeCount}/>
            </div>
            <div className="form-group county-aside">
                <label>Adresas</label>
                <Geosuggest
                    id="input-county-address"
                    ref={el=>this._geoSuggest=el}
                    placeholder=""
                    inputClassName="form-control"
                    country="LTU"
                    queryDelay={500}
                    onSuggestSelect={props.setSuggest}
                    onActiveSuggest={hideOtherSuggestions}
                    onChange={props.changeAddress}
                />
            </div>
            <div className="county-form-actions">
                <button type="submit" id="create-county-button" onClick={props.add} className="btn btn-default btn-xs ">Pridėti</button>
                <button type="submit" id="cancel-county-creation-button" onClick={props.cancel} className="btn btn-default btn-xs ">Atšaukti</button>
            </div>
        </form>
    )
}

module.exports = NewCountyAsideForm;
