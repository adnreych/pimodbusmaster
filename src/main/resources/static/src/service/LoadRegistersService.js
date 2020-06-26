import axios from 'axios'

const PATH = `/api/loadregisters`
const GET_DEVICE = `/api/device/`

class LoadRegistersService {
    
	load(registers) {
        return axios.post(`${PATH}`, registers);
    } 
	
	
	getDevice(deviceId) {
		return axios.get(`${GET_DEVICE}` + deviceId);
	}

}

export default new LoadRegistersService()
