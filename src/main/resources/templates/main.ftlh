<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>OurCalendar</title>
    <style>
        body {
            font-family: sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
        }

        .header {
            position: absolute;
            top: 0;
            right: 0;
            padding: 20px;
        }

        .header a {
            margin-left: 15px;
            text-decoration: none;
            color: #333;
            font-weight: bold;
        }

        .main-content {
            text-align: center;
        }

        .main-content h1 {
            font-size: 3em;
            margin-bottom: 10px;
            color: #333;
        }

        .main-content p {
            font-size: 1.2em;
            color: #666;
            margin-bottom: 30px;
        }

        .start-button {
            display: inline-block;
            padding: 15px 30px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-size: 1.2em;
            transition: background-color 0.3s ease;
        }

        .start-button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

<div class="header">
    <#if user??>
        <a href="/profile">Профиль</a>
    <#else>
        <a href="/login">Войти</a>
        <a href="/registration">Регистрация</a>
    </#if>
</div>

<div class="main-content">
    <h1>OurCalendar</h1>
    <p>для менеджмента и планирования</p>
    <a href="/calendar/${year?c}/${month?c}" class="start-button">Начать</a>
</div>

</body>
</html>

