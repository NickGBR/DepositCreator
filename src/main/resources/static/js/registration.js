let input_postfix = "_input";
let div_postfix = "_div"

let passwordDiv;
let passwordCheckDiv;
let loginDiv;
let passwordInput;
let passwordCheckInput;
let loginInput;

function create() {
    let registrationDTO;
    registrationDTO = JSON.stringify(getAuthenticationData());
    if (checkInputs()) sendRegistrationRequest(registrationDTO, getCreatingStatus);
}

function initRegistrationPage() {
    bindHtmlElements();
}

function sendRegistrationRequest(registrationDTO, callback) {
    let request = new XMLHttpRequest();
    request.open("POST", api.REGISTRATION_POST_REQUEST, true);
    request.setRequestHeader("Content-Type", "application/json");
    console.log(registrationDTO);
    request.send(registrationDTO);
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            let json = JSON.parse(request.responseText);
            callback(json);
        }
    }
}


function getCreatingStatus(jsonData) {
    console.log(jsonData)

    switch (jsonData.status) {
        case 0:
            alert("Регистрация прошла успешно")
            callLoginPage();
            break;
        case 1:
            handleErrors(jsonData.errors);
            break;
        default:
            alert("Произошла неизвестная ошибка, обратитесь к разработчику.")
            break;
    }
}

function handleErrors(errors) {
    clearErrors();
    for (let i = 0; i < errors.length; i++) {
        handleError(errors[i])
    }
}

function handleError(error) {
    switch (error) {
        case ("INVALID_USER_NAME"):
            alert("Имя пользователя должно содержать от 3 до 20 символов!")
            setErrorView(loginDiv);
            break;
        case ("INVALID_PASSWORD"):
            alert("Пароль должен содержать от 8 до 20 символов!")
            setErrorView(passwordCheckDiv);
            setErrorView(passwordDiv);
            break;
        case ("USER_LOGIN_ALREADY_EXIST"):
            alert("Логин занят!")
            setErrorView(loginDiv);
            break;
    }
}

function setErrorView(element) {
    element.className = "error";
}

function setSuccessView(element) {
    element.className = "success"
}

function clearErrors() {
    setSuccessView(loginDiv);
    setSuccessView(passwordDiv);
    setSuccessView(passwordCheckDiv);

}

function getAuthenticationData() {
    let login = document.getElementById("login_input").value;
    let password = document.getElementById("password_input").value;
    return {
        'login': login,
        'password': password,
    }
}


function checkPassword() {
    let password = passwordInput.value;
    let passwordCheck = passwordCheckInput.value;

    if (!(password == passwordCheck)) {
        setErrorView(passwordDiv);
        setErrorView(passwordCheckDiv);
        alert("Пароли должны совпадать!");
        return false;
    } else if (password == "" || passwordCheck == "") {
        alert("Пароль не должен быть пустым.");
        setErrorView(passwordDiv);
        setErrorView(passwordCheckDiv);
        return false;
    }
    return true;
}

function checkLogin() {
    bindHtmlElements();
    let loginText = loginInput.value;
    console.log(loginText);
    if (loginText === "") {
        setErrorView(loginDiv);
        alert("Заполните имя пользователя")
        return false;
    }
    return true;
}

function checkInputs() {
    clearErrors();
    return (checkPassword() & checkLogin());
}

function callLoginPage() {
    window.location.href = api.LOGIN_PAGE;
}

function bindHtmlElements() {

    passwordDiv = document.getElementById("password_div");
    passwordCheckDiv = document.getElementById("password_check_div");
    loginDiv = document.getElementById("login_div");
    passwordInput = document.getElementById("password_input");
    passwordCheckInput = document.getElementById("password_check_input");
    loginInput = document.getElementById("login_input");
}





