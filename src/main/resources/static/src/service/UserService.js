import axios from "axios";

export const userService = {
    login,
    logout,
   // getAll
};

async function login(username, password) {
 /*   const username = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
    };
    console.log("requestOptions: ", requestOptions);
    const response = await fetch(`/login_process`, requestOptions); 
    */
   /* const user = await handleResponse(response);
    // login successful if there's a user in the response
    if (user) {
        // store user details and basic auth credentials in local storage 
        // to keep user logged in between page refreshes
        user.authdata = window.btoa(username + ':' + password);
        localStorage.setItem('user', JSON.stringify(user));
    } 
    */
    
    axios({
        method:'post',
        url:'/login_process',
        params:{
               username: username,
               password: password
           },
     //   config: { headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
           config: { headers: {'Content-Type': 'application/json'}}
       })
       .then(
           //authentication success...
       )
       .catch(error=>{
           var errResp = error.response;
           if(errResp.status === 401){
              //Ex: show login page again...
           }

       })
}


function logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('user');
}

/*function getAll() {
    const requestOptions = {
        method: 'GET',
        headers: authHeader()
    };

    return fetch(`${config.apiUrl}/users`, requestOptions).then(handleResponse);
} */

function handleResponse(response) {
    return response.text().then(text => {
    	console.log("response: ", text);
        const data = text && JSON.parse(text);
        if (!response.ok) {
            if (response.status === 401) {
                // auto logout if 401 response returned from api
                logout();
                window.location.reload(true);
            }

            const error = (data && data.message) || response.statusText;
            return Promise.reject(error);
        }

        return data;
    });
}