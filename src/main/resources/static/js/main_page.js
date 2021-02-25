function initMainPage(){

}

function create() {
    let request = new XMLHttpRequest();
    let userDTO;

    request.open("POST", api.CREATE_POST, true);
    request.setRequestHeader("Content-Type", "application/json");
    userDTO = JSON.stringify(getUserData());
    console.log(userDTO);
    request.send(userDTO);
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            let data = JSON.parse(request.responseText);
            getCreatingStatus(data);
        }
    }
}

    function getCreatingStatus(data) {
        alert(data);
    }

    function getUserData(){
        let name = document.getElementById("name_input").value;
        let surname = document.getElementById("surname_input").value;
        let middleName = document.getElementById("middle_name_input").value;
        let passportNumber = document.getElementById("passport_number").value;
        let dateOfBirthday = document.getElementById("date_of_birthday").value;

        return  {
            'name' : name,
            'surname' : surname,
            'middleName' : middleName,
            'passportNumber' : passportNumber,
            'dateOfBirthday' : dateOfBirthday
        }
}

function callPersonalDataAddingPage(){
changePage(api.ADD_PERSONAL_DATA_PAGE, sessionStorage.getItem(keys.AUTHORIZATION_TOKEN))
}