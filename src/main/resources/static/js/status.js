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

//create een jqueryui chatbox of roept er een op
function startChatDialog(to_user_id, to_user_name) {
    if (!document.getElementById("chat_" + to_user_id)) {
        var chat = document.createElement("div");
        chat.title = "Chat met " + to_user_name;
        chat.id = "chat_" + to_user_id;
        var messages = document.createElement("div");
        messages.style = "height:400px; border: 1px solid #ccc overflow";
        messages.id = "messages_" + to_user_id;
        var input = document.createElement("input");
        input.id = "msg_" + to_user_id;
        var button = document.createElement("button");
        button.id = "send_" + to_user_id;
        button.innerHTML = "SEND";

        var status = document.createElement('p');
        status.id = "status_" + to_user_id;
        status.innerHTML = "Unknown";
        chat.appendChild(status);

        chat.appendChild(messages);
        chat.appendChild(input);
        chat.appendChild(button);
        document.getElementById("chatbox").appendChild(chat);
        console.log('created');
    } else {
        console.log('already exists');
    }

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

                if (!(friend === "Uw vriendenlijst is leeg." || friend === "Voeg meer vrienden toe!")) {
                    var btn = document.createElement("button");
                    btn.innerHTML = "Chat!";
                    btn.className = "start_chat";
                    btn.id = friend + '@ucll.be';
                    tableRow.appendChild(btn);
                }
                table.appendChild(tableRow);
                if (!(friend === "Uw vriendenlijst is leeg." || friend === "Voeg meer vrienden toe!")) {
                    document.getElementById(btn.id).addEventListener('click', function () {
                        var to_user_id = this.id;
                        var to_user_name = this.id.replace('@ucll.be', '');
                        startChatDialog(to_user_id, to_user_name);
                        $dialog = $("#chat_" + to_user_name + "\\@ucll\\.be");
                        $dialog.dialog({
                            autoOpen: false,
                            width: 400
                        });
                        $dialog.dialog('option', {
                            close: function (event, ui) {
                                $dialog.find("form").remove();
                                $dialog.empty();
                                $dialog.remove();
                            }
                        });
                        console.log("created dialog");
                        $dialog.dialog('open');
                        getMessages(to_user_id);
                        console.log("opened dialog");

                        document.getElementById('send_' + to_user_id).addEventListener('click', function () {
                            var bericht = $("#msg_" + to_user_name + "\\@ucll\\.be").val();
                            //input leeg maken
                            $("#msg_" + to_user_name + "\\@ucll\\.be").val("");
                            //ajax POST naar /sendMessage
                            $.ajax({
                                type: "POST",
                                url: "/sendMessage",
                                data: {
                                    bericht: bericht,
                                    ontvanger: to_user_id
                                },
                                async: 'true',
                                dataType: "json"
                            });

                        })
                    });
                }
            }
            //voer 'getFriends' uit om de 10000ms
            setTimeout(getFriends, 10000);
        }
    }

    //ajax GET polling
    function getMessages(ontvanger_id) {
        //als chatvenster open is (werkt niet 100%)
        if ($('#chat_' + ontvanger_id.replace('@', '\\@').replace('.', '\\.')).length !== 0 || !$('#chat_' + ontvanger_id.replace('@', '\\@').replace('.', '\\.')).dialog('isOpen')) {
            $.ajax({
                type: "Get",
                url: "/getMessages",
                data: {
                    ontvanger: ontvanger_id
                },
                async: 'true',
                dataType: "json",
                success: function (json) {
                    $("#messages_" + ontvanger_id.replace('@', '\\@').replace('.', '\\.')).empty();
                    $(json).each(function (index, chatbericht) {
                        console.log(ontvanger_id);
                        $("#messages_" + ontvanger_id.replace('@', '\\@').replace('.', '\\.')).append($('<p />').text(chatbericht.zender.userId + ' : ' + chatbericht.bericht));
                    });

                    setTimeout(function () {
                        getMessages(ontvanger_id);
                    }, 10000);

                }
            });
            getStatus(ontvanger_id);
        }

    }

    //AJAX GET eigen functionaliteit
    function getStatus(ontvanger_id) {
        $.get("/getStatus", {userId: ontvanger_id}, function (status) {
            $("#status_" + ontvanger_id.replace('@', '\\@').replace('.', '\\.')).html(status);
        })
    }
}