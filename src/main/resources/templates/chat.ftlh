<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Чат с ${friendUsername}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }

        .chat-container {
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 80%;
            max-width: 800px;
            overflow: hidden;
        }

        .chat-header {
            background-color: #007bff;
            color: white;
            padding: 20px;
            text-align: center;
            font-size: 24px;
            position: relative;
        }

        .back-button {
            position: absolute;
            top: 0;
            left: 0;
            padding: 10px;
            color: white;
            text-decoration: none;
        }

        .message-container {
            padding: 20px;
            height: 400px;
            overflow-y: auto;
            display: flex;
            flex-direction: column;
        }

        .message {
            margin-bottom: 10px;
            padding: 10px;
            border-radius: 8px;
            max-width: 70%;
            word-wrap: break-word;
        }

        .message.sent {
            background-color: #dcf8c6;
            align-self: flex-end;
            text-align: right;
        }

        .message.received {
            background-color: #f0f0f0;
            align-self: flex-start;
            text-align: left;
        }

        .chat-form {
            padding: 20px;
            background-color: #f9f9f9;
            border-top: 1px solid #eee;
            display: flex;
        }

        .chat-form input {
            flex: 1;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            margin-right: 10px;
        }

        .chat-form button {
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .chat-form button:hover {
            background-color: #0056b3;
        }

        .message-container::-webkit-scrollbar {
            width: 8px;
        }

        .message-container::-webkit-scrollbar-track {
            background: #f1f1f1;
        }

        .message-container::-webkit-scrollbar-thumb {
            background: #888;
            border-radius: 4px;
        }

        .message-container::-webkit-scrollbar-thumb:hover {
            background: #555;
        }
    </style>
</head>
<body>
<div class="chat-container">
    <div class="chat-header">
        <a href="#" onclick="history.back(); return false;" class="back-button"><-</a>
        <#if friendUsername == username>
            Избранное
        <#else>
            Чат с ${friendUsername}
        </#if>
    </div>

    <div class="message-container" id="message-container">
        <#list history as message>
            <#if message??>
            <div class="message <#if message.from == username>sent<#else>received</#if>">
                <b><#if message.from == username>Вы:<#else>${message.from}:</#if></b> ${message.message}
            </div>
            </#if>
        </#list>
    </div>

    <form class="chat-form" onsubmit="sendMessage(event)">
        <input type="text" id="message-input" placeholder="Введите сообщение..." required>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Отправить</button>
    </form>

    <input type="hidden" id="my-username" value="${username}">
    <input type="hidden" id="friend-username" value="${friendUsername}">

</div>

<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.5.1/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
<script>
    let stompClient = null;
    let username = null;
    let friendUsername = null;

    function connectToChat() {
        console.log("погнали");
        username = document.getElementById('my-username').value;
        friendUsername = document.getElementById('friend-username').value;
        console.log(username);
        console.log(friendUsername);

        if (!username || !friendUsername) {
            alert("Username or friend's username is missing!");
            return;
        }

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);

    }

    function onConnected() {
        stompClient.subscribe('/user/' + '${username}' + '/queue/messages', onMessageReceived);
        // <#--stompClient.subscribe('/user/' + '${friendUsername}' + '/queue/messages', onMessageReceived);-->
        console.log("Subscribing to:", '/user/' + username + '/queue/messages');
        console.log("Subscribing to:", '/user/' + friendUsername + '/queue/messages');
        document.getElementById('message-container').scrollTop = document.getElementById('message-container').scrollHeight;
        console.log("подписались");
    }

    function onError(error) {
        console.error('Could not connect to WebSocket server. Please refresh this page to try again!');
    }

    function sendMessage(event) {
        const messageInput = document.getElementById('message-input');
        const messageText = messageInput.value.trim();

        if (messageText && stompClient) {
            const chatMessage = {
                from: username,
                to: friendUsername,
                message: messageText
            };

            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            // <#--stompClient.send('/user/' + '${friendUsername}' + '/queue/messages', {}, JSON.stringify(chatMessage))-->
            // <#--stompClient.send('/user/' + '${username}' + '/queue/messages', {}, JSON.stringify(chatMessage))-->
            messageInput.value = '';
            // displayMessage(chatMessage);
        }
        event.preventDefault();
    }

    function onMessageReceived(payload) {
        console.log(payload.toString())
        const message = JSON.parse(payload.body);
        displayMessage(message);
    }

    function displayMessage(message) {
        const messageContainer = document.getElementById('message-container');
        const messageElement = document.createElement('div');
        messageElement.classList.add('message');

        let messageContent = message && message.message ? message.message : '';

        if (message && message.from === username) {
            messageElement.classList.add('sent');
            messageElement.innerHTML = `<b>Вы:</b> ` + messageContent;
        } else if (message) {
            messageElement.classList.add('received');
            messageElement.innerHTML = `<b>` + message.from + ":</b> " + message.message;
        } else {
            console.warn("Received a null message, ignoring.");
            return;
        }
        console.log("чекаем отправку " + message);
        messageContainer.appendChild(messageElement);

        messageContainer.scrollTop = messageContainer.scrollHeight;
    }

    document.addEventListener('DOMContentLoaded', function() {
        connectToChat();
    });
</script>
</body>
</html>