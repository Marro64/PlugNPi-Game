function handleButtonClick(button) {
    // Remove the 'active' class from all buttons
    const buttons = document.querySelectorAll('.leaderboard-by-sorter');
    buttons.forEach((btn) => {
        btn.classList.remove('active');
    });

    // Add the 'active' class to the clicked button
    button.classList.add('active');
}

function callActiveButton() {
    const activeButton = document.querySelector('.leaderboard-by-sorter.active');
    if (activeButton) {
        let activeData =  activeButton.getAttribute('data-by');
        console.log(activeData)
        if(activeData === "All Users") {
            fetchUsersAndPopulate();
        } else if (activeData === "Players Only") {
            fetchUsersAndPopulate('PLAYER');
        } else if (activeData === "Admins Only") {
            fetchUsersAndPopulate('ADMIN');
        }
    }
}

