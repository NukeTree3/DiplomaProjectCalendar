<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Поиск друзей</title>
    <style>
        body {
            font-family: sans-serif;
            margin: 20px;
        }

        #search-container {
            margin-bottom: 20px;
        }

        #search_box {
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
            width: 200px;
        }

        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            padding: 8px 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .user-list {
            list-style: none;
            padding: 0;
        }

        .user-list li {
            border: 1px solid #ddd;
            padding: 10px;
            margin-bottom: 5px;
            border-radius: 4px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .add-friend-button {
            background-color: #008CBA;
            color: white;
            padding: 6px 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
        }

        .add-friend-button:hover {
            background-color: #0077A3;
        }

        #search_advice_wrapper {
            color: #777;
            font-size: 0.9em;
            margin-top: 5px;
        }

        .friend-status {
            font-style: italic;
            color: green;
        }

        .offer-sent {
            font-style: italic;
            color: orange;
        }

        .chat-header {
            background-color: white;
            color: black;
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
            color: black;
            text-decoration: none;
        }
    </style>
</head>
<body>
<div class="chat-header">
    <a href="#" onclick="history.back(); return false;" class="back-button">назад</a>
    <h1>Поиск друзей</h1>
</div>


<div id="search-container">
    <form action="/friend_search" method="GET">
        <input type="text" name="query" id="search_box" value="" autocomplete="off" placeholder="Поиск">
        <input type="submit" value="Поиск">
    </form>
    <div id="search_advice_wrapper">
    </div>
</div>

<#if users?? && users?has_content>
        <h2>Результаты поиска:</h2>
    <ul class="user-list">
        <#list users?keys as user>
            <li>
                <span>${user}</span>
                <#assign status = users[user]>
                <#if status == "friend">
                    <span class="friend-status">Друг</span>
                <#elseif status == "offer">
                    <span class="offer-sent">Приглашение отправлено</span>
                <#elseif status == "invite">
                    <span class="offer-sent">Пользователь хочет дружить</span>
                <#else>
                    <form action="/friend_search/add_friend" method="post">
                        <input type="hidden" name="username" value="${user}">
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <button type="submit" class="add-friend-button">Добавить в друзья</button>
                    </form>
                </#if>
            </li>
        </#list>
    </ul>
<#else>
    <p>По вашему запросу ничего не найдено.</p>
</#if>
</body>
</html>

