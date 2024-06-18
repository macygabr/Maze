const stompClient = new StompJs.Client({
    // brokerURL: 'ws://10.54.202.32:8080/gs-guide-websocket'
    brokerURL: 'ws://37.194.168.90:8080/gs-guide-websocket'
});

var userName;

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);

//    stompClient.subscribe('/topic/greetings', (greeting) => {
//            showFild(JSON.parse(greeting.body).content);
//    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};


function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function callOnConnect() {
    stompClient.onConnect();
}

function sendMessage(element) {
    var gridItems = document.querySelectorAll('.grid-item');
    var gridSize = Math.sqrt(gridItems.length);
    var index = Array.from(gridItems).indexOf(element);
    var x = index % gridSize;
    var y = Math.floor(index / gridSize);

    stompClient.publish({
        destination: "/app/hello",
        body: JSON.stringify({'x': x, 'y': y, 'name': "test"})
    });
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
});