document.addEventListener('DOMContentLoaded', () => {
    const notesContainer = document.getElementById('notes-container');
    const createBtn = document.getElementById('create-note-btn');


    async function loadNotes() {
        const response = await fetch('/api/notes');
        const notes = await response.json();

        notesContainer.innerHTML = '';
        notes.forEach(note => {
            const noteElement = document.createElement('div');
            noteElement.className = 'note';
            noteElement.innerHTML = `
                <h3>${note.title}</h3>
                <p>${note.content}</p>
                <small>${new Date(note.createdAt).toLocaleString()}</small>
                <button onclick="deleteNote(${note.id})">Удалить</button>
            `;
            notesContainer.appendChild(noteElement);
        });
    }


    window.deleteNote = async (id) => {
        await fetch(`/api/notes/${id}`, { method: 'DELETE' });
        loadNotes();
    };


    createBtn.addEventListener('click', () => {
        const title = prompt('Введите заголовок:');
        const content = prompt('Введите текст заметки:');

        if (title && content) {
            fetch('/api/notes', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ title, content })
            }).then(loadNotes);
        }
    });


    loadNotes();
});