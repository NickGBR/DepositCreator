function callRegistrationPage() {
    window.location.href = api.REGISTRATION_PAGE;
}

function login() {
    authenticationDTO = JSON.stringify(getAuthenticationData());
    sendLoginRequest(authenticationDTO, handleResponse);
}

function sendLoginRequest(authenticationDTO, callback) {
    let request = new XMLHttpRequest();
    request.open("POST", api.LOGIN_POST_REQUEST, true);
    request.setRequestHeader("Content-Type", "application/json");
    request.send(authenticationDTO);
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status === 403) {
                alert("Не верный логин или пароль")
            } else {
                let response = JSON.parse(request.responseText);
                callback(response);
            }
        }
    }
}

function handleResponse(response) {
    sessionStorage.setItem(keys.AUTHORIZATION_TOKEN, response.token)
    changePage(api.MAIN_PAGE, response.token)
}

function getAuthenticationData() {
    let login = document.getElementById("login_input").value;
    let password = document.getElementById("password_input").value;
    return {
        'login': login,
        'password': password,
    }
}