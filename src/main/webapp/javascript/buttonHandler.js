function handleButtonClick(button, buttonData) {
    // Remove the 'active' class from all buttons
    const buttons = document.querySelectorAll('.leaderboard-by-sorter');
    buttons.forEach((btn) => {
        btn.classList.remove('active');
    });

    // Add the 'active' class to the clicked button
    button.classList.add('active');
}


