<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Административная панель</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 80%;
            margin: 20px auto;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 30px;
        }

        h1 {
            text-align: center;
            color: #333;
        }

        .query-form {
            margin-bottom: 20px;
        }

        .query-form label {
            display: block;
            margin-bottom: 5px;
            color: #555;
        }

        .query-form textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
            font-size: 16px;
            resize: vertical;
        }

        .query-form button {
            background-color: #28a745;
            color: #fff;
            padding: 12px 25px;
            border: none;
            border-radius: 5px;
            font-size: 1.1em;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .query-form button:hover {
            background-color: #218838;
        }

        .status-message {
            margin-top: 20px;
            padding: 15px;
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
            border-radius: 5px;
        }

        .error-message {
            margin-top: 20px;
            padding: 15px;
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
            border-radius: 5px;
        }

        a {
            color: #007bff;
            text-decoration: none;
            display: inline-block;
            margin-top: 15px;
        }

        a:hover {
            text-decoration: underline;
        }

        .back-link {
            margin-top: 20px;
            text-align: center;
        }

        textarea:focus,
        button:focus {
            outline: none;
            box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
        }
    </style>
</head>
<body>

<div class="container">
    <h1>Административная панель</h1>

    <a href="/">На главную</a>

    <form id="queryForm" class="query-form" action="admin" method="post">
        <label for="Query">Запрос:</label>
        <textarea id="Query" name="Query" rows="6" cols="50"></textarea>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Выполнить</button>
    </form>

    <#if queryStatus??>
        <p class="status-message">${queryStatus}</p>
    </#if>
</div>

</body>
</html>s