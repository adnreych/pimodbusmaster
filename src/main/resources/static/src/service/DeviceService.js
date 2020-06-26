import axios from 'axios'

const SAVE = `/api/savedevice`
const GET_ALL = `/api/devices`
const DELETE = `/api/deviceDelete/`

class DeviceService {

    
	save(device) {
        return axios.post(`${SAVE}`, device);
    }
	
	getAll() {
        return axios.get(`${GET_ALL}`);
    }
	
	deleteDevice(deviceId) {
        return axios.delete(`${DELETE}` + deviceId);
    }
   

}

export default new DeviceService()
