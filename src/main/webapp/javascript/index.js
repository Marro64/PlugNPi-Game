/* TODO: Implement Helper method for retrieving both json response and status code */
let BASE_URL = null
function getLinkBase() {
    let useIp = false;
    if (useIp) {
        // Use a web service to get the client's IP address
        $.get("https://jsonip.com/", function(response) { //TODO find a nice way to instantly get the IP
            //const clientIp = response.ip;
            const clientIp = '192.168.2.5'
            BASE_URL = `http://${clientIp}:8080/plugnpi/api`; // Set BASE_URL using the retrieved IP address
            console.log(BASE_URL); // Log BASE_URL after it's set
            // Place any code that depends on BASE_URL here
        }, "json");
    } else {
        BASE_URL = "http://localhost:8080/plugnpi/api";
    }
}

getLinkBase()
// You can call the getLinkBase function to retrieve the IP address and set BASE_URL.

// After calling getLinkBase, you can use BASE_URL within your code.



/*
Have to send a UserLogin object with a String username and String password so {"username":"xxx","password":"xxx"}
Handle an unauthorized response for invalid logins
Handle a NOT FOUND response for users that are not found
 */

const APILoginCall = async (data) => {
    console.log("login call");
    const response = await fetch(`/plugnpi/api/session/login`, {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json",
        }
    });

    if (response.status === 401) {
        console.log("invalid creds")
        const errorMessage = "Invalid credentials.";
        return {success: false, message: errorMessage};
    } else if (response.status === 404) {
        console.log("no user exists")
        const errorMessage = "No such user exists.";
        return {success: false, message: errorMessage};
    } else if (response.status === 200){
        console.log("valid login")
        return {success: true, message: await response.text()};
    }
}


/*
Have to send a UserSignup object with a String username and String password and String email so {"username":"xxx","email":"xxx","password":"xxx"}
Handle not acceptable if the username/email already exists
 */
const APIRegisterCall = async (data) => {
    const response = await fetch(`/plugnpi/api/users`, {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json",
        }
    });

    if (response.status === 400) {
        const errorMessage = "That Username or Email already exists.";
        return {success: false, message: errorMessage};
    } else if (response.status === 200){
        return {success: true, message: await response.text()};
    }
}

/*
Has to handle a cookie that it got, just do:
response => {
ok = response.ok
return response.text()
}
If you get an OK you send the user back to the login page
If you somehow manage to logout without being logged in, you get an unauthorized (handle that somehow)
 */
const APILogoutCall = async () => {
    const response = await fetch(`/plugnpi/api/session`, {
        method: "GET",
    });

    if (response.status === 200) {
        window.location.href = "..";
    }
}

const APIConnectToPi = async (session) => {
    const response = await fetch(`/plugnpi/api/pi/link?session=${session}&connect=true`, {
        method: "GET",
    });

    if (response.status === 200) {
        const message = "Successfully connected to a Pi."
        return {success: true, message: message};
    }else if(response.status === 204) {
        const errorMessage = "No such session exists.";
        return {success: true, message: errorMessage};
    }
}

const APIGetLeaderboardCall = async (date) => {
    const request = await fetch(`/plugnpi/api/leaderboard?date=${date}`);

    return request.json();
}




