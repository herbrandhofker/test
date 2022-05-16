
const serverHost="localhost";
const serverPort=8081;
const metaDiv = document.getElementById("meta");
const controlDiv = document.getElementById("control");
const dataDiv = document.getElementById("data");
const MAX_ROWS = 20;

function createSocket(parent, topic, titleKey, titleValue, registerRecord, showDetails, schema) {
    const topicDiv = document.createElement("div");
    parent.appendChild(topicDiv);
    topicDiv.id = topic;
    topicDiv.className = "topic";

    const topicTable = document.createElement("table");
    topicDiv.appendChild(topicTable);
    const thead = document.createElement("thead");
    topicTable.appendChild(thead);
    let tr = thead.insertRow();
    const thTitle = document.createElement("th");
    thTitle.colSpan = 2;
    thTitle.innerText = topic;
    tr.appendChild(thTitle);

    tr = thead.insertRow();
    if (schema != null) {
        let colSpan = 0;
        for (let prop in schema.properties) {
            colSpan++;
            const th = document.createElement("th");
            tr.appendChild(th);
            th.innerText = prop;
        }
        thTitle.colSpan = colSpan;

    } else {
        const thKey = document.createElement("th");
        tr.appendChild(thKey);
        thKey.innerText = titleKey;
        const thValue = document.createElement("th");
        thValue.innerText = titleValue;
        tr.appendChild(thValue);
    }
    const tbody = document.createElement("tbody");
    topicTable.appendChild(tbody);

    const socket = new WebSocket("ws://"+serverHost+":"+serverPort+"/myChatApp?" + topic);

    socket.onopen = function (event) {
        const msg = topic + " is connected";
        socket.send('{ "key": "info", "value": "' + msg + '" }');
    };

    socket.onclose = function (event) {
        tbody.appendChild(addRecord("info", topic + " connection is closed"));
    };

    socket.onmessage = function (event) {
        try {
            const lst = JSON.parse(event.data);
            if (Array.isArray(lst)) {
                for (let i = 0; i < lst.length; i++) {
                    const key = lst[i].key;
                    const value = lst[i].value;
                    addRecord(tbody, key, value);
                }
            }
            else {
                const key = lst.key;
                let value = lst.value;
                addRecord(tbody, key, value, schema);
            }
        }
        catch (e) {
            console.log(e);
        }
    };

    return socket;

    function addRecord(tbody, key, value, schema) {
        while (tbody.children.length > MAX_ROWS && tbody.lastChild) {
            tbody.lastChild.remove();
        }

        const id = topic + "_" + key;
        let tr = document.getElementById(id);

        if (tr != null) {//force update
            while (tr.firstChild) {
                tr.firstChild.remove()
            }
        }
        else {
            tr = tbody.insertRow(-1);
            tr.id = id;
            tr.addEventListener('dblclick', (e) => showDetails(key, value, topic, schema));
        }
        if (registerRecord != null) registerRecord(key, value);

        if (schema != null) {
            if (typeof value !== 'object') {
                value = JSON.parse(value);
            }
            for (let prop in schema.properties) {
                const td = tr.insertCell();
                let txt = value[prop];
                if (typeof txt === 'object') {
                    txt = JSON.stringify(txt);
                }
                td.innerText = txt;
            }
        }
        else {
            if (typeof value === 'object') {
                value = JSON.stringify(value);
            }
            let td = tr.insertCell();
            td.innerText = key;
            td.className = "key";
            td = tr.insertCell();
            td.innerText = value;
            td.className = "value";
        }
    }
}

function registerSchema(topic, schema) {
    createSocket(dataDiv, topic, "key", "value", null, (key, data, topic, schema) => recordDetails(key, data, topic, schema), schema);
    schemaDetails(topic, schema, false);
}

function schemaDetails(topic, schema, show) {
    const data = document.getElementById("data");
    for (let i = 0; i < data.children.length; i++) {
        data.children[i].style.display = "none";
    }

    const el = document.getElementById(topic);
    if (show)
        el.style.display = "block";
    else
        el.style.display = "none";
}

function recordDetails(key, data, topic, schema) {
    const detail_id = topic + "_detail";
    const tr = document.getElementById(topic + "_" + key);
    const t_body = tr.parentElement;

    const weg = document.getElementById(detail_id);
    if (weg != null){
        t_body.deleteRow(weg.rowIndex-2);
    }   
    const detail_tr = t_body.insertRow(tr.rowIndex - 1);
    detail_tr.id = detail_id;
    const detail_td = detail_tr.insertCell(0);
    detail_td.colSpan = tr.children.length;
    detail_td.appendChild(createJsonEditor(schema,data));    
}

function createJsonEditor(schema, data){
    const div = document.createElement("div");
    div.innerText=JSON.stringify(data);
    return div;  
}

createSocket(metaDiv, "meta", "Name", "Schema", (key, value) => registerSchema(key, value), (schemaName, schema, dummy, dummy2) => schemaDetails(schemaName, schema, true), null);
const controlSocket = createSocket(controlDiv, "control", "Type", "Message", (k, v) => { console.log(v); }, null, null);

const label = document.createElement("label");
controlDiv.appendChild(label);
label.innerText = "Command";
label.for = "Command";

const input = document.createElement("input");
controlDiv.appendChild(input);
input.name = "Command";
input.addEventListener("keyup", (event) => {
    if (event.keyCode === 13) {
        event.preventDefault();
        sendCommand(controlSocket, input.value);
        input.value = '';
    }
});

function sendCommand(socket, cmd) {

    if (socket.readyState == WebSocket.OPEN) {
        const json = { "key": "cmd", "value": cmd }
        socket.send(JSON.stringify(json));

    } else {
        alert("The socket is not open.");
    }
    return false;
}