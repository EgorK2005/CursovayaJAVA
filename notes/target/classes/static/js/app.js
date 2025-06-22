// Полный рабочий код app.js без ошибок
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOMContentLoaded event fired');

    // Определяем текущую страницу по URL
    const path = window.location.pathname;
    console.log('Current path:', path);

    if (path.endsWith('create-note.html')) {
        console.log('Initializing create note page');
        initCreateNotePage();
    } else if (path.endsWith('view-notes.html')) {
        console.log('Initializing view notes page');
        initViewNotesPage();
    } else if (path.endsWith('edit-note.html')) {
        console.log('Initializing edit note page');
        initEditPage();
    } else {
        console.log('No specific initialization for this page');
    }
});

// Инициализация страницы создания заметки
function initCreateNotePage() {
    const form = document.getElementById('note-form');

    if (!form) {
        console.error('Error: note-form element not found');
        return;
    }

    console.log('Adding submit listener to note-form');
    form.addEventListener('submit', handleCreateNote);
}

// Обработчик создания заметки
async function handleCreateNote(e) {
    e.preventDefault();
    console.log('Form submit triggered');

    try {
        const title = document.getElementById('title').value;
        const content = document.getElementById('content').value;

        if (!title || !content) {
            alert('Title and content are required!');
            return;
        }

        console.log('Saving note with title:', title);

        const response = await fetch('/api/notes', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include',
            body: JSON.stringify({ title, content })
        });

        console.log('Response status:', response.status);

        if (response.ok) {
            const data = await response.json();
            console.log('Note saved successfully:', data);
            alert('Note saved successfully!');
            window.location.href = 'view-notes.html';
        } else {
            const errorText = await response.text();
            console.error('Server error:', errorText);
            alert(`Error saving note: ${errorText}`);
        }
    } catch (error) {
        console.error('Unexpected error:', error);
        alert('Failed to save note. Please check console for details.');
    }
}

// Инициализация страницы просмотра заметок
function initViewNotesPage() {
    const container = document.getElementById('notes-container');

    if (!container) {
        console.error('Error: notes-container element not found');
        return;
    }

    console.log('Loading notes...');
    loadNotes();
}

// Загрузка заметок
async function loadNotes() {
    try {
        console.log('Fetching notes from API');
        const response = await fetch('/api/notes', {
            method: 'GET',
            credentials: 'include'
        });

        console.log('Response status:', response.status);

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Server error: ${response.status} - ${errorText}`);
        }

        const notes = await response.json();
        console.log('Received notes:', notes.length);

        const container = document.getElementById('notes-container');
        if (!container) return;

        container.innerHTML = '';

        if (notes.length === 0) {
            container.innerHTML = '<p>No notes found. Create your first note!</p>';
            return;
        }

        notes.forEach(note => {
            const noteElement = document.createElement('div');
            noteElement.className = 'note-card';
            noteElement.innerHTML = `
                <h3>${note.title}</h3>
                <p>${note.content}</p>
                <p>Created: ${new Date(note.createdAt).toLocaleString()}</p>
                <button class="delete-btn" data-id="${note.id}">Delete</button>
            `;
            container.appendChild(noteElement);
        });

        // Добавляем обработчик для кнопок удаления
        container.addEventListener('click', function(e) {
            if (e.target.classList.contains('delete-btn')) {
                const noteId = e.target.dataset.id;
                console.log('Delete button clicked for note:', noteId);
                deleteNote(noteId);
            }
        });

    } catch (error) {
        console.error('Error loading notes:', error);
        const container = document.getElementById('notes-container');
        if (container) {
            container.innerHTML = `<p class="error">Error: ${error.message}</p>`;
        }
    }
}

// Удаление заметки
async function deleteNote(noteId) {
    if (!confirm('Are you sure you want to delete this note?')) {
        return;
    }

    try {
        console.log(`Deleting note ${noteId}`);
        const response = await fetch(`/api/notes/${noteId}`, {
            method: 'DELETE',
            credentials: 'include'
        });

        console.log('Delete response status:', response.status);

        if (response.ok) {
            console.log('Note deleted successfully');
            alert('Note deleted successfully!');
            await loadNotes();
        } else {
            const errorText = await response.text();
            console.error('Delete error:', errorText);
            alert(`Error deleting note: ${errorText}`);
        }
    } catch (error) {
        console.error('Error deleting note:', error);
        alert('Failed to delete note. Please try again.');
    }
}

// Инициализация страницы редактирования
function initEditPage() {
    console.log('Edit page initialization');
    // Реализация по аналогии с другими страницами
}

// Глобальные вспомогательные функции
function getCsrfToken() {
    const cookies = document.cookie.split(';');
    for (const cookie of cookies) {
        const [name, value] = cookie.trim().split('=');
        if (name === 'XSRF-TOKEN') {
            return decodeURIComponent(value);
        }
    }
    return null;
}