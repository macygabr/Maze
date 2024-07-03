server = JSON.parse(server);
setCookie("maze", 1);

stompClient = new StompJs.Client({
    brokerURL: `ws://${server.ip}:${server.port}/gs-websocket`
});

// function connectToServer() {
//     stompClient.connect({}, function (frame) {
//         console.log('Connected: ' + frame);

//         stompClient.subscribe('/topic/reboot', (greeting) => {
//             try {
//                 let serverData = JSON.parse(greeting.body);
//                 console.log("Message: ", serverData);
//                 console.log("Message Size: ", serverData.field.sizve);
//             } catch (e) {
//                 console.error("Error parsing message: ", e);
//             }
//         });

//     }, function (error) {
//         console.error('Connection error: ', error);
//         setTimeout(connect, 1000);
//     });
// }

// connectToServer();

stompClient.onclose = function() {
    console.error("WebSocket connection closed");
};

stompClient.onopen = function() {
    console.log("WebSocket connection opened");
};

stompClient.onerror = function(error) {
    console.error("WebSocket error: ", error);
};

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log("Connected: " + frame);

    stompClient.subscribe('/topic/greetings', (greeting) => {
        ListenServer(greeting);
    });
    
    stompClient.subscribe('/topic/reboot', async (greeting) => {
        server = JSON.parse(greeting.body);
        console.log("Message: " + server.field.size);
        // RenderField();
        location.reload();
    });
    
    stompClient.subscribe('/topic/findPath', async (greeting) => {
        server = JSON.parse(greeting.body);
        RenderPath();
        RenderCheese();
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
