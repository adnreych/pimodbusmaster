import axios from 'axios'

const SAVE = `/api/savesubdevices/`

class SubDevicesService {

    
	save(groups) {
        return axios.post(`${SAVE}`, groups);
    }
	
}

export default new SubDevicesService()