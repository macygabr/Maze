const stompClient = new StompJs.Client({
    brokerURL: 'ws://10.54.202.32:8080/gs-guide-websocket'
});

var userName;

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);

    stompClient.subscribe('/topic/greetings', (greeting) => {
            showUser(greeting);
    });
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

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
});

function showUser(greeting) {
    var numbers = JSON.parse(greeting.body).coordUser.split(" ");

    console.log(JSON.parse(greeting.body).coordUser);
    var gridItems = document.querySelectorAll('.grid-item');
    var gridSize = Math.sqrt(gridItems.length);

    for (var i = 0; i < gridItems.length; i++) {
        var x = i % gridSize;
        var y = Math.floor(i / gridSize);
        var currentStyle = gridItems[i].getAttribute("style") || "";

        for(var k =0; k < numbers.length; k+=2){
            if(x == numbers[k] && y == numbers[k+1]) gridItems[i].setAttribute("style", currentStyle + "background-image: url(" + JSON.parse(greeting.body).direction + "); background-size: 100%; background-color: white;");
            else gridItems[i].setAttribute("style", currentStyle + "background-image: none;");

        }
    }
}