import axios from "axios";
import qs from "querystring";

export const userService = {
    login,
    logout,
   // getAll
};

async function login(username, password) {

	const requestBody = {
	  username: username,
	  password: password
	}

	const config = {
	  headers: {
	    'Content-Type': 'application/x-www-form-urlencoded'
	  }
	}

	await axios.post('/login_process', qs.stringify(requestBody), config)
	  .then((result) => {
		  console.log("result: ", result);
		  localStorage.setItem('user', JSON.stringify(requestBody));
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