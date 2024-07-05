function connect() {
    RenderField();
}


function RenderField() {
    console.log(server);
    var gridContainer = document.querySelector('.grid-container');
    if (!gridContainer) {
        console.error("Element with class 'grid-container' not found.");
        return;
    }
    gridContainer.innerHTML = '';
    var size = server.field.size
    var arr = server.field.result
    for (var i = 0; i < size*size; i++) {
        var div = document.createElement('div');
        div.className = 'grid-item';
        div.style.width = 100.0/size + "%";
        div.style.height = 100.0/size + "%";

        var fieldimg = new Image();
        fieldimg.className = 'img';

        if(server.field.result[i].right == 1 && server.field.result[i].down == 1) fieldimg.src = "/img/field/fieldBoth.png";
        else if(server.field.result[i].right == 1) fieldimg.src = "/img/field/fieldRight.png";
        else if(server.field.result[i].down == 1) fieldimg.src = "/img/field/fieldDown.png";
        else fieldimg.src = "/img/field/fieldEmpty.png";

        div.appendChild(fieldimg);
        gridContainer.appendChild(div);
    }
    // RenderPath();
    RenderUsers();
    RenderCheese();
}

function RenderPath() {
    gridItems = document.querySelectorAll('.grid-item');
    gridSize = server.field.size;
    
    gridItems.forEach(item => {
        const cheeseImages = item.querySelectorAll('.cheesepath-img');
        cheeseImages.forEach(image => image.remove());
    });
    
       for (var i = 0; i < gridSize*gridSize; i++) {
           if(server.field.result[i].path) {
                var image = new Image();
                image.className = 'cheesepath-img';
                image.src = "/img/field/path.png";
                gridItems[i].appendChild(image);
            }
     }
}

function RenderCheese(){
    gridItems = document.querySelectorAll('.grid-item');
    gridSize = server.field.size;
    
    gridItems.forEach(item => {
        const cheeseImages = item.querySelectorAll('.cheese-img');
        cheeseImages.forEach(image => image.remove());
    });
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


function RenderUsers() {
    var gridItems = document.querySelectorAll('.grid-item');
    var gridSize = server.field.size;
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
            if (users[k].authentication && x == users[k].x && y == users[k].y) {
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

function ListenServer(greeting) {
    server = JSON.parse(greeting.body);
    RenderCheese();
    RenderUsers();
    RenderPath();
}

function Reboot() {
    stompClient.publish({
        destination: "/app/reboot",
        body: JSON.stringify({'cookie' : document.cookie})
    });
    event.preventDefault();
}

function saveMap() {
    stompClient.publish({
        destination: "/app/saveMap",
        body: JSON.stringify({'cookie' : document.cookie})
    });
    event.preventDefault();
}

function loadMap() {
    stompClient.publish({
        destination: "/app/loadMap",
        body: JSON.stringify({'cookie' : document.cookie})
    });
    event.preventDefault();
}


function SetSize(val) {
    if (stompClient && stompClient.connected) {
        stompClient.publish({
            destination: "/app/setSize",
            body: JSON.stringify({'sizeMap': val, 'cookie': document.cookie})
        });
    } else {
        console.error('No STOMP connection established.');
        if (!stompClient.active) {
            stompClient.activate();
            SetSize(val);
        }
    }
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

function Help() {
    stompClient.publish({
            destination: "/app/findPath",
            body: JSON.stringify({'cookie' : document.cookie})
    });
}

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



// function ChangeField() {
//     gridItems = document.querySelectorAll('.grid-item');
//     var pathX = PathX.split(" ");
//     var pathY = PathY.split(" ");
//     for (var i = 0; i < gridItems.length; i++) {
//         y = i % gridSize;
//         x = Math.floor(i / gridSize);

//         for(var k=0; k<pathX.length; k++) {
//             if(pathX[k] == x && pathY[k] == y && pathX[k]!="" && pathY[k] != "") {
//                 var field = new Image();
//                 field.src = "/img/СheeseСrumbles.png";
//                 field.style.width  = "100%";
//                 field.style.height = "100%";
//                 if (gridItems[i].children.length > 0) gridItems[i].children[0].replaceWith(field);
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


