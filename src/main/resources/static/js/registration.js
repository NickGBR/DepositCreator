let input_postfix = "_input";
let div_postfix = "_div"

function create() {
    let registrationDTO;
    registrationDTO = JSON.stringify(getAuthenticationData());
    if (checkInputs()) sendRegistrationRequest(registrationDTO, getCreatingStatus);
}

function sendRegistrationRequest(registrationDTO, callback) {
    let request = new XMLHttpRequest();
    request.open("POST", api.REGISTRATION_POST_REQUEST, true);
    request.setRequestHeader("Content-Type", "application/json");
    console.log(registrationDTO);
    request.send(registrationDTO);
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            let response = JSON.parse(request.responseText);
            callback(response);
        }
    }
}


function getCreatingStatus(data) {
    if(data == true){
        alert("Регистрация прошла успешно")
        callLoginPage();
    }
    else {
        document.getElementById("login_div").className = "error"
        alert("Пользователь с таким логином уже существует")
    }
}

function getAuthenticationData() {
    let login = document.getElementById("login_input").value;
    let password = document.getElementById("password_input").value;
    return {
        'login': login,
        'password': password,
    }
}


function checkInputs() {
    let result = true;
    if (!checkInput("login")) result = false;
    if (!checkPassword()) result = false;
    return result;
}

function checkInput(id) {
    let input = document.getElementById(id
        + input_postfix);
    if (input != null) {
        if (input.value != "") {
            document.getElementById(id + div_postfix).className = "success"
            return true;
        } else {
            console.log("Ошибка в блоке " + id + div_postfix)
            document.getElementById(id + div_postfix).className = "error"
            return false;
        }
    } else console.log("Ошибка внутренней логики, html эллемент \"" + id + input_postfix + "\" не обнаружен")
}

function checkPassword() {
    let password = document.getElementById("password_input").value;
    let passwordCheck = document.getElementById("password_check_input").value;
    if (!(password == passwordCheck)) {
        document.getElementById("password_check_div").className = "error";
        document.getElementById("password_div").className = "error";
        alert("Пароли должны совпадать!");
        return false;
    } else if (password == "" || passwordCheck == "") {
        alert("Пароль не должен быть пустым.");
        document.getElementById("password_check_div").className = "error";
        document.getElementById("password_div").className = "error";
        return false;
    }
    document.getElementById("password_check_div").className = "success";
    document.getElementById("password_div").className = "success";
    return true;
}

function callLoginPage(){
    window.location.href = api.LOGIN_PAGE;
}





