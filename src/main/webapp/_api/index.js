/* TODO: Implement Helper method for retrieving both json response and status code */

const BASE_URL = "http://localhost:8080/plugnpi";

/* Request and response interceptor */
// const { fetch: originalFetch } = window;
// window.fetch = async (...args) => {
//     const [resource, options = {}] = args;
//
//     if (!resource.includes(BASE_URL)) return await originalFetch(resource, options);
//
//     const token = localStorage.getItem("access-token");
//
//     /* Adding access token to request headers */
//     if (token !== null && token !== undefined) {
//         options.headers = options.headers || {};
//         options.headers["Authorization"] = `Bearer ${token}`;
//     }
//
//     const response = await originalFetch(resource, options);
//
//     /* Unauthorized, try to refresh the access token */
//     if (response.status === 401 && localStorage.getItem("access-token")) {
//         const refreshResponse = await originalFetch(`${BASE_URL}/auth/refresh`,
//             { method: "POST", credentials: "include" }
//         );
//
//         const refreshToken = await refreshResponse.json();
//
//         /* Update the access token  */
//         if (refreshToken?.value) {
//             localStorage.setItem("access-token", refreshToken.value);
//
//             /* Re-fetch the resource again */
//             return await fetch(resource, options);
//         }
//     }
//
//     /* TODO: Intercept response to see if access token has expired */
//     return response;
// }

/*
Have to send a UserLogin object with a String username and String password so {"username":"xxx","password":"xxx"}
Handle an unauthorized response for invalid logins
Handle a NOT FOUND response for users that are not found
 */
const APILoginCall = async (data) => {
    const response = await fetch(`${BASE_URL}/pages/home/login`, {
        method: "POST",
        body: JSON.stringify(data),
        headers: {
            "Content-Type": "application/json",
        }
    });

    return response.json();
}

/*
Have to send a UserSignup object with a String username and String password and String email so {"username":"xxx","email":"xxx","password":"xxx"}
Handle not acceptable if the username/email already exists
 */
const APIRegisterCall = async (data) => {
    const request = await fetch(`${BASE_URL}/pages/home/signup`, {
        method: "POST",
        body: JSON.stringify(data),
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
        },
    }).then(
        request => {
            if (request.ok) {
                return request.json();
            }
        }
    ).catch(e => console.error(e));

    return request.json();
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
    const response = await fetch(`${BASE_URL}/session/logout`, {
        method: "GET",
        credentials: "include",
    });

    if (response.status === 200) {
        localStorage.clear("access-token");
        window.location.href = "/";
    }
}

const APIGetLeaderboardCall = async (by) => {
    const request = await fetch(`${BASE_URL}/leaderboard?by=${by}`);

    return request.json();
}




