<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Календарь</title>
    <style>
        body {
            font-family: sans-serif;
            margin: 20px;
        }

        #main {
            display: flex;
            flex-wrap: wrap;
            transition: margin-left .5s;
            padding: 16px;
            margin-left: 0;
            justify-content: center;
        }

        h1 {
            text-align: center;
            width: 100%;
        }

        .calendar-friends-container {
            display: flex;
            width: 80%;
            max-width: 1200px;
            margin: 0 auto;
        }

        .calendar-container {
            width: 70%;
        }

        .calendar-header {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .calendar-header > div {
            display: flex;
            align-items: center;
        }

        .calendar-header a {
            text-decoration: none;
            color: #333;
            padding: 0 5px;
        }

        .calendar-header span {
            font-size: 1.2em;
        }

        .calendar-header h2 {
            margin-top: 0;
            margin-bottom: 0;
        }

        .calendar {
            display: grid;
            grid-template-columns: repeat(7, 1fr);
            grid-gap: 5px;
            width: 100%;
        }

        .day-header {
            text-align: center;
            font-weight: bold;
            padding: 5px;
            border-bottom: 1px solid #ccc;
        }

        .calendar a {
            display: flex;
            flex-direction: column;
            padding: 10px;
            text-align: center;
            border: 1px solid #ccc;
            text-decoration: none;
            color: #333;
            align-items: center;
        }

        .calendar a:hover {
            background-color: #f0f0f0;
        }

        .calendar .note {
            margin-top: 5px;
            font-size: 0.8em;
            color: #777;
            text-align: left;
            width: 100%;
        }

        .calendar .note p {
            margin: 0;
            text-align: left;
        }

        .friend-item {
            list-style: none;
        }

        .friend-item label {
            display: flex;
            align-items: center;
            gap: 5px;
        }

        .friends-list {
            width: 30%;
            border: 1px solid #ccc;
            padding: 10px;
            overflow-y: auto;
            max-height: 400px;
        }

        .friends-list a {
            display: block;
            margin-bottom: 5px;
            text-decoration: none;
            color: #333;
        }

        .sidebar {
            height: 100%;
            width: 250px;
            position: fixed;
            top: 0;
            left: -250px;
            background-color: #f8f9fa;
            overflow-x: hidden;
            transition: 0.5s;
            padding-top: 60px;
            z-index: 1;
            box-shadow: 2px 0px 5px rgba(0, 0, 0, 0.1);
        }

        .sidebar a {
            padding: 8px 8px 8px 32px;
            text-decoration: none;
            font-size: 18px;
            color: #495057;
            display: block;
            transition: 0.3s;
        }

        .sidebar a:hover {
            color: #007bff;
        }

        .sidebar .closebtn {
            position: absolute;
            top: 0;
            right: 25px;
            font-size: 36px;
            margin-left: 50px;
            cursor: pointer;
            color: #495057;
        }

        .openbtn {
            font-size: 20px;
            cursor: pointer;
            background-color: #fff;
            color: #495057;
            padding: 10px 15px;
            border: none;
            position: fixed;
            top: 10px;
            left: 10px;
            z-index: 2;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            border-radius: 5px;
        }

        .openbtn:hover {
            background-color: #e9ecef;
        }

        .profile-picture {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #ddd;
        }

        .profile-picture-mini {
            width: 20px;
            height: 20px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #ddd;
            vertical-align: middle
        }

        .profile-link {
            position: fixed;
            top: 10px;
            right: 10px;
            z-index: 3;
        }
    </style>
</head>
<body>

<div id="mySidebar" class="sidebar">
    <button id="submitButton" onclick="submitSelectedFriends()">Отправить выбранных друзей</button>
    <form id="friendsForm" action="/calendar/${year?c}/${month?c}" method="GET">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <#list friends as friend, url>
            <li class="friend-item">
                <label>
                    <input type="checkbox" class="friend-checkbox" data-friend="${friend}">
                    <#if friend == myUserName>
                        <#if url??>
                            <img src="/images/download/${url}" alt="Фото профиля" class="profile-picture-mini">
                        <#else>
                            <img src="/def.jpg" alt="Фото профиля" class="profile-picture-mini">
                        </#if>
                        я
                    <#else>
                        <#if url??>
                            <img src="/images/download/${url}" alt="Фото профиля" class="profile-picture-mini">
                        <#else>
                            <img src="/def.jpg" alt="Фото профиля" class="profile-picture-mini">
                        </#if>
                        ${friend}
                    </#if>
                </label>
            </li>
        </#list>
    </form>
</div>

<div id="main">
    <button class="openbtn" onclick="toggleNav()">☰</button>
    <a href="/profile" class="profile-link">
        <#if photourl??>
            <img src="/images/download/${photourl}" alt="Фото профиля" class="profile-picture">
        <#else>
            <img src="/def.jpg" alt="Фото профиля" class="profile-picture">
        </#if>
    </a>
    <h1>Calendar</h1>

    <div class="calendar-friends-container">
        <div class="calendar-container">
            <div class="calendar-header">
                <div>
                    <button onclick="yearMinus()">&lt;&lt;</button>
<#--                    <a href="/calendar/${(year - 1)?c}/${month?c}"> &lt;&lt; </a>-->
                    <h2>${year?c}</h2>
<#--                    <a href="/calendar/${(year + 1)?c}/${month?c}"> &gt;&gt; </a>-->
                    <button onclick="yearPlus()">&gt;&gt;</button>
                </div>
                <div>
                    <button onclick="monthMinus()">&lt;</button>
<#--                    <a href="/calendar/<#if month == 1>${(year - 1)?c}<#else>${year?c}</#if>/<#if month == 1>12<#else>${(month - 1)?c}</#if>">-->
<#--                        &lt; </a>-->
                    <h2>${monthString}</h2>
<#--                    <a href="/calendar/<#if month == 12>${(year + 1)?c}<#else>${year?c}</#if>/<#if month == 12>1<#else>${(month + 1)?c}</#if>">-->
<#--                        &gt; </a>-->
                    <button onclick="monthPlus()">&gt;</button>
                </div>
            </div>

            <div class="calendar">
                <div class="day-header">Пн</div>
                <div class="day-header">Вт</div>
                <div class="day-header">Ср</div>
                <div class="day-header">Чт</div>
                <div class="day-header">Пт</div>
                <div class="day-header">Сб</div>
                <div class="day-header">Вс</div>

                <#assign firstDayOfWeek = calendar[0].date.dayOfWeek.value>
                <#assign emptyCellsCount = (firstDayOfWeek - 1) % 7>

                <#list 1..emptyCellsCount as i>
                    <div></div>
                </#list>

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
        </div>

        <div class="friends-list">
            Чат:
            <#list friends as friend, url>
                <li class="friend-item">
                    <label>
                        <#if friend == myUserName>
                            <a href="/friend/chat/${friend}">
                                <#if url??>
                                    <img src="/images/download/${url}" alt="Фото профиля" class="profile-picture-mini">
                                <#else>
                                    <img src="/def.jpg" alt="Фото профиля" class="profile-picture-mini">
                                </#if>
                                Избранное
                            </a>
                        <#else>
                            <a href="/friend/chat/${friend}">
                                <#if url??>
                                    <img src="/images/download/${url}" alt="Фото профиля" class="profile-picture-mini">
                                <#else>
                                    <img src="/def.jpg" alt="Фото профиля" class="profile-picture-mini">
                                </#if>
                            ${friend}
                            </a>
                        </#if>
                    </label>
                </li>
            </#list>
        </div>
    </div>
</div>
<script>
    function yearMinus(){
        sessionStorage.setItem('formSubmitted', 'false');
        window.location.href = "/calendar/${(year - 1)?c}/${month?c}";
    }

    function yearPlus(){
        sessionStorage.setItem('formSubmitted', 'false');
        window.location.href = "/calendar/${(year + 1)?c}/${month?c}";
    }

    function monthMinus(){
        sessionStorage.setItem('formSubmitted', 'false');
        window.location.href = "/calendar/<#if month == 1>${(year - 1)?c}<#else>${year?c}</#if>/<#if month == 1>12<#else>${(month - 1)?c}</#if>";
    }

    function monthPlus(){
        sessionStorage.setItem('formSubmitted', 'false');
        window.location.href = "/calendar/<#if month == 12>${(year + 1)?c}<#else>${year?c}</#if>/<#if month == 12>1<#else>${(month + 1)?c}</#if>";
    }


    function toggleNav() {
        var sidebar = document.getElementById("mySidebar");
        var main = document.getElementById("main");

        if (sidebar.style.left === "0px") {
            sidebar.style.left = "-250px";
            main.style.marginLeft = "0";
        } else {
            sidebar.style.left = "0px";
            main.style.marginLeft = "250px";
        }
    }

    function submitSelectedFriends() {
        const checkboxes = document.querySelectorAll('.friend-checkbox:checked');
        const selectedFriends = Array.from(checkboxes).map(checkbox => checkbox.getAttribute('data-friend'));

        const form = document.getElementById('friendsForm');
        const existingInputs = form.querySelectorAll('input[name="checkFriends"]');
        existingInputs.forEach(input => input.remove());

        if (selectedFriends.length === 0) {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'checkFriends';
            input.value = '';
            form.appendChild(input);
        } else {
            selectedFriends.forEach(friend => {
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'checkFriends';
                input.value = friend;
                form.appendChild(input);
            });
        }
        form.submit();
    }

    function saveCheckboxState() {
        const checkboxes = document.querySelectorAll('.friend-checkbox');
        checkboxes.forEach(checkbox => {
            localStorage.setItem(checkbox.dataset.friend, checkbox.checked);
        });
    }

    function restoreCheckboxState() {
        const checkboxes = document.querySelectorAll('.friend-checkbox');
        checkboxes.forEach(checkbox => {
            const savedState = localStorage.getItem(checkbox.dataset.friend);
            if (savedState !== null) {
                checkbox.checked = savedState === 'true';
            } else {
                checkbox.checked = false;
            }
        });
    }

    function restoreCheckboxStateAndSubmitSelectedFriends(){
        restoreCheckboxState();
        if (sessionStorage.getItem('formSubmitted') !== 'true') {
            submitSelectedFriends();
            sessionStorage.setItem('formSubmitted', 'true');
        }

    }

    document.addEventListener('DOMContentLoaded', restoreCheckboxStateAndSubmitSelectedFriends);
    document.querySelectorAll('.friend-checkbox').forEach(checkbox => {
        checkbox.addEventListener('change', saveCheckboxState);
    });
</script>
</body>
</html>

