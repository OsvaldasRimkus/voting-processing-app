/**
 * Created by osvaldas on 17.2.21.
 */

var React = require ('react');

var ValidationsOR = {

    nameValidation: function (name) {
        var currentErrors = [];
        var isFirstCharacterGood = Variables.firstNameCharacterRegex.test(name.charAt(0));
        var areOtherCharactersGood = Variables.otherNameCharactersRegex.test(name);
        if(!isFirstCharacterGood || !areOtherCharactersGood){
            currentErrors.push(Helpers.errorWrapperWarning(ErrorMessages.wrongNameCharacters, 1));
        }
        var checkMaxLength = name.length <= Variables.maxNameLength;
        if (!checkMaxLength){
            currentErrors.push(Helpers.errorWrapperWarning(ErrorMessages.nameTooLongBeginning + Variables.maxNameLength + ErrorMessages.nameTooLongEnding + name.length, 2));
        }
        var checkMinLength = name.length >= Variables.minNameLength;
        if (!checkMinLength){
            currentErrors.push(Helpers.errorWrapperWarning(ErrorMessages.nameTooShort, 3));
        }
        return currentErrors;
    },

    emailValidation: function (email, allRepresentativesEmails) {
        var currentErrors = [];
        var checkIfOnlyCharacters = Variables.emailRegex.test(email);
        if(!checkIfOnlyCharacters){
            currentErrors.push(Helpers.errorWrapperInformation(ErrorMessages.emailFormat, 1));
        }
        var doesEmailAlreadyExists = false;
        doesEmailAlreadyExists = allRepresentativesEmails.includes(email.toLowerCase());
        if(doesEmailAlreadyExists){
            currentErrors.push(Helpers.errorWrapperWarning(ErrorMessages.emailAlreadyExists, 2));
        }
        return currentErrors;
    },
};

var Helpers = {

    errorWrapperWarning: function (message, id) {
        return (
            <div key={id} className="alert alert-danger error">
                <small>{message}</small>
            </div>
        )
    },

    errorWrapperInformation: function (message, id) {
        return (
            <div key={id} className="alert alert-info error">
                <small>{message}</small>
            </div>
        )
    },
};

var Variables = {
    firstNameCharacterRegex: /^[AaĄąBbCcČčDdEeĘęĖėFfGgHhIiĮįYyJjKkLlMmNnOoPpRrSsŠšTtUuŲųŪūVvZzŽž\s]*$/,
    otherNameCharactersRegex: /^([AaĄąBbCcČčDdEeĘęĖėFfGgHhIiĮįYyJjKkLlMmNnOoPpRrSsŠšTtUuŲųŪūVvZzŽž\s\-]*)$/,
    maxNameLength: 15,
    minNameLength: 3,
    emailRegex: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
};

var ErrorMessages = {
    wrongNameCharacters: "Įvedėte netinkamus simbolius: įveskite tik lietuviškas raides",
    nameTooLongBeginning: "Per daug simbolių: įveskite ne daugiau ",
    nameTooLongEnding: " simbolių. Dabar įvesta: ",
    nameTooShort: "Įveskite bent 3 simbolius",
    emailFormat: "El. pašto adreso pavyzdys: vartotojas@e-mail.lt",
    emailAlreadyExists: "Toks el. pašto adresas jau yra sistemoje.",
};

module.exports = ValidationsOR;