function callRegistrationPage(){
    window.location.href = api.REGISTRATION_PAGE;
}

function login(){
    let login = document.getElementById("login_input").value;
    let password = document.getElementById("password_input");
sendLoginRequest(login, password, handleResponse);
}

function sendLoginRequest(login, password, callback) {
    let request = new XMLHttpRequest();
    request.open("POST", "/login", true);
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("?username=" + login + "&password=" + password)
    console.log(request);
    request.send();
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            let response = JSON.parse(request.responseText);
            callback(response);
        }
    }
}

function handleResponse(response){
    console.log(response);
}