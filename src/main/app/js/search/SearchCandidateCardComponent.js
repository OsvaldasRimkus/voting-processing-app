var React = require('react');
var truncate = require('truncate');
var text = "Candidato aprasymas turi buti pakankamai ilgas kad galetu butis sutrumpiontas";
var styles = {"image": {width: 20, height: 20}, "marginLeft": {marginLeft: 25}}
var partyName;

function SearchCandidateCardComponent(props) {
  partyName = (props.candidate.party == null) ? "Išsikėlęs pats" : props.candidate.party.name;
  return (
        <div className="well well-sm candidate-card-wide">
                <div className="row candidate-card-low">
                    <div className="col-sm-8 col-md-12" style={{backgroundColor: "white"}}>
                        <p style={{ marginTop: 15 }}>
                            <p style={ styles.marginLeft, {fontSize: 22} }>
                                <span id={"candidate-info-tab-name-" + props.candidate.personId} >
                                    <span style={{color:'#1c9312', fontWeight:"bold"}}>
                                        {props.candidate.firstName} &nbsp; {props.candidate.lastName}
                                    </span>
                                </span>
                                <span
                                    className="glyphicon glyphicon-remove-circle"
                                    aria-hidden="true"
                                    style={{ float: 'right', cursor: 'pointer' }}
                                    onClick={props.hideDetails}>
                                </span>
                            </p>
                        </p>
                        <p>
                            <small id={"candidate-info-tab-party-dependency-" + props.candidate.personId} ><img src="app/imgs/political_party.png" style={ styles.image }/>&nbsp;&nbsp;<b>Partija:</b>&nbsp; {partyName}</small>
                        </p>
                        <p>
                            <small id={"candidate-info-tab-birth-day-" + props.candidate.personId} ><img src="app/imgs/cupcake.png" style={ styles.image }/>&nbsp;&nbsp;<b>Gimimo data:</b>&nbsp; {props.candidate.birthDate}</small>
                        </p>
                        <p>
                            <small id={"candidate-info-tab-information-" + props.candidate.personId} >
                                <img src="app/imgs/books.png" style={ styles.image }/>&nbsp;&nbsp;
                                <b>Apie:</b>&nbsp;<br />
                                <span style={ styles.marginLeft }>{props.candidate.description}</span>
                            </small>
                        </p>
                    </div>
                </div>
            </div>
  );
}

module.exports = SearchCandidateCardComponent;
