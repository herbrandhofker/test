

function createSockets(topic,title) {
    document.getElementById(topic).innerText = topic;
    document.getElementById(topic+"-title").innerHTML = "<h3>"+topic.toUpperCase()+"</h3><p>"+title+"</p>";
    const messageBlock = document.getElementById(topic + '-messages');

    const socket = new WebSocket("ws://localhost:8092/myChatApp?" + topic);

    socket.onopen = function (event) {
        socket.send(topic +" is connected!");
    };

    socket.onclose = function(event) {
         messageBlock.innerHTML += topic+" closed connection<br />";
  
      };

    socket.onmessage = function (event) {
        messageBlock.innerHTML += event.data + "<br />";
    };
    const input = document.getElementById(topic + '-msg');
    input.addEventListener("keyup", (event) => {
        if (event.keyCode === 13) {
            event.preventDefault();
            send(input.value);
            input.value = '';
        }
    });

    function send(message) {
        if (socket.readyState == WebSocket.OPEN) {
            socket.send(message);
        } else {
            alert("The socket is not open.");
        }
        return false;
    }
}
createSockets("meta","only at opening the connection to get meta data (json schema) only server->client");
createSockets("control","to control the connection, query commands. update commands, close commands, and control related messages etc.");
createSockets("data","the data from one or more Apache Kafka topics in the form of json data in key/value pairs");