import axios from 'axios'

const SAVE = `/api/savedevice`
const GET_ALL = `/api/devices`	

class DeviceService {

    
	save(device) {
        return axios.post(`${SAVE}`, device);
    }
	
	getAll() {
        return axios.get(`${GET_ALL}`);
    }
   

}

export default new DeviceService()
