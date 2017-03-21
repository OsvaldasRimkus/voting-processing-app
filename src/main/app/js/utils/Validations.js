var React = require('react');
var ErrorWrapper = require('../components/tiny_components/ErrorWrapper');
var RootErrorWrapper = require('../components/tiny_components/RootErrorWrapper');

var Vars = {
    districtNameRegex: new RegExp(/^([a-zA-ZąčęėįšųūžĄČĘĖĮŠŲŪŽ\s\-][^qQwWxX0-9]*)$/),
    countyNameRegex: new RegExp(/^([a-zA-ZąčęėįšųūžĄČĘĖĮŠŲŪŽ0-9\s\-][^qQwWxX]*)$/),
    partyNameRegex: new RegExp(/^([a-zA-ZąčęėįšųūžĄČĘĖĮŠŲŪŽ\s\-][^qQwWxX0-9]*)$/),
    min: 3,
    max: 70
};

var Validations = {
    checkErrorsPartyAsideForm: function(name, file) {
        var errors = [];

        if (name.length < Vars.min || name.length > Vars.max) errors.push("Partijos pavadinimas " + Errors.length);
        if (!Vars.partyNameRegex.test(name)) errors.push(Errors.onlyAlphas);

        if (file == undefined) {
            errors.push(Errors.noFileError);
        } else {
            var extension = file.name.split(".").pop().toLowerCase();
            if (extension.localeCompare("csv") != 0) errors.push(Errors.csvOnly);
        }

        return errors;
    },
    checkErrorsUnitEditForm: function(name) {
        var errors = [];

        if (name.length < Vars.min || name.length > Vars.max) errors.push("Pavadinimas " + Errors.length);
        if (!Vars.partyNameRegex.test(name)) errors.push(Errors.onlyAlphas);

        return errors;
    },
    checkErrorsDistrictAsideForm: function(name) {
        var errors = [];

        if (name.length < Vars.min || name.length > Vars.max) errors.push("Apygardos pavadinimas " + Errors.length);
        if (!Vars.districtNameRegex.test(name)) errors.push(Errors.onlyAlphas);

        return errors;
    },
    checkErrorsCountyForm: function(name, count, address) {
        var errors = [];

        if (name.length < Vars.min || name.length > Vars.max) errors.push("Apylinkės pavadinimas " + Errors.length);
        if (!Vars.countyNameRegex.test(name)) errors.push(Errors.onlyAlphas);

        switch (true) {
            case (count == undefined):
              errors.push(Errors.popToLow + 0);
              break;
            case (count < 100):
              errors.push(Errors.popToLow + count);
              break;
            case (count > 100000):
              errors.push(Errors.popToHigh + count);
              break;
        }

        if (address.length == 0) {
            errors.push("Adresas - " + Errors.blankField);
        } else {
            if (address.length < Vars.min) errors.push("Adresas " + Errors.length);
            if (address.length > Vars.max + 30) errors.push("Adresas " + Errors.length);
        }

        return errors;
    },
    /*checkErrorsPartyMMform: function(dictionary, voters) {
        var errors = [];
        var emptyFields = 0;

        dictionary.forEach(function(value) {
            if (value === "") {
                emptyFields += 1;
            } else if (isNaN(value)) {
                errors.push(value + " " + Errors.NaNerror);
            } else if (parseInt(value) < 0) {
                errors.push(value + " " + Errors.negativeNumError);
            } else if (parseInt(value) > voters) {
                errors.push(value + " " + Errors.positiveInfiniteNumError + " " + voters);
            }
        });
        if (emptyFields > 0) errors.push(Errors.emptyFieldsError + "(" + emptyFields + ")");

        if (emptyFields == dictionary.size + 1) {
            var emptyForm = new Array();
            emptyForm.push(Errors.emptyFormError);
            errors = emptyForm;
        }

        return errors;
    },*/
    checkErrorsResultForm: function(dictionary, spoiled, voters) {
        var errors = [];
        var emptyFields = 0;

        if (spoiled === "" || spoiled == undefined) {
            emptyFields += 1;
        } else if (isNaN(spoiled)) {
            errors.push(spoiled + " " + Errors.NaNerror);
        }
        if (parseInt(spoiled) < 0) {
            errors.push(spoiled + " " + Errors.negativeNumError);
        } else if (parseInt(spoiled) > voters) {
            errors.push(value + " " + Errors.positiveInfiniteNumError + " " + voters);
        }

        var countyVoters = voters;
        var total = 0;

        dictionary.forEach(function(value, key) {
            if (value == "" || value == undefined) {
                emptyFields += 1;
            } else if (isNaN(value)) {
                errors.push(value + " " + Errors.NaNerror);
            } else if (parseInt(value) < 0) {
                errors.push(value + " " + Errors.negativeNumError);
            } else if (parseInt(value) > countyVoters) {
                errors.push(value + " " + Errors.positiveInfiniteNumError + " " + countyVoters);
            } else {
                total += parseInt(value);
            }
        });
        if (total > (voters-spoiled) || total > voters) errors.push(Errors.votersOverflowError);

        if (emptyFields > 0) errors.push(Errors.emptyFieldsError + "(" + emptyFields + ")");
        if (emptyFields == dictionary.size + 1) {
            var emptyForm = new Array();
            emptyForm.push(Errors.emptyFormError);
            errors = emptyForm;
        }

        return errors;
    },
    validateCsv: function(file) {
      var errors = [];

      if (file == undefined) {
          errors.push(Errors.noFileError);
      } else {
          var extension = file.name.split(".").pop().toLowerCase();
          if (extension.localeCompare("csv") != 0) errors.push(Errors.csvOnly);
      }

      return errors;
    },
    prepareJSerrors: function(errors, root_message, style) {
        var preparedErrors = [];
        for (var i = 0; i < errors.length; i++) {
            preparedErrors.push(<ErrorWrapper message={errors[i]} key={i}/>);
        }
        return (
            <RootErrorWrapper message={root_message} key={errors.length} inlineStyle={style}>
                {preparedErrors}
            </RootErrorWrapper>
        );
    },
    prepareSpringErrors: function(errors, style) {
        var preparedErrors = [];
        for (var i = 1; i < errors.length; i++) {
            preparedErrors.push(<ErrorWrapper message={errors[i]} key={i}/>);
        }
        return (
            <RootErrorWrapper message={errors[0]} inlineStyle={style}>
                {preparedErrors}
            </RootErrorWrapper>
        );
    },
};

var Errors = {
    length: "nuo " + Vars.min + " iki " + Vars.max + " simbolių",
    blankField: "tuščias laukas",
    onlyAlphas: "Netinkamas pavadinimo formatas",
    popToLow: "Per mažai gyventojų - ",
    popToHigh: "Per daug gyventojų - ",
    noFileError: "Būtina įkelti narių sarašą",
    csvOnly: "Failas turi būti .csv",
    NaNerror: "yra ne skaičius",
    negativeNumError: "yra mažiau už 0",
    emptyValueError: "Balsų įvedimo laukas liko tuščias",
    emptyFieldsError: "Formoje liko tuščių laukų ",
    positiveInfiniteNumError: "netinkamas skaičius. Apylinkės gyventojų tik",
    emptyFormError: "Forma tuščia",
    votersOverflowError: "Suminis biuletenių skaičius viršija apylinkės balsuotojų skaičių"
};

module.exports = Validations;
