function login() {
    const loginInput = document.getElementById('floatingInput').value;
    const passInput = document.getElementById('floatingPassword').value;
    
    stompClient.publish({
        destination: "/app/authentication",
        body: JSON.stringify({'login': loginInput, 'pass': passInput, 'cookie' : document.cookie})
    });
}

function sign() {
    const loginInput = document.getElementById('floatingInput').value;
    const passInput = document.getElementById('floatingPassword').value;
    
    stompClient.publish({
        destination: "/app/sign",
        body: JSON.stringify({'login': loginInput, 'pass': passInput, 'cookie' : document.cookie})
    });
}