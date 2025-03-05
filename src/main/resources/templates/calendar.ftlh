<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Calendar</title>
    <style>
        body {
            font-family: sans-serif;
            margin: 20px;
        }

        .calendar-header {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-bottom: 20px;
        }

        .calendar-header button {
            padding: 5px 10px;
            margin: 0 10px;
            cursor: pointer;
        }

        .calendar {
            display: grid;
            grid-template-columns: repeat(7, 1fr);
            grid-gap: 5px;
            width: 700px;
        }

        .calendar a {
            display: block;
            padding: 10px;
            text-align: center;
            border: 1px solid #ccc;
            text-decoration: none;
            color: #333;
        }

        .calendar a:hover {
            background-color: #f0f0f0;
        }

        .calendar .note {
            margin-top: 5px;
            font-size: 0.8em;
            color: #777;
        }

        .calendar .note p {
            margin: 0;
        }

        h1 {
            text-align: center;
        }
    </style>
</head>
<body>
<h1>Calendar</h1>

<div class="calendar-header">
    <a href="/calendar/${(year - 1)?c}/${month?c}">&lt;&lt; Previous Year</a>

    <a href="/calendar/<#if month == 1>${(year - 1)?c}<#else>${year?c}</#if>/<#if month == 1>12<#else>${(month - 1)?c}</#if>">&lt; Previous Month</a>

    <h2>${month} ${year}</h2>

    <a href="/calendar/<#if month == 12>${(year + 1)?c}<#else>${year?c}</#if>/<#if month == 12>1<#else>${(month + 1)?c}</#if>">Next Month &gt;</a>

    <a href="/calendar/${(year + 1)?c}/${month?c}">Next Year &gt;&gt;</a>
</div>




<div class="calendar">
    <#list calendar as day>
        <a href="/calendar/${day.date.toString()}">
            ${day.date.dayOfMonth}

            <#if day.note??>
                <div class="note">
                    <#list day.note as note>
                        <p><b>${note.author}:</b> ${note.text}</p>
                    </#list>
                </div>
            </#if>
        </a>
    </#list>
</div>
</body>
</html>


