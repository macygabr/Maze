const stompClient = new StompJs.Client({
     brokerURL: 'ws://10.54.203.40:8080/gs-guide-websocket'
//    brokerURL: 'ws://localhost:8080/gs-guide-websocket'
//   brokerURL: 'ws://37.194.168.90:8080/gs-guide-websocket'
});

var userName;
var uuid;
var UserX =0;
var UserY =0;
var EnemyX;
var EnemyY;
var Greeting;
var Rotate = "0";
var PathX = "";
var PathY = "";

function connect() {
    stompClient.activate();
    RenderUsers();
    RenderFild();
    RenderCheese();
    SetCookies();
}
function RenderCheese(){
    gridItems = document.querySelectorAll('.grid-item');
    gridSize = Math.round(Math.sqrt(gridItems.length));

       for (var i = 0; i < gridItems.length; i++) {
           y = i % gridSize;
           x = Math.floor(i / gridSize);
            if(x == CheeseX && y == CheeseY) {
                var image = new Image();
                image.src = CheesePng;
                image.style.width =  "100%";
                image.style.height = "100%";
                if (gridItems[i].children.length > 0) gridItems[i].children[0].replaceWith(image);
                break;
            }
     }
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function callOnConnect() {
    stompClient.onConnect();
    Reboot(0,0,1);
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

function RenderFild() {
    gridItems = document.querySelectorAll('.grid-item');
    for (var i = 0; i < gridItems.length; i++) {
        y = i % gridSize;
        x = Math.floor(i / gridSize);
        var fild = new Image();
        fild.src = "/img/fild.png";
        fild.style.width  = "100%";
        fild.style.height = "100%";
        gridItems[i].appendChild(fild);
    }
}

function RenderUsers() {
    gridItems = document.querySelectorAll('.grid-item');
    gridSize = Math.round(Math.sqrt(gridItems.length));
    usersX = UsersX.split(" ");
    usersY = UsersY.split(" ");
    console.log(usersX + " " + usersY);
    usersPng = png.split(" ");
    rotate = Rotate.split(" ");

        for (var i = 0; i < gridItems.length; i++) {
        y = i % gridSize;
        x = Math.floor(i / gridSize);
            var currentStyle = gridItems[i].getAttribute("style") || "";

            for(var k=0; k<usersX.length; k++) {
                if(usersX[k] != "" && usersY[k] != "" && x == usersX[k] && y == usersY[k]) {
                    var image = new Image();
                    image.src = usersPng[k];
                    image.style.transform = "rotate("+ rotate[k] +"deg)";
                    image.style.width =  "100%";
                    image.style.height = "100%";
                    if (gridItems[i].children.length > 0) gridItems[i].children[0].replaceWith(image);
                    break;
                }
                else {
                      var fild = new Image();
                      fild.src = "/img/fild.png";
                      fild.style.width =  "100%";
                      fild.style.height = "100%";
                      if (gridItems[i].children.length > 0) gridItems[i].children[0].replaceWith(fild);
                }
            }
      }
}

function ListenServer(greeting) {
    UsersX = JSON.parse(greeting.body).UsersXCoord;
    UsersY = JSON.parse(greeting.body).UsersYCoord;
    Rotate = JSON.parse(greeting.body).Rotate;
    png = JSON.parse(greeting.body).UsersPng;
    CheeseX = JSON.parse(greeting.body).CheeseX;
    CheeseY = JSON.parse(greeting.body).CheeseY;
    CheesePng = JSON.parse(greeting.body).CheesePng;
    PathX = JSON.parse(greeting.body).pathX;
    PathY = JSON.parse(greeting.body).pathY;

    RenderUsers();
    RenderCheese();
    ChangeFild();
    if(JSON.parse(greeting.body).reboot == 1) location.reload();
}

function Reboot() {
    Send(0,0,1);
}


function addActiveClass() {
    var element = document.querySelector('.nav-link.active');
    if (element) {
        element.classList.remove('active');
    }
    var rebootElement = document.querySelector('.nav-link');
    rebootElement.classList.add('active');
    rebootElement.classList.remove('link-dark');
}

function removeActiveClass() {
    var rebootElement = document.querySelector('.nav-link.active');
    rebootElement.classList.remove('active');
    rebootElement.classList.add('link-dark');
}

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', (greeting) => {
            ListenServer(greeting);
    });
    Send(0,0,0);
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

document.addEventListener('keydown', function(event) {
    var dirX = 0;
    var dirY = 0;
    if(event.key == "ArrowUp") dirX = -1;
    else if(event.key == "ArrowDown") dirX = 1;
    else if(event.key == "ArrowLeft")  dirY = -1;
    else if(event.key == "ArrowRight") dirY = 1;
    else return;
    Send(dirX, dirY, 0);
});

function Send(x, y, reb) {
 stompClient.publish({
        destination: "/app/hello",
        body: JSON.stringify({'x': x, 'y': y, 'reboot': reb, 'cookieValue' : uuid})
    });
 }

function SetCookies(){
 document.cookie = encodeURIComponent("player") + '=' + encodeURIComponent(uuid);
}

function Help() {
    stompClient.publish({
            destination: "/app/hello",
            body: JSON.stringify({'x': x, 'y': y, 'auto': 1, 'cookieValue' : uuid})
    });
}

function ChangeFild() {
    gridItems = document.querySelectorAll('.grid-item');
    var pathX = PathX.split(" ");
    var pathY = PathY.split(" ");
    for (var i = 0; i < gridItems.length; i++) {
        y = i % gridSize;
        x = Math.floor(i / gridSize);

        for(var k=0; k<pathX.length; k++) {
            if(pathX[k] == x && pathY[k] == y && pathX[k]!="" && pathY[k] != "") {
                var fild = new Image();
                fild.src = "/img/СheeseСrumbles.png";
                fild.style.width  = "100%";
                fild.style.height = "100%";
                if (gridItems[i].children.length > 0) gridItems[i].children[0].replaceWith(fild);
                console.log(x + " " + y + " " + pathX[k] + " " + pathY[k]);
             }
        }
    }
}

function Download(){
    stompClient.publish({
            destination: "/app/files",
            body: JSON.stringify({'Download': 1})
    });
}


function SetSize(val){
  console.log("Size:", val);
    stompClient.publish({
            destination: "/app/fild",
            body: JSON.stringify({'sizeMap': val})
    });
    Reboot();
}