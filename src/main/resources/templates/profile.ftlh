<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Профиль</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f4f4f4;
        }

        .profile-container {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            max-width: 600px;
            margin: 0 auto;
        }

        .profile-header {
            text-align: center;
            margin-bottom: 20px;
        }

        .profile-picture {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #ddd;
        }

        .profile-details {
            margin-bottom: 20px;
        }

        .profile-details p {
            margin: 5px 0;
        }

        .friend-list {
            margin-bottom: 20px;
        }

        .friend-list h3 {
            margin-bottom: 10px;
        }

        .friend-list ul {
            list-style: none;
            padding: 0;
        }

        .friend-list li {
            padding: 5px 0;
            border-bottom: 1px solid #eee;
        }

        .upload-form {
            margin-bottom: 20px;
        }

        .logout-form {
            text-align: right;
        }

        input[type="submit"] {
            background-color: #5cb85c;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #449d44;
        }

        a {
            color: #337ab7;
            text-decoration: none;
        }

        a:hover {
            text-decoration: underline;
        }

        .hidden {
            display: none;
        }

        .friend-search-link {
            margin-bottom: 10px;
            display: block;
        }

        .friend-item {
            list-style: none;
        }

        .friend-item label {
            display: flex;
            align-items: center;
            gap: 5px;
        }

        .profile-picture-mini {
            width: 20px;
            height: 20px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #ddd;
            vertical-align: middle
        }

        .delete-friend-button {
            background-color: #f44336;
            color: white;
            padding: 5px 10px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 12px;
            border: none;
            cursor: pointer;
            border-radius: 4px;
        }

        .delete-friend-button:hover {
            background-color: #da190b;
        }
    </style>
</head>
<body>
<div class="profile-container">
    <div class="profile-header">
        <#if photourl??>
            <img src="/images/download/${photourl}" alt="Фото профиля" class="profile-picture">
        <#else>
            <img src="/def.jpg" alt="Фото профиля" class="profile-picture">
        </#if>

        <h2>${username}</h2>
    </div>

    <div class="profile-details">
        <p><strong>Имя:</strong> ${firstname}</p>
        <p><strong>Фамилия:</strong> ${lastname}</p>
        <p><strong>Email:</strong> ${email}</p>
        <p><strong>Дата рождения:</strong> ${birthday}</p>
    </div>

    <div class="friend-search-link">
        <a href="/friend_search">Поиск друзей</a>
    </div>

    <div class="friend-list">
        <h3>Друзья:</h3>
        <#if friends??>
            <#list friends as friend, url>
                <li class="friend-item">
                    <label>
                        <a href="/friend/chat/${friend}">
                            <#if url??>
                                <img src="/images/download/${url}" alt="Фото профиля" class="profile-picture-mini">
                            <#else>
                                <img src="/def.jpg" alt="Фото профиля" class="profile-picture-mini">
                            </#if>
                            ${friend}
                        </a>
                    </label>
                    <form action="/profile/delete_friend" method="post" style="display: inline;">
                        <input type="hidden" name="friend" value="${friend}">
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <button type="submit" class="delete-friend-button">Удалить из друзей</button>
                    </form>
                </li>
            </#list>
        <#else>
            <p>У вас пока нет друзей.</p>
        </#if>
    </div>


    <form class="upload-form" action="/images/upload" method="post" enctype="multipart/form-data">
        <h3>Фото</h3>
        <label for="file">Обновить фото профиля:</label>
        <input type="file" id="file" name="file" accept="image/*"><br><br>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Загрузить</button>
    </form>

    <div class="invitations-link">
        <a href="/profile/invitations">Запросы в друзья</a>
    </div>


    <form class="logout-form" action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <input type="submit" value="Выйти">
    </form>
    <a href="#" onclick="history.back(); return false;" class="back-button">Назад</a>
</div>
</body>
</html>
