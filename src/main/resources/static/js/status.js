window.onload = getStatus;

var addFriendButton = document.getElementById("addFriendButton");
addFriendButton.onclick = addFriend;
var statusp = document.getElementById("statusp");
var updateStatusButton = document.getElementById("changeStatus");
updateStatusButton.onclick = updateStatus;
var getFriendListRequest = new XMLHttpRequest();
var newStatusRequest = new XMLHttpRequest();

function addFriend() {
    var friend = document.getElementById("friend").value;
    // encodeURIComponent om UTF-8 te gebruiken en speciale karakters om te zetten naar code
    var informatie = 'email='+encodeURIComponent(friend);
    newStatusRequest.open("POST", "/addFriend", true);
    // belangrijk dat dit gezet wordt anders kan de servlet de informatie niet interpreteren!!!
    // protocol header information
    newStatusRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    newStatusRequest.send(informatie);
    statusp.innerHTML = status;
}

function updateStatus() {
    var status = document.getElementById("status").value;
    // encodeURIComponent om UTF-8 te gebruiken en speciale karakters om te zetten naar code
    var informatie = 'status='+encodeURIComponent(status);
    newStatusRequest.open("POST", "/updateStatus", true);
    // belangrijk dat dit gezet wordt anders kan de servlet de informatie niet interpreteren!!!
    // protocol header information
    newStatusRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    newStatusRequest.send(informatie);
    statusp.innerHTML = status;
}

function getStatus () {
    getFriendListRequest.open("GET", "/friendlist", true);
    getFriendListRequest.onreadystatechange = getData;
    getFriendListRequest.send();
}

function getData () {
    if (getFriendListRequest.status == 200) {
        if (getFriendListRequest.readyState == 4) {
            var serverResponse = JSON.parse(getFriendListRequest.responseText);
            var table = document.getElementById("friends");

            //clear table
            table.innerHTML= "";
            for (i = 0; i < serverResponse.length; i++) {

                var tr = table.childNodes[i];

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

            setTimeout(getData, 2000);
        }
    }
}