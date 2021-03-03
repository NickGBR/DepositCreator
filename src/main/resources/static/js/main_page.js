// Константы содержащие id эллиментов, которые в зависимости от наличия персональных данных,
// либо отображаются либо нет.
let personalDataEmpty = "personal_data_empty"
let personalDataExist = "personal_data_exist"
let depositOpeningSpinnerDiv;
let personalDataCheckingSpinnerDiv;
let personalDataCheckingSuccessDiv;
let personalDataCheckingErrorDataDiv;
let personalDataCheckingErrorTerroristDiv;
let depositHolderTab;
let save = "x-auth-token";

function socketTest() {
    console.log(sessionStorage.getItem(keys.AUTHORIZATION_TOKEN))
    const socket = new SockJS(api.SOCKET_ENDPOINT);
    stompClient = Stomp.over(socket);
    stompClient.connect({'x-auth-token': "Bearer" + sessionStorage.getItem(keys.AUTHORIZATION_TOKEN)}, afterSocketConnect, onSocketError);
}

function onSocketError(){
    console.log("Socket ERROR")
}

function afterSocketConnect(){
    console.log("SUCCESSFULLY");
    stompClient.subscribe(api.SOCKET_DEPOSIT_ENDPOINT, handleDepositOpeningInfo)
}

function handleDepositOpeningInfo() {
console.log("ChtoToPrishlo")
}


function initMainPage() {
    getPersonalDataRequest();
    bindHtmlElements();
    hideElement(depositOpeningSpinnerDiv);
}

function openDepositRequest(){
    let request = new XMLHttpRequest();

    request.open("GET", api.OPEN_DEPOSIT_GET_REQUEST, true);
    request.setRequestHeader("Authorization", sessionStorage.getItem(keys.AUTHORIZATION_TOKEN));
    request.send();
    request.onreadystatechange = function (){
        console.log(request.responseText);
    }
}

function getPersonalDataRequest() {
    let request = new XMLHttpRequest();

    request.open("GET", api.PERSONAL_DATA_GET_REQUEST, true);
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", sessionStorage.getItem(keys.AUTHORIZATION_TOKEN));

    request.send();
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            handlePersonalDataRequest(request);
        }
    }
}

function deletePersonalData() {
    if (confirm("Вы уверены, что хотите безвозвратно удалить свои данные?")) {
        deletePersonalRequestData()
    } else {
    }
}

function deletePersonalRequestData() {
    let request = new XMLHttpRequest();
    request.open("DELETE", api.PERSONAL_DATA_DELETE_REQUEST, true);
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", sessionStorage.getItem(keys.AUTHORIZATION_TOKEN));
    request.send();
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            handlePersonalDataDeleteRequest(request);
        }
    }
}

function handlePersonalDataRequest(request) {
    if (request.status === 200) {
        if (request.responseText !== "") {
            fillPersonalDataTable(request);
            activeDepositHolder(true);
            setPersonalDataView(true)
        } else {
            activeDepositHolder(false);
            setPersonalDataView(false);
        }
    }
}

function handlePersonalDataDeleteRequest(request) {
    if (request.status === 200) {
        if (request.responseText === "true") {
            getPersonalDataRequest();
            alert("Персональные данные успешно удалены")
        } else alert("Персональные данные не найдены!")
    } else {
        alert("Произошла ошибка при удалении персональных данных! Код " + request.status + "!")
    }
}


function callPersonalDataAddingPage() {
    changePage(api.ADD_PERSONAL_DATA_PAGE, sessionStorage.getItem(keys.AUTHORIZATION_TOKEN))
}

//Функции отвечающие за внешний вид и появление эллиментов


function fillPersonalDataTable(request) {
    let json = JSON.parse(request.responseText);
    let fio = json.name + " " + json.surname;
    let date = new Date(json.dateOfBirthday);

    fillTableRow("FIO_row", fio);
    fillTableRow("address_row", json.address);
    fillTableRow("date_of_birthday_row", getFormattedDate(date));
    fillTableRow("passport_number_row", json.passportNumber)
}

function getFormattedDate(date) {
    const ye = new Intl.DateTimeFormat('ru', {year: 'numeric'}).format(date);
    const mo = new Intl.DateTimeFormat('ru', {month: '2-digit'}).format(date);
    const da = new Intl.DateTimeFormat('ru', {day: '2-digit'}).format(date);
    return `${da}-${mo}-${ye}`
}

function fillTableRow(keyId, value) {
    document.getElementById(keyId).innerText = value;
}

function setPersonalDataView(isPersonalDataExist) {
    let divForExistingData = document.getElementById(personalDataExist);
    let divForEmptyData = document.getElementById(personalDataEmpty);
    if (isPersonalDataExist) {
        divForEmptyData.style = "display:none";
        divForExistingData.style = "";
    } else {
        divForExistingData.style = "display:none";
        divForEmptyData.style = "";
    }
}

function activeDepositHolder(isActive){
    if(isActive) {
        depositHolderTab.setAttribute("class", "nav-link")
    }
    else {
        depositHolderTab.setAttribute("class", "nav-link disabled")
    }
}

function hideElement(element){
    element.style = "display:none"
}

function  showElement(element){
    element.style = ""
}

function bindHtmlElements(){
    depositHolderTab = document.getElementById("deposit_holder_button");
    depositOpeningSpinnerDiv = document.getElementById("deposit_opening_spinner");
    personalDataCheckingSpinnerDiv = document.getElementById("personal_data_spinner_div");;
    personalDataCheckingSuccessDiv = document.getElementById("success_personal_data_checking_div");;
    personalDataCheckingErrorDataDiv = document.getElementById("error_personal_data_checking_div");
    personalDataCheckingErrorTerroristDiv = document.getElementById("terrorist_error_div");
}


