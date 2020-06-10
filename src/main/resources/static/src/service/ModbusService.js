import axios from 'axios'

const PATH = `/modbusread`

class ModbusService {

    getModbusResponse() {
        return axios.get(`${PATH}`);
    }
    
    modbusRequest(modbusRequest) {
        return axios.post(`${PATH}`, modbusRequest);
    }
   

}

export default new ModbusService()
