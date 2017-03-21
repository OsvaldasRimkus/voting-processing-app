var React = require('react');
var truncate = require('truncate');
var CandidateDetails = require('../components/tiny_components/CandidateDetails');
var styles = {
    "image": {width: 20, height: 20},
    "margLeft": {marginLeft: 25},
    "strong": {fontWeight: 'bold'}
};
var partyName;

function CandidateCardComponent(props) {
  partyName = (props.candidate.partyName == undefined) ? "Išsikėlęs pats" : props.candidate.partyName;
  return (
        <div className="well well-sm candidate-card">
                <div className="row candidate-card-low">
                    <div className="col-sm-8 col-md-12">
                        <h4 id={"candidate-name-main-" + props.candidate.personId} style={ styles.marginLeft, styles.strong }>{props.candidate.firstName}&nbsp;{props.candidate.lastName}</h4><br />
                        <p id={"party-name-main-" + props.candidate.personId}><cite><img src="app/imgs/political_party.png" style={ styles.image }/>&nbsp; {partyName}</cite></p>
                        <p id={"candidate-id-main-" + props.candidate.personId}>
                            <img src="app/imgs/fingerprint.png" style={ styles.image }/>&nbsp; {props.candidate.personId}
                        </p>
                        <p id={"candidate-birthdate-main-" + props.candidate.personId}>
                            <img src="app/imgs/cupcake.png" style={ styles.image }/>&nbsp; {props.candidate.birthDate}
                        </p>
                        <p id={"candidate-description-main-" + props.candidate.personId}>
                            <img src="app/imgs/books.png" style={ styles.image }/>&nbsp; {truncate(props.candidate.description, 50)}
                        </p>
                        <CandidateDetails
                            firstName={props.candidate.firstName}
                            lastName={props.candidate.lastName}
                            partyName={partyName}
                            positionInPartyList={props.candidate.positionInPartyList}
                            personId={props.candidate.personId}
                            birthDate={props.candidate.birthDate}
                            about={props.candidate.description}
                        />
                    </div>
                </div>
            </div>
  );
}

module.exports = CandidateCardComponent;
