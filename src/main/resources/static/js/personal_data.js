let kladrId;
let input_postfix = "_input";
let div_postfix = "_div"

function create() {
    let userDTO;

    userDTO = JSON.stringify(getUserData());

    if (checkInputs()) sendRegistrationRequest(userDTO, getCreatingStatus());
}

function sendRegistrationRequest(userDTO, callback) {
    let request = new XMLHttpRequest();
    request.open("POST", api.CREATE_POST, true);
    request.setRequestHeader("Content-Type", "application/json");
    console.log(userDTO);
    request.send(userDTO);
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            let response = JSON.parse(request.responseText);
            callback(response);
        }
    }
}


function getCreatingStatus(data) {
    alert(data);
}

function getUserData() {
    let name = document.getElementById("name_input").value;
    let surname = document.getElementById("surname_input").value;
    let middleName = document.getElementById("middle_name_input").value;
    let passportNumber = document.getElementById("passport_number_input").value;
    let dateOfBirthday = document.getElementById("date_of_birthday_input").value;
    let address = kladrId;

    return {
        'name': name,
        'surname': surname,
        'middleName': middleName,
        'passportNumber': passportNumber,
        'dateOfBirthday': dateOfBirthday,
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
    if (!checkInput("name")) result = false;
    if (!checkInput("surname")) result = false;
    if (!checkInput("middle_name")) result = false;
    if (!checkInput("passport_number")) result = false;
    if (!checkInput("date_of_birthday")) result = false;
    if(!checkKladrId(kladrId)) result = false;
    return result;
}

function checkInput(id) {
    let input = document.getElementById(id + input_postfix);
    if (input != null) {
        if (input.value != "") {
            document.getElementById(id + div_postfix).className = "success"
            return true;
        } else {
            console.log("Ошибка в блоке " + id + div_postfix)
            document.getElementById(id+div_postfix).className = "error"
            return false;
        }
    } else console.log("Ошибка внутренней логики, html эллемент \"" + id + input_postfix + "\" не обнаружен")
}

function checkKladrId(kladrId){
    if(kladrId==null){
        document.getElementById("address_div").className = "error"
        console.log("Кладр id не получен! Невозможно отправить запрос на регистрацию.")
        return false
    }
    else {
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
            print(obj);
        }
    });


    function print(obj) {
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