import axios from 'axios'

const SAVE = `/api/savedevice/`
const GET_ALL = `/api/devices/`
const DELETE = `/api/deviceDelete/`
const CSD_CONNECT = `/api/csdConnect/`
const CSD_DISCONNECT = `/api/csdDisconnect/`
const CSD_REFRESH = `/api/csdRefresh/`
const GET_PORTS = `/api/ports/`

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
	
	connectFromCSD(number) {
		return axios.post(`${CSD_CONNECT}`, number);
	}
	
	disconnectFromCSD(number) {
		return axios.post(`${CSD_DISCONNECT}`, number);
	}
	
	getPorts() {
		return axios.get(`${GET_PORTS}`);
	} 

}

export default new DeviceService()
