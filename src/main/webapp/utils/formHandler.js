const clearFormErrors = () => {
    Array.from(document.getElementsByClassName("form-error")).forEach(element => element.remove());
    Array.from(document.getElementsByClassName("input-error")).forEach(element => element.classList.remove("input-error", "input-bordered"));
}

const formErrorCount = () => {
    return document.getElementsByClassName("form-error").length;
}

/*
    @ error - string, text to be displayed of the error (optional).
    If nothing is provided, it's assumed that form element wasn't populated with input and default message is displayed.
    @ children - child dom node that has triggered this error
*/
const setFormError = (error, child) => {
    error = error ?? `${child.getAttribute("name")} can not be empty!`;

    /* Getting reference to the childrens parent wrapper div and appending error code */
    child.parentNode.appendChild(parseHTML(`<div class="form-error"><p class="text-xs text-red-500 mt-1 ml-1">${error}</p></div>`));

    /* Marking the input field as error causing */
    child.classList.add("input-error", "input-bordered");
}

const isValidUser = (user) =>{
    if(user.toString().length < 4){ // more parameters and check if the user exists
        return false;
    }

    return true;
}