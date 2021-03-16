let kladrId;

let surname_div;
let name_div;
let passport_number_div;
let date_of_birthday_div;
let address_div;
let middle_name_div;

let surname_input;
let name_input;
let passport_number_input;
let date_of_birthday_input;
let address_input;
let middle_name_input;

function add() {
    let personalDataDto;
    personalDataDto = JSON.stringify(getUserData());
    if (checkInputs()) sendRegistrationRequest(personalDataDto, getCreatingStatus);
}

function initPersonalDataPage() {
    bindHtmlElements();
}

function sendRegistrationRequest(personalDataDto, callback) {
    let request = new XMLHttpRequest();
    request.open("POST", api.PERSONAL_DATA_POST_REQUEST, true);
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", sessionStorage.getItem(keys.AUTHORIZATION_TOKEN))
    console.log(personalDataDto);
    request.send(personalDataDto);
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            let response = JSON.parse(request.responseText);
            callback(response);
        }
    }
}


function getCreatingStatus(data) {
    console.log(data)

    if (data.status === 0) {
        alert("Данные успешно добавлены.")
        changePage(api.MAIN_PAGE, sessionStorage.getItem(keys.AUTHORIZATION_TOKEN))
    } else if (data.status === 1) {

    } else if (data.status === 2) {
        handleErrors(data.errors)
    } else (alert("Проблемы на стороне сервера"))
}

function handleErrors(errorsList) {
    for (let i = 0; i < errorsList.length; i++) {
        console.log(errorsList[i] + " " + errors.USER_AGE_IS_UNDER_14 + " " + errors.text.LOL);
        switch (errorsList[i]) {
            case errors.PERSONAL_DATA_ALREADY_EXIST:
                showErrorText(errors.text.PASSPORT_ALREADY_EXIST)
                changePage(api.MAIN_PAGE, sessionStorage.getItem(keys.AUTHORIZATION_TOKEN))
                break;
            case errors.PASSPORT_ALREADY_EXIST:
                setError(passport_number_div)
                showErrorText(errors.text.PASSPORT_ALREADY_EXIST)
                break;
            case errors.USER_NAME_IS_TOO_SHORT:
                setError(name_div);
                showErrorText(errors.text.USER_NAME_IS_TOO_SHORT)
                break;
            case errors.USER_NAME_IS_TOO_LONG:
                setError(name_div);
                showErrorText(errors.text.USER_NAME_IS_TOO_LONG);
                break;
            case errors.USER_NAME_IS_INVALID:
                setError(name_div)
                showErrorText(errors.text.USER_NAME_IS_INVALID);
                break;
            case errors.USER_SURNAME_IS_TOO_SHORT:
                setError(surname_div)
                showErrorText(errors.text.USER_SURNAME_IS_TOO_SHORT)
                break;
            case errors.USER_SURNAME_IS_TOO_LONG:
                setError(surname_div)
                showErrorText(errors.text.USER_SURNAME_IS_TOO_SHORT)
                break;
            case errors.USER_SURNAME_IS_INVALID:
                setError(surname_div)
                showErrorText(errors.text.USER_SURNAME_IS_INVALID)
                break;
            case errors.PASSPORT_NUMBER_LENGTH:
                setError(passport_number_div)
                showErrorText(errors.text.PASSPORT_NUMBER_LENGTH)
                break;
            case errors.PASSPORT_CONTAINS_WRONG_SYMBOLS:
                setError(passport_number_div)
                showErrorText(errors.text.PASSPORT_CONTAINS_WRONG_SYMBOLS)
                break;
            case errors.USER_AGE_IS_UNDER_14:
                setError(date_of_birthday_div)
                showErrorText(errors.text.USER_AGE_IS_UNDER_14)
                break;
            default:
                showErrorText("ПОЛУЧЕНА НЕИЗВЕСТНАЯ ОШИБКА.")
        }
    }
}

function showErrorText(text) {
    alert(text);
}


function getUserData() {
    let name = name_input.value.trim();
    console.log(name)
    let surname = surname_input.value.trim();
    let middleName = middle_name_input.value.trim();
    let passportNumber = passport_number_input.value;
    let dateOfBirthday = date_of_birthday_input.value;
    let address = document.getElementById("address_input").value;
    // !!MOCK!! пока сервер не работает
    // kladrId = Math.random()*899000 + 100000
    console.log(kladrId)
    return {
        'name': name,
        'surname': surname,
        'middleName': middleName,
        'passportNumber': passportNumber,
        'dateOfBirthday': dateOfBirthday,
        'kladrAddress': kladrId,
        'address': address
    }
}

function checkAddress(obj) {
    if (obj != null) {
        if (obj.hasOwnProperty("type")) {
            console.log(obj.type)
            if (obj.type != "дом") {
                alert("Укажите польный адрес вашей прописки.")
                document.getElementById("address_div").className = "error"
                kladrId = null;
            } else {
                kladrId = obj.id;
                document.getElementById("address_div").className = "success"
            }
        }
    }
}

function checkInputs() {
    let result = true;
    if (!checkInput(name_input, name_div)) result = false;
    if (!checkInput(surname_input, surname_div)) result = false;
    if (!checkInput(passport_number_input, passport_number_div)) result = false;
    if (!checkInput(date_of_birthday_input, date_of_birthday_div)) result = false;
    if (!checkKladrId(kladrId)) result = false;
    return result;
}

function checkInput(input, div) {
    if (input != null) {
        if (input.value != "") {
            setSuccess(div)
            return true;
        } else {
            setError(div)
            console.log("Ошибка в блоке " + div.id)
            return false;
        }
    } else console.log("Ошибка внутренней логики, html эллемент \"" + div.id + "\" не обнаружен")
}

function checkKladrId(kladrId) {
    if (kladrId == null) {
        document.getElementById("address_div").className = "error"
        console.log("Кладр id не получен! Невозможно отправить запрос на регистрацию.")
        return false
    } else {
        document.getElementById("address_div").className = "success"
        return true;
    }
}

$(function () {
    var $address = $('[name="address"]')

    $address.fias({
        oneString: true,
        change: function (obj) {
            $('.js-log li').hide();
            check(obj);
        }
    });


    function check(obj) {
        checkAddress(obj);
    }

    function log(obj) {
        var $log, i;

        $('.js-log li').hide();

        for (i in obj) {
            $log = $('#' + i);

            if ($log.length) {
                $log.find('.value').text(obj[i]);
                console.log($log.find('.value').text(obj[1]))
                $log.show();
            }
        }
    }
});

function setError(element) {
    element.className = "error";
}

function setSuccess(element) {
    element.className = "success";
}

function fillSuccessFields() {
    setSuccess(surname_div)
    setSuccess(name_div)
    setSuccess(passport_number_div)
    setSuccess(date_of_birthday_div)
    setSuccess(address_div)
}

function bindHtmlElements() {
    surname_div = document.getElementById("surname_div");
    name_div = document.getElementById("name_div");
    passport_number_div = document.getElementById("passport_number_div");
    date_of_birthday_div = document.getElementById("date_of_birthday_div");
    address_div = document.getElementById("address_div");
    middle_name_div = document.getElementById("middle_name_div");

    surname_input = document.getElementById('surname_input');
    name_input = document.getElementById('name_input');
    passport_number_input = document.getElementById('passport_number_input');
    date_of_birthday_input = document.getElementById('date_of_birthday_input');
    address_input = document.getElementById('address_input');
    middle_name_input = document.getElementById('middle_name_input');
}