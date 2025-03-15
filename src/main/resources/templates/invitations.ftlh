<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Friend Requests</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        h1 {
            text-align: center;
            color: #333;
        }

        .invitation-list {
            list-style: none;
            padding: 0;
        }

        .invitation-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #f9f9f9;
        }

        .invitation-username {
            font-weight: bold;
            color: #333;
        }

        .invitation-actions {
            display: flex;
            gap: 10px;
        }

        .accept-button,
        .reject-button {
            background-color: #4CAF50;
            border: none;
            color: white;
            padding: 8px 16px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 14px;
            cursor: pointer;
            border-radius: 5px;
        }

        .reject-button {
            background-color: #f44336;
        }

        .no-invitations {
            text-align: center;
            color: #777;
        }

        .profile-picture-mini {
            width: 20px;
            height: 20px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #ddd;
            vertical-align: middle
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
<div class="container">
    <div class="chat-header">
        <a href="#" onclick="history.back(); return false;" class="back-button">назад</a>
        <h1>Запросы в друзья</h1>
    </div>

    <#if invitations??>
        <ul class="invitation-list">
            <#list invitations as invitation, url>
                <#if invitation!="">
                    <li class="invitation-item">
                        <span class="invitation-username">
                            <#if url??>
                                <img src="/images/download/${url}" alt="Фото профиля" class="profile-picture-mini">
                            <#else>
                                <img src="/def.jpg" alt="Фото профиля" class="profile-picture-mini">
                            </#if>
                            ${invitation}
                        </span>
                        <div class="invitation-actions">
                            <form action="/profile/invitations/accept" method="post">
                                <input type="hidden" name="friendUserName" value="${invitation}">
                                <input type="hidden" name="_csrf" value="${_csrf.token}">
                                <button type="submit" class="accept-button">Accept</button>
                            </form>
                            <form action="/profile/invitations/reject" method="post">
                                <input type="hidden" name="friendUserName" value="${invitation}">
                                <input type="hidden" name="_csrf" value="${_csrf.token}">
                                <button type="submit" class="reject-button">Reject</button>
                            </form>
                        </div>
                    </li>
                <#else>
                    <p class="no-invitations">Нет запросов</p>
                </#if>
            </#list>
        </ul>
    <#else>
        <p class="no-invitations">Нет запросов</p>
    </#if>
</div>
</body>
</html>
