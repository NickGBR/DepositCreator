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
let depositsTableDiv;
let depositsTableBody;
let youDontHaveDepositsHolder;
let addDepositButton;
let depositsGettingErrorDiv;
let depositsGettingSpinner;
let mvdCheckingTimeoutErrorDiv;

let depositOpeningCheckingTimeout = 5000;
let depositOpeningCheckingIntervalId;
let save = "x-auth-token";

function socketTest() {
    console.log(sessionStorage.getItem(keys.AUTHORIZATION_TOKEN))
    const socket = new SockJS(api.SOCKET_ENDPOINT);
    stompClient = Stomp.over(socket);
    stompClient.connect({'x-auth-token': "Bearer" + sessionStorage.getItem(keys.AUTHORIZATION_TOKEN)}, afterSocketConnect, onSocketError);
}

function onSocketError() {
}

function afterSocketConnect() {
    stompClient.subscribe(api.SOCKET_DEPOSIT_ENDPOINT, handleDepositOpeningInfo)
}

function handleDepositOpeningInfo() {
}


function initMainPage() {
    bindHtmlElements();
    getPersonalDataRequest();
    getDepositsRequest();
}

function openDepositRequest() {
    hideErrors();
    makeNotActive(addDepositButton);
    showElement(depositOpeningSpinnerDiv);
    let request = new XMLHttpRequest();

    request.open("GET", api.OPEN_DEPOSIT_GET_REQUEST, true);
    request.setRequestHeader("Authorization", sessionStorage.getItem(keys.AUTHORIZATION_TOKEN));
    request.send();
    request.onreadystatechange = function () {
        if (request.readyState === 4) {
            handleOpenDepositRequest(request)
        }
    }
}

function handleOpenDepositRequest(request) {
    if (request.status === 200) {
        depositOpeningCheckingIntervalId = setInterval(checkDepositOpeningStatus, depositOpeningCheckingTimeout);

    }
}

function checkDepositOpeningStatus() {
    let request = new XMLHttpRequest();

    request.open("GET", api.CHECK_DEPOSIT_STATUS_GET_REQUEST, true);
    request.setRequestHeader("Authorization", sessionStorage.getItem(keys.AUTHORIZATION_TOKEN));
    request.send();
    request.onreadystatechange = function () {
        if (request.readyState === 4) {
            handleDepositOpeningStatus(request);
        }
    }
}

function handleDepositOpeningStatus(request) {
    console.log(request.responseText)
    let response = JSON.parse(request.responseText);
    let status = response.status;
    switch (status) {
        case statuses.WAITING:
            console.log("WAITING")
            console.log(response)
            break;
        case statuses.SUCCESS:
            console.log("SUCCESS")
            // Делаем кнопку открытия депозита активной
            makeActive(addDepositButton);
            // Запрашиваем депозиты
            getDepositsRequest();
            // Прекразщаем переодический запрос статуса открытия счета
            clearInterval(depositOpeningCheckingIntervalId);
            break;
        case statuses.ERROR:
            handleDepositOpeningError(response)
            makeActive(addDepositButton);
            clearInterval(depositOpeningCheckingIntervalId);
            console.log("FAILED")
            break;
    }
}

function handleDepositOpeningError(json) {
    let i = 0;
    let mvdErrorsList = json.errors;
    console.log(mvdErrorsList);

    for (i; i < mvdErrorsList.length; i++) {
        if (mvdErrorsList[i] === "MVD_TERRORIST_ERROR") {
            showElement(personalDataCheckingErrorTerroristDiv);
            hideElement(depositOpeningSpinnerDiv);
        }
        if (mvdErrorsList[i] === "MVD_PERSONAL_DATA_DOESNT_EXIST") {
            showElement(personalDataCheckingErrorDataDiv);
            hideElement(depositOpeningSpinnerDiv);
        }
        if (mvdErrorsList[i] === "MVD_TIME_OUT_ERROR") {
            showElement(mvdCheckingTimeoutErrorDiv);
            hideElement(depositOpeningSpinnerDiv);
        }
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
            return handlePersonalDataRequest(request);
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
            return true;
        } else {
            activeDepositHolder(false);
            setPersonalDataView(false);
            return false;
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

function getDepositsRequest() {
    let request = new XMLHttpRequest();

    request.open("GET", api.GET_DEPOSITS_GET_REQUEST, true);
    request.setRequestHeader("Content-Type", "application/json");
    request.setRequestHeader("Authorization", sessionStorage.getItem(keys.AUTHORIZATION_TOKEN));
    request.send();
    showElement(depositsGettingSpinner);
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            handleDepositsRequest(request);
        }
    }
}

function handleDepositsRequest(request) {
    let json = JSON.parse(request.responseText);
    console.log(json)
    if (request.status === 200) {
        const status = json.status;
        switch (status) {
            case statuses.SUCCESS:
                hideElement(youDontHaveDepositsHolder);
                hideElement(depositsGettingSpinner);
                hideElement(depositOpeningSpinnerDiv);
                hideElement(depositsGettingErrorDiv)

                showElement(addDepositButton);
                showElement(depositsTableDiv);
                showTableWithDeposits(json)

                break;
            case statuses.ERROR:
                hideElement(depositsTableDiv)
                hideElement(depositsGettingErrorDiv)
                hideElement(depositsGettingSpinner);
                hideElement(depositOpeningSpinnerDiv)

                showElement(addDepositButton)
                showElement(youDontHaveDepositsHolder)
                break;
            default:
                showElement(depositsGettingErrorDiv);
                hideElement(youDontHaveDepositsHolder);
                hideElement(depositsTableDiv);
                hideElement(depositOpeningSpinnerDiv);
                hideElement(depositsGettingSpinner);
                hideElement(addDepositButton);
                console.log(`Sorry, there is no handle for ` + status + ' status.');
                break;
        }
    }
    if (request.status === 500) {
        console.log(errors.text.SERVER_PROBLEM)
        showElement(depositsGettingErrorDiv);
        hideElement(youDontHaveDepositsHolder);
        hideElement(depositsTableDiv);
        hideElement(depositOpeningSpinnerDiv);
        hideElement(depositsGettingSpinner);
        hideElement(addDepositButton);
    }
}

function showTableWithDeposits(json) {
    depositsTableBody.innerText = "";
    for (var i = 0; i < json.deposits.length; i++) {
        var deposit = json.deposits[i];
        addDepositsTableRow(deposit.depositNumber, deposit.depositAmount, 'RUB')
    }
    showElement(depositsTableDiv);
}

function addDepositsTableRow(depositNumber, depositAmount, currency) {
    let depositNumberTh = document.createElement("td")
    let depositNumberText = document.createTextNode(depositNumber);
    depositNumberTh.appendChild(depositNumberText);

    let depositAmountTh = document.createElement("td")
    let depositAmountText = document.createTextNode(depositAmount);
    depositAmountTh.appendChild(depositAmountText);

    let currencyTh = document.createElement("td")
    let currencyText = document.createTextNode(currency);
    currencyTh.appendChild(currencyText);

    let depositTr = document.createElement("tr")
    depositTr.appendChild(depositNumberTh);
    depositTr.appendChild(depositAmountTh);
    depositTr.appendChild(currencyTh);
    depositsTableBody.appendChild(depositTr);
}

function activeDepositHolder(isActive) {
    if (isActive) {
        depositHolderTab.setAttribute("class", "nav-link")
    } else {
        depositHolderTab.setAttribute("class", "nav-link disabled")
    }
}

function hideElement(element) {
    element.style = "display:none"
}

function showElement(element) {
    element.style = ""
}

function makeActive(element) {
    element.disabled = false;
}

function makeNotActive(element) {
    element.disabled = true;
}

function hideErrors(){
    hideElement(personalDataCheckingErrorDataDiv);
    hideElement(personalDataCheckingErrorTerroristDiv);
    hideElement(mvdCheckingTimeoutErrorDiv);
}

function bindHtmlElements() {
    depositHolderTab = document.getElementById("deposit_holder_button");
    depositOpeningSpinnerDiv = document.getElementById("deposit_opening_spinner_div");
    personalDataCheckingSpinnerDiv = document.getElementById("personal_data_spinner_div");
    personalDataCheckingSuccessDiv = document.getElementById("success_personal_data_checking_div");
    personalDataCheckingErrorDataDiv = document.getElementById("error_personal_data_checking_div");
    personalDataCheckingErrorTerroristDiv = document.getElementById("terrorist_error_div");
    depositsTableDiv = document.getElementById("deposits_table_div");
    depositsTableBody = document.getElementById("deposits_table_body");
    youDontHaveDepositsHolder = document.getElementById("you_dont_have_deposits_holder");
    addDepositButton = document.getElementById("add_deposit_button");
    depositsGettingErrorDiv = document.getElementById("deposits_getting_error_div");
    depositsGettingSpinner = document.getElementById("deposits_getting_spinner");
    mvdCheckingTimeoutErrorDiv = document.getElementById("mvd_checking_timeout_error_div");
}


