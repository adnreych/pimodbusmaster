import axios from 'axios'
import qs from "querystring";


class userService {
	
	login(username, password) {

		const requestBody = {
		  username: username,
		  password: password
		}

		const config = {
		  headers: {
		    'Content-Type': 'application/x-www-form-urlencoded'
		  }
		}

		return axios.post(`/auth/login`, qs.stringify(requestBody), config)
		  .then((result) => {
			  console.log("result: ", result);
			  localStorage.setItem('user', JSON.stringify(requestBody));
		  })
	}
	
	logout() {
		
		var user = localStorage.getItem('user');
		
		return axios.post(`/auth/logout`, user)
		  .then((result) => {
			  console.log("result: ", result);
			  localStorage.removeItem('user');
		  })
		  .catch((err) => {
					  console.log("ERROR: ", err);
				  });
	}

	
	getAll() {
	    return axios.get(`/api/allusers`);
	} 
	
	addUser(user) {
	    return axios.post(`/api/adduser`, user);
	} 
	
	getRoles() {
	    return axios.get(`/api/allroles`);
	} 
   

}

export default new userService()
