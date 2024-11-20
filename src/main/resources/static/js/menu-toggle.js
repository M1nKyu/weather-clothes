document.addEventListener('DOMContentLoaded', () => {
    const menuButton = document.querySelector('.menu-toggle-button');
    const menuContainer = document.querySelector('.menu-container');

    menuButton.addEventListener('click', () => {
        menuContainer.classList.toggle('active');
    });

    document.addEventListener('click', (event) => {
        if (!menuContainer.contains(event.target) && !menuButton.contains(event.target)) {
            menuContainer.classList.remove('active');
        }
    });
});
