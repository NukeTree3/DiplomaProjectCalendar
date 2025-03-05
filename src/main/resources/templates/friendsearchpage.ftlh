<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MVC</title>
</head>
<body>
    <div>
        <form action="/friend_search" method="GET">
            <input type="text" name="query" id="search_box" value="" autocomplete="off" placeholder="Поиск">
            <input type="submit" value="Поиск">
        </form>
        <div id="search_advice_wrapper"></div>
    </div>
    <#if users??>
        <#list users as user>
            ${user}
        </#list>
    </#if>
</body>
</html>