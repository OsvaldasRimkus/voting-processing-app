/**
 * Created by osvaldas on 17.2.25.
 */
var React = require('react');
var Modal = require('react-modal');
var PartyDisplayContainer = require ('../../political_units/containers/PartyDisplayContainer');

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

var PleaseWaitModal = React.createClass ({

    getInitialState: function() {
        return { modalIsOpen: false,
                 partyCandidate: {display: "block"},
                 independentCandidate: {display: "none"}
        };
    },

    openModal: function() {
        this.setState({modalIsOpen: true});
        if(this.props.unit.name == "Išsikėlęs pats"){
            this.setState({partyCandidate: {display: "none"}, independentCandidate: {display: "block"}});
        }
        setTimeout(function(){
            this.closeModal();
        }, 3000);
    },

    afterOpenModal: function() {
        // references are now sync'd and can be accessed.
        this.refs.subtitle.style.color = '#1c9312';
        this.refs.subtitle.style.fontSize = '18px';
        this.refs.subtitle.style.fontWeight = "bold";
        this.refs.subtitle.style.textAlign = "center";
    },

    closeModal: function() {
        this.setState({modalIsOpen: false});
    },

    render: function () {
        return (
            <div>
                <PartyDisplayContainer
                    raktas={this.props.raktas}
                    index={this.props.index}
                    unit={this.props.unit}
                    delete={this.props.delete}
                    deleteCandidates={this.props.deleteCandidates}
                    updateParties={this.props.updateParties}
                    openModal={this.openModal}
                    closeModal={this.closeModal}
                />
                <Modal
                    isOpen={this.state.modalIsOpen}
                    onAfterOpen={this.afterOpenModal}
                    onRequestClose={this.closeModal}
                    style={customStyles}
                    contentLabel="Please-Wait"
                >
                <div>
                    <div ref="subtitle">Prašome palaukti. Sunkiai dirbame...</div>

                </div>
                </Modal>
            </div>

        )
    }
});

module.exports = PleaseWaitModal;