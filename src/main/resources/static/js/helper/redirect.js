function init(){
    chekAuthority()
}

function chekAuthority(){
    console.log(sessionStorage.getItem(keys.AUTHORIZATION_TOKEN))
    if(sessionStorage.getItem(keys.AUTHORIZATION_TOKEN)==null || document.title === "HTTP Status 401 – Unauthorized"){
        document.cookie = "";
        window.location.href = api.LOGIN_PAGE;
    }
}

function changePage(url, token){
    document.cookie = "Authorization=" + token;
    window.location.href = url;
}