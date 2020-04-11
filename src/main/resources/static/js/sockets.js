// variabelen
var aWebSocket;
var aMessages = document.getElementById("aMessages");
var aOpen = document.getElementById("aOpen");
// onclick instellen; als () achter de functie staat wordt deze na het inladen vd pagina uitgevoerd
aOpen.onclick=openSocketA;
var aSend = document.getElementById("aSend");
aSend.onclick=sendA;
var aClose = document.getElementById("aClose");
aClose.onclick=closeSocketA;

var bWebSocket;
var bMessages = document.getElementById("bMessages");
var bOpen = document.getElementById("bOpen");
bOpen.onclick=openSocketB;
var bSend = document.getElementById("bSend");
bSend.onclick=sendB;
var bClose = document.getElementById("bClose");
bClose.onclick=closeSocketB;

var cWebSocket;
var cMessages = document.getElementById("cMessages");
var cOpen = document.getElementById("cOpen");
cOpen.onclick=openSocketC;
var cSend = document.getElementById("cSend");
cSend.onclick=sendC;
var cClose = document.getElementById("cClose");
cClose.onclick=closeSocketC;

var dWebSocket;
var dMessages = document.getElementById("dMessages");
var dOpen = document.getElementById("dOpen");
dOpen.onclick=openSocketD;
var dSend = document.getElementById("dSend");
dSend.onclick=sendD;
var dClose = document.getElementById("dClose");
dClose.onclick=closeSocketD;

var eWebSocket;
var eMessages = document.getElementById("eMessages");
var eOpen = document.getElementById("eOpen");
eOpen.onclick=openSocketE;
var eSend = document.getElementById("eSend");
eSend.onclick=sendE;
var eClose = document.getElementById("eClose");
eClose.onclick=closeSocketE;

function openSocketA(){
    //endpoint van socket
    aWebSocket = new WebSocket("ws://localhost:8080/a");

    //socket client side openen
    aWebSocket.onopen = function(event){
        writeResponse("Connection opened", aMessages)
    };

    //berichten door socket versturen wanneer opgeroepen
    aWebSocket.onmessage = function(event){
        writeResponse(event.data, aMessages);
    };

    //socket sluiten wanneer sluiten
    aWebSocket.onclose = function(event){
        writeResponse("Connection closed", aMessages);
    };
}

//onmessage van socket wordt opgeroepen
function sendA(){
    var text = document.getElementById("aName").value + ': ' + document.getElementById("aRemark").value + ' ' + document.getElementById("aScore").value;
    aWebSocket.send(text);
}

//onclose van socket wordt opgeroepen
function closeSocketA(){
    aWebSocket.close();
}

function openSocketB(){
    bWebSocket = new WebSocket("ws://localhost:8080/b");

    bWebSocket.onopen = function(event){
        writeResponse("Connection opened", bMessages)
    };

    bWebSocket.onmessage = function(event){
        writeResponse(event.data, bMessages);
    };

    bWebSocket.onclose = function(event){
        writeResponse("Connection closed", bMessages);
    };
}

function sendB(){
    var text = document.getElementById("bName").value + ': ' + document.getElementById("bRemark").value + ' ' + document.getElementById("bScore").value;
    bWebSocket.send(text);
}

function closeSocketB(){
    bWebSocket.close();
}

function openSocketC(){
    cWebSocket = new WebSocket("ws://localhost:8080/c");

    cWebSocket.onopen = function(event){
        writeResponse("Connection opened", cMessages)
    };

    cWebSocket.onmessage = function(event){
        writeResponse(event.data, cMessages);
    };

    cWebSocket.onclose = function(event){
        writeResponse("Connection closed", cMessages);
    };
}

function sendC(){
    var text = document.getElementById("cName").value + ': ' + document.getElementById("cRemark").value + ' ' + document.getElementById("cScore").value;
    cWebSocket.send(text);
}

function closeSocketC(){
    cWebSocket.close();
}

function openSocketD(){
    dWebSocket = new WebSocket("ws://localhost:8080/d");

    dWebSocket.onopen = function(event){
        writeResponse("Connection opened", dMessages)
    };

    dWebSocket.onmessage = function(event){
        writeResponse(event.data, dMessages);
    };

    dWebSocket.onclose = function(event){
        writeResponse("Connection closed", dMessages);
    };
}

function sendD(){
    var text = document.getElementById("dName").value + ': ' + document.getElementById("dRemark").value + ' ' + document.getElementById("dScore").value;
    dWebSocket.send(text);
}

function closeSocketD(){
    dWebSocket.close();
}

function openSocketE(){
    eWebSocket = new WebSocket("ws://localhost:8080/e");

    eWebSocket.onopen = function(event){
        writeResponse("Connection opened", eMessages)
    };

    eWebSocket.onmessage = function(event){
        writeResponse(event.data, eMessages);
    };

    eWebSocket.onclose = function(event){
        writeResponse("Connection closed", eMessages);
    };
}

function sendE(){
    var text = document.getElementById("eName").value + ': ' + document.getElementById("eRemark").value + ' ' + document.getElementById("eScore").value;
    eWebSocket.send(text);
}

function closeSocketE(){
    eWebSocket.close();
}

function writeResponse(text, div){
    div.innerHTML += "<br/>" + text;
}