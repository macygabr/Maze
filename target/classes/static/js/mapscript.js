server = JSON.parse(server);
console.log(server)
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
};

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

function SetCookies() {
    document.cookie = encodeURIComponent(server.user.cookieName) + '=' + encodeURIComponent(server.user.cookieValue);
}

function connect() {
    stompClient.activate();
    RenderFild();
    SetCookies();
}


function RenderFild() {
    var gridContainer = document.querySelector('.grid-container');
    if (!gridContainer) {
        console.error("Element with class 'grid-container' not found.");
        return;
    }
    gridContainer.innerHTML = '';
    var size = server.fild.size
    var arr = server.fild.result
    for (var i = 0; i < size*size; i++) {
        var div = document.createElement('div');
        div.className = 'grid-item';
        div.style.width = 100.0/size + "%";
        div.style.height = 100.0/size + "%";

        var fildimg = new Image();
        fildimg.className = 'img';

        if(server.fild.result[i].right == 1 && server.fild.result[i].down == 1) fildimg.src = "/img/fild/fildBoth.png";
        else if(server.fild.result[i].right == 1) fildimg.src = "/img/fild/fildRight.png";
        else if(server.fild.result[i].down == 1) fildimg.src = "/img/fild/fildDown.png";
        else fildimg.src = "/img/fild/fildEmpty.png";

        div.appendChild(fildimg);
        gridContainer.appendChild(div);
    }
    RenderUsers();
    RenderCheese();
}

function RenderCheese(){
    gridItems = document.querySelectorAll('.grid-item');
    gridSize = server.fild.size;

       for (var i = 0; i < gridSize*gridSize; i++) {
           y = i % gridSize;
           x = Math.floor(i / gridSize);
           if(x == server.cheese.x && y == server.cheese.y) {
                var image = new Image();
                image.className = 'cheese-img';
                image.src = server.cheese.path;
                gridItems[i].appendChild(image);
                break;
            }
     }
}

function ListenServer(greeting) {
    server = JSON.parse(greeting.body);
    RenderUsers();
}

function RenderUsers() {
    var gridItems = document.querySelectorAll('.grid-item');
    var gridSize = server.fild.size;
    const userCount = Object.keys(server.users).length;
    
    gridItems.forEach(item => {
        var userImages = item.querySelectorAll('.user-img');
        userImages.forEach(img => item.removeChild(img));
    });

    for (var i = 0; i < gridItems.length; i++) {
        y = i % gridSize;
        x = Math.floor(i / gridSize);
        for (var k = 0; k < userCount; k++) {
            var users = Object.values(server.users);
            if (x == users[k].x && y == users[k].y) {
                var image = new Image();
                image.src = users[k].png;
                image.className = 'user-img';
                image.style.transform = "rotate(" + users[k].rotate + "deg)";
                gridItems[i].appendChild(image);
                break;
            }
        }
    }
}

function Reboot() {
    stompClient.publish({
        destination: "/app/reboot",
        body: JSON.stringify({'cookie' : document.cookie})
    });
    event.preventDefault();
}

function SetSize(val) {
    stompClient.publish({
            destination: "/app/setSize",
            body: JSON.stringify({'sizeMap': val, 'cookie' :  document.cookie})
    });
}

document.addEventListener('keydown', function(event) {
    var dirX = 0;
    var dirY = 0;
    if(event.key == "ArrowUp") dirX = -1;
    else if(event.key == "ArrowDown") dirX = 1;
    else if(event.key == "ArrowLeft")  dirY = -1;
    else if(event.key == "ArrowRight") dirY = 1;
    else return;

    stompClient.publish({
           destination: "/app/move",
           body: JSON.stringify({'x': dirX, 'y': dirY, 'cookie' : document.cookie})
    });
});

// function addActiveClass() {
//     var element = document.querySelector('.nav-link.active');
//     if (element) {
//         element.classList.remove('active');
//     }
//     var rebootElement = document.querySelector('.nav-link');
//     rebootElement.classList.add('active');
//     rebootElement.classList.remove('link-dark');
// }

// function removeActiveClass() {
//     var rebootElement = document.querySelector('.nav-link.active');
//     rebootElement.classList.remove('active');
//     rebootElement.classList.add('link-dark');
// }


// stompClient.onWebSocketError = (error) => {
//     console.error('Error with websocket', error);
// };

// stompClient.onStompError = (frame) => {
//     console.error('Broker reported error: ' + frame.headers['message']);
//     console.error('Additional details: ' + frame.body);
// };



// function Help() {
//     stompClient.publish({
//             destination: "/app/hello",
//             body: JSON.stringify({'x': x, 'y': y, 'auto': 1, 'cookieValue' : uuid})
//     });
// }

// function ChangeFild() {
//     gridItems = document.querySelectorAll('.grid-item');
//     var pathX = PathX.split(" ");
//     var pathY = PathY.split(" ");
//     for (var i = 0; i < gridItems.length; i++) {
//         y = i % gridSize;
//         x = Math.floor(i / gridSize);

//         for(var k=0; k<pathX.length; k++) {
//             if(pathX[k] == x && pathY[k] == y && pathX[k]!="" && pathY[k] != "") {
//                 var fild = new Image();
//                 fild.src = "/img/СheeseСrumbles.png";
//                 fild.style.width  = "100%";
//                 fild.style.height = "100%";
//                 if (gridItems[i].children.length > 0) gridItems[i].children[0].replaceWith(fild);
//                 console.log(x + " " + y + " " + pathX[k] + " " + pathY[k]);
//              }
//         }
//     }
// }

// function Download(){
//     stompClient.publish({
//             destination: "/app/files",
//             body: JSON.stringify({'Download': 1})
//     });
// }


