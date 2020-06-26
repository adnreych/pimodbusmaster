import axios from 'axios'

const READ = `/api/modbusread`
const WRITE = `/api/modbuswrite`

class ModbusService {

    getModbusResponse() {
        return axios.get(`${READ}`);
    }
    
    modbusRead(readRequest) {
        return axios.post(`${READ}`, readRequest);
    }
    
    modbusWrite(writeRequest) {
        return axios.post(`${WRITE}`, writeRequest);
    }
   

}

export default new ModbusService()
