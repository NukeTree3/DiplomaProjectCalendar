<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MVC</title>
</head>
<body>
<a href="/friend_search">Поиск</a>
${username}
${birthday}
${email}
${firstname}
${lastname}
<#if photourl??>
    ${photourl}
</#if>
<#if friends??>
    <#list friends as friend>
        ${friend}
    </#list>
</#if>
</body>
</html>