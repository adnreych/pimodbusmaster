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

	await axios.post('/auth/login', qs.stringify(requestBody), config)
	  .then((result) => {
		  console.log("result: ", result);
		  localStorage.setItem('user', JSON.stringify(requestBody));
	  })
}


async function logout() {
	
	var user = localStorage.getItem('user');
	
	await axios.post('/auth/logout', user)
	  .then((result) => {
		  console.log("result: ", result);
		  localStorage.removeItem('user');
	  })
	  .catch((err) => {
				  console.log("ERROR: ", err);
			  });
}

/*function getAll() {
    const requestOptions = {
        method: 'GET',
        headers: authHeader()
    };

    return fetch(`${config.apiUrl}/users`, requestOptions).then(handleResponse);
} */