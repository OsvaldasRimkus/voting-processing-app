/**
 * Created by osvaldas on 17.2.22.
 */
var React = require('react');
var Modal = require('react-modal');

var styles = {
    "image": {width: 20, height: 20},
    "closeButton": {backgroundColor: "#f44336",
                    fontSize: "14px",
                    padding: "10px 14px",
                    borderRadius: "8px",
                    color: "#ffffff",
                    fontWeight: "bold",
                    margin: "auto"},
    "candidateName": {  fontSize: "14px",
                        color: "#3543ff",
                        fontWeight: "bold"},
    };

const customStyles = {
    content : {
        top                   : '50%',
        left                  : '50%',
        right                 : 'auto',
        bottom                : 'auto',
        marginRight           : '-50%',
        transform             : 'translate(-50%, -50%)',
        maxWidth              : '40%',
    }
};

var CandidateDetails = React.createClass({

    getInitialState: function() {
        return { modalIsOpen: false,
            partyCandidate: {display: "block"},
            independentCandidate: {display: "none"},
        };
    },

    openModal: function() {
        this.setState({modalIsOpen: true});
        if(this.props.partyName == "Išsikėlęs pats"){
            this.setState({partyCandidate: {display: "none"}});
            this.setState({independentCandidate: {display: "block"}})
        }
    },

    afterOpenModal: function() {
        // references are now sync'd and can be accessed.
        this.refs.subtitle.style.color = '#1c9312';
        this.refs.subtitle.style.fontSize = '18px';
        this.refs.subtitle.style.fontWeight = "bold";
    },

    closeModal: function() {
        this.setState({modalIsOpen: false});
    },

    render: function() {
        return (
            <div>
                <button id={"details-button-" + this.props.personId} type="button" className="btn btn-default btn-sm" style={{ marginBottom: 10 }} onClick={this.openModal}>Išamiau</button>
                <Modal
                    isOpen={this.state.modalIsOpen}
                    onAfterOpen={this.afterOpenModal}
                    onRequestClose={this.closeModal}
                    style={customStyles}
                    contentLabel="Example Modal"
                >
                    <p style={{ textAlign: 'center' }}>
                        <span id={"candidate-name-" + this.props.personId} ref="subtitle">{this.props.firstName}&nbsp;{this.props.lastName}</span>
                    </p>
                    <p id={"party-dependency-" + this.props.personId} style={ this.state.partyCandidate }>
                        <img src="app/imgs/political_party.png" style={ styles.image } alt="party-icon"/>&nbsp;
                        <b>Politinė partija:</b>&nbsp;&nbsp;
                        {this.props.partyName}
                    </p>
                    <p id={"party-dependency-" + this.props.personId} style={ this.state.independentCandidate }>
                        <img src="app/imgs/political_party.png" style={ styles.image } alt="number-icon"/>&nbsp;
                        <b>Išsikėlęs pats</b>
                    </p>
                    <p id={"party-list-number-" + this.props.personId} style={ this.state.partyCandidate }>
                        <img src="app/imgs/hash.png" style={ styles.image } alt="number-icon"/>&nbsp;
                        <b>Numeris partijos sąraše:</b>&nbsp;&nbsp;
                        {this.props.positionInPartyList}
                    </p>
                    <p id={"candidate-id-" + this.props.personId}>
                        <img src="app/imgs/fingerprint.png" style={ styles.image } alt="personal-id-icon"/>&nbsp;
                        <b>Asmens kodas:</b>&nbsp;&nbsp;
                        {this.props.personId}
                    </p>
                    <p id={"birth-date-" + this.props.personId} >
                        <img src="app/imgs/cupcake.png" style={ styles.image } alt="birthdate-icon"/>&nbsp;
                        <b>Gimimo data:</b>&nbsp;&nbsp;
                        {this.props.birthDate}
                    </p>
                    <p>
                        <img src="app/imgs/books.png" style={ styles.image } alt="about-candidate-icon"/>&nbsp;
                        <b>Apie kandidatą:</b>
                    </p>
                    <p id={"candidate-information-" + this.props.personId} style={{textAlign: "justify", marginLeft: 25, marginBottom: 20}}>{this.props.about}</p>
                    <div style={{display: "flex", justifyContent: "center"}}>
                        <button id="close-details" onClick={this.closeModal} style={ styles.closeButton }>Išjungti</button>
                    </div>
                </Modal>
            </div>
        );
    }
});





module.exports = CandidateDetails;
