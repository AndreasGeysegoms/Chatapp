//waneer window ingeladen is, roep 'getFriends' op
window.onload = getFriends;

//variabelen + onclicks
var addFriendButton = document.getElementById("addFriendButton");
addFriendButton.onclick = addFriend;
var updateStatusButton = document.getElementById("changeStatus");
updateStatusButton.onclick = updateStatus;
var getFriendListRequest = new XMLHttpRequest();
var addFriendRequest = new XMLHttpRequest();
var updateStatusRequest = new XMLHttpRequest();

function addFriend() {
    //get input
    var friend = document.getElementById("friend").value;
    // encodeURIComponent om UTF-8 te gebruiken en speciale karakters om te zetten naar code
    //op te roepen in request.getAttribute("email")
    var informatie = 'email=' + encodeURIComponent(friend);
    //post request naar /addFriend met addFriendRequest
    addFriendRequest.open("POST", "/addFriend", true);
    // belangrijk dat dit gezet wordt anders kan de servlet de informatie niet interpreteren!!!
    // protocol header information
    addFriendRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    addFriendRequest.send(informatie);
    document.getElementById("friend").value = "";
}

function updateStatus() {
    var statusp = document.getElementById("statusp");
    //get input
    var status = document.getElementById("status").value;
    // encodeURIComponent om UTF-8 te gebruiken en speciale karakters om te zetten naar code
    //op te roepen in request.getAttribute("status")
    var informatie = 'status=' + encodeURIComponent(status);
    //post request naar /updateStatus met updateStatusRequest
    updateStatusRequest.open("POST", "/updateStatus", true);
    // belangrijk dat dit gezet wordt anders kan de servlet de informatie niet interpreteren!!!
    // protocol header information
    updateStatusRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    updateStatusRequest.send(informatie);
    //verfijning: status updaten in html
    statusp.innerHTML = status;
}

function getFriends() {
    //get request naar /friendlist met getFriendListRequest
    getFriendListRequest.open("GET", "/friendlist", true);
    //wanneer deze een response krijgt: roep 'getData' op
    getFriendListRequest.onreadystatechange = getData;
    getFriendListRequest.send();
}

function getData() {
    //als request succes is
    if (getFriendListRequest.status == 200) {
        //als juiste status
        if (getFriendListRequest.readyState == 4) {
            //parse responseText als JSON
            var serverResponse = JSON.parse(getFriendListRequest.responseText);
            var table = document.getElementById("friends");

            console.log("retrieving friends...");
            //clear table
            table.innerHTML = "";
            for (i = 0; i < serverResponse.length; i++) {

                var tr = table.childNodes[i];

                //get from JSON
                var friend = serverResponse[i].firstName;
                var status = serverResponse[i].status;

                var tableRow = document.createElement("tr");
                var tableDataName = document.createElement("td");
                var tableDataStatus = document.createElement("td");


                tableDataName.innerHTML = friend;
                tableDataStatus.innerHTML = status;

                tableRow.appendChild(tableDataName);
                tableRow.appendChild(tableDataStatus);
                table.appendChild(tableRow);

            }
            //voer 'getFriends' uit om de 2000ms
            setTimeout(getFriends, 2000);
        }
    }
}