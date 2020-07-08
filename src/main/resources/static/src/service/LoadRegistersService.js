import axios from 'axios'

const PATH = `/api/registers/load`
const GET_DEVICE = `/api/device/`
const CHANGE_REGISTER = `/api/registers/change`
const DELETE_REGISTER = `/api/registers/delete/`
const ADD_REGISTER = `/api/registers/add`

class LoadRegistersService {
    
	load(registers) {
        return axios.post(`${PATH}`, registers);
    } 
	
	
	getDevice(deviceId) {
		return axios.get(`${GET_DEVICE}` + deviceId);
	}
	
	changeRegister(register) {
		return axios.put(`${CHANGE_REGISTER}`, register);
	}
	
	deleteRegister(registerId) {
		return axios.delete(`${DELETE_REGISTER}` + registerId);
	}
	
	addRegister(register) {
		return axios.post(`${ADD_REGISTER}`, register);
	}

}

export default new LoadRegistersService()
