<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Заметки на ${date}</title>
    <style>
        body {
            font-family: sans-serif;
            margin: 20px;
        }

        .note {
            border: 1px solid #ccc;
            padding: 10px;
            margin-bottom: 10px;
            border-radius: 5px;
        }

        .note-actions {
            margin-top: 5px;
        }

        .note-actions a, .note-actions button {
            margin-right: 5px;
            padding: 5px 10px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            text-decoration: none;
            color: white;
        }

        .note-actions a.edit-note, .note-actions button.edit-note {
            background-color: #4CAF50;
        }

        .note-actions a.delete, .note-actions button.delete {
            background-color: #f44336;
        }

        form {
            margin-top: 20px;
        }

        label {
            display: block;
            margin-bottom: 5px;
        }

        input[type="text"] {
            width: 300px;
            padding: 8px;
            border: 1px solid #2831d2;
            border-radius: 4px;
            box-sizing: border-box;
        }

        input[type="submit"] {
            background-color: #428bca;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .edit-form {
            display: none;
        }
    </style>
</head>
<body>
<header>
<a href="#" onclick="history.back(); return false;" class="back-button"><-</a>
<h1>Заметки на ${date}</h1>
</header>
<#if notes?? && notes?has_content>
<#list notes as note>
<div class="note" data-note-id="${note.id}">
    <#if note.author == user>
        я:
    <#else>
        ${note.author}:
    </#if>
    <p class="note-text">${note.text}</p>
    <form class="edit-form" action="/calendar/${date}/${note.id}/change_text" method="post">
        <div>
            <label for="text">Текст:</label>
            <input type="text" id="text" name="text" value="${note.text}" required>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Сохранить</button>
        <button type="button" class="cancel-edit">Отменить</button>
    </form>
    <div class="note-actions">
        <#if note.author == user>
            <button class="edit-note">Изменить</button>
            <form action="/calendar/${date}/${note.id}/delete_note" method="post" style="display:inline;">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <button type="submit" class="delete">Удалить</button>
            </form>
        </#if>
    </div>
</div>
</#list>
<#else>
    <p>Нет заметок на этот день.</p>
</#if>

<h2>Добавить заметку на ${date}</h2>
<form action="/calendar/${date.toString()}/add_note" method="post">
    <div>
        <label for="textNode">Текст:</label>
        <input type="text" id="textNode" name="textNode" required>
    </div>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <input type="submit" value="Добавить">
</form>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const notes = document.querySelectorAll('.note');

        notes.forEach(note => {
            const editButton = note.querySelector('.edit-note');
            const cancelButton = note.querySelector('.cancel-edit');
            const noteText = note.querySelector('.note-text');
            const editForm = note.querySelector('.edit-form');

            editButton.addEventListener('click', function() {
                noteText.style.display = 'none';
                editForm.style.display = 'block';
                editButton.style.display = 'none';
            });

            cancelButton.addEventListener('click', function(event) {
                event.preventDefault();
                noteText.style.display = 'block';
                editForm.style.display = 'none';
                editButton.style.display = 'inline-block';
            });
        });
    });
</script>
</body>
</html>
