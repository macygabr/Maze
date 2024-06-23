server = JSON.parse(server);
// console.log(server)
const stompClient = new StompJs.Client({
    brokerURL: `ws://${server.ip}:${server.port}/gs-guide-websocket`
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    stompClient.subscribe('/topic/greetings', (greeting) => {
        ListenServer(greeting);
    });
    stompClient.subscribe('/topic/reboot', async (greeting) => {
        server = JSON.parse(greeting.body);
        location.reload();
    });
    stompClient.subscribe('/topic/authentication', async (greeting) => {
        server = JSON.parse(greeting.body);
        console.log(server);
        if(server.user.authentication) window.location.href = '/field'
        document.cookie = encodeURIComponent(server.user.cookieName) + '=' + encodeURIComponent(server.user.cookieValue);
    });
    stompClient.subscribe('/topic/loadMap', (greeting) => {
        server = JSON.parse(greeting.body);
        if(server.user.authentication) window.location.href = '/upload'
    });
    document.cookie = encodeURIComponent(server.user.cookieName) + '=' + encodeURIComponent(server.user.cookieValue);
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