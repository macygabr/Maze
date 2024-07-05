function login() {
    const loginInput = document.getElementById('floatingInput').value;
    const passInput = document.getElementById('floatingPassword').value;

    // stompClient.publish({
    //     destination: "/app/authentication",
    //     body: JSON.stringify({
    //         'login': loginInput,
    //         'pass': passInput,
    //         'cookie': document.cookie
    //     })
    // });
    fetch('/authentication', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            'login': loginInput,
            'pass': passInput,
            'cookie': document.cookie
        })
    })
    .then(response => response.json())
    .then(data => {
        if(data.user.authentication){
            console.log('Received from server:', data);
            window.location.href = '/field';
        }
    })
    .catch(error => {
        console.error('Error:', error);
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