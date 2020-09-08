import axios from 'axios'

const SAVE = `/api/registerGroup/save/`
const GET_ALL = `/api/registerGroup/`

class GroupRegistersService {

    
	save(groups) {
        return axios.post(`${SAVE}`, groups);
    }
	
	getAll() {
        return axios.get(`${GET_ALL}`);
    }
	
	getById(groupId) {
        return axios.get(`${GET_ALL}` + groupId);
    }
	
}

export default new GroupRegistersService()