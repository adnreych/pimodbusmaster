import axios from 'axios'

const PATH = `/api/loadregisters`

class LoadRegistersService {
    
	load(registers) {
        return axios.post(`${PATH}`, registers);
    } 

}

export default new LoadRegistersService()
