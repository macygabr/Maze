server = JSON.parse(server);
setCookie("maze", 1); 

console.log(server);
const stompClient = new StompJs.Client({
    brokerURL: `ws://${server.ip}:${server.port}/gs-guide-websocket`
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log("Connect: " + frame);

    stompClient.subscribe('/topic/greetings', (greeting) => {
        ListenServer(greeting);
    });
    stompClient.subscribe('/topic/reboot', async (greeting) => {
        server = JSON.parse(greeting.body);
        location.reload();
    });
    stompClient.subscribe('/topic/authentication', async (greeting) => {
        server = JSON.parse(greeting.body);
        if(server.user.authentication) window.location.href = '/field'
    });
    stompClient.subscribe('/topic/loadMap', (greeting) => {
        server = JSON.parse(greeting.body);
        if(server.user.authentication) window.location.href = '/upload'
    });
};

stompClient.activate();

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function setCookie(name, days) {
    if (document.cookie.split(';').some((item) => item.trim().startsWith(name + '='))) {
        console.log("Cookie already exists");
        return;
    }

    const timestamp = Date.now().toString(16);
    const randomNum = Math.floor(Math.random() * 10000);
    const uniqueID = timestamp + '-' + randomNum;

    let expires = "";
    if (days) {
        let date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (uniqueID || "") + expires + "; path=/";
    console.log("Cookie has been set");
}

// server = JSON.parse(server);
// console.log(server)
// const stompClient = new StompJs.Client({
//     brokerURL: `ws://${server.ip}:${server.port}/gs-guide-websocket`
// });

// stompClient.onConnect = (frame) => {
//     setConnected(true);
//     stompClient.subscribe('/topic/greetings', (greeting) => {
//         ListenServer(greeting);
//     });
//     stompClient.subscribe('/topic/reboot', async (greeting) => {
//         server = JSON.parse(greeting.body);
//         location.reload();
//     });
//     // stompClient.subscribe('/topic/authentication', async (greeting) => {
//     //     server = JSON.parse(greeting.body);
//     //     if(server.user.authentication) window.location.href = '/field'
//     //     document.cookie = encodeURIComponent(server.user.cookieName) + '=' + encodeURIComponent(server.user.cookieValue);
//     // });
//     stompClient.subscribe('/user/queue/reply', function (message) {
//         var server = JSON.parse(message.body);
//         console.log("Private message: " + server);
//     });
    
//     stompClient.subscribe('/topic/loadMap', (greeting) => {
//         server = JSON.parse(greeting.body);
//         if(server.user.authentication) window.location.href = '/upload'
//     });
//     document.cookie = encodeURIComponent(server.user.cookieName) + '=' + encodeURIComponent(server.user.cookieValue);
// };

// stompClient.activate();

// function setConnected(connected) {
//     $("#connect").prop("disabled", connected);
//     $("#disconnect").prop("disabled", !connected);
//     if (connected) {
//         $("#conversation").show();
//     } else {
//         $("#conversation").hide();
//     }
//     $("#greetings").html("");
// }
