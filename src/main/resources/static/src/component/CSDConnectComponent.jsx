import React, { Component } from 'react';
import DeviceService from '../service/DeviceService';
import * as Strings from '../helpers/strings';
import DeviceComponent from '../component/DeviceComponent';



class CSDConnectComponent extends Component {
	
	constructor(props) {
        super(props);

		this.state = {
					loading: false,
					deviceId: this.props.match.params.id,
					number: "89805400316",
					port: "/dev/ttyUSB0",
					error : null,
					success: false
		        }

		this.handleNumberInput = this.handleNumberInput.bind(this);
		this.handlePortInput = this.handlePortInput.bind(this);

    }	
	
	componentDidUpdate() {
		if(window.onbeforeunload == true) {
		    alert("RESULT");
		}
	}
	
	handleNumberInput(event) {
			var number = event.target.value
			this.setState({ number : number})
	}
	
	handlePortInput(event) {
			var port = event.target.value
			this.setState({ port : port})
	}

	render() {	
		const {loading, error, success} = this.state;
		
		return(
			<>
			{error &&
	                        <div className={'alert alert-danger'}>{this.state.error}</div>
	                    }
			<label>Адрес порта</label>
	        <input type="text" className="form-control" onChange={(event) => this.handlePortInput(event)} />	
		  	<label>Номер телефона в формате 8ХХХХХХХХХХ</label>
	        <input type="text" className="form-control" onChange={(event) => this.handleNumberInput(event)} />	
			<button 
				onClick={() => {
					this.setState({ loading : true})
					var request = {
						port : this.state.port,
						phone : this.state.number,
					}
					DeviceService.connectFromCSD(request)
					.then((response) => {
						console.log("response", response)
						if (response.data == "CONNECT") {
							this.setState({ 
								loading: false,
								error: null,
								success: true
							 });
						} else if (response.data == "NO CARRIER") {
							this.setState({ 
								loading: false,
								error: "Не удается установить соединение с устройством",
								success: false });
						}
						
					})
					.catch((err) => {
							  console.log("ERROR: ", err);
							  this.setState({ 
								loading: false,
								error: "Ошибка при подключении устройства " + err + ".Попробуйте обновить статус соединения",
								success: false });
						  });	
			}}>Соединение</button>		
			
			<button 
				onClick={() => {
					this.setState({ loading : true})
					var request = {
						port : this.state.port,
						phone : this.state.number,
					}
					DeviceService.refreshCSD(request)
					.then(() => {
						this.setState({ 
								loading: false,
								error: null,
							 });
						
					})
					.catch((err) => {
							  console.log("ERROR: ", err);
							  this.setState({ 
								loading: false,
								error: "Ошибка при обновлении статуса соединения " + err,
								success: false });
						  });	
			}}>Обновить статус соединения</button>
			
			{success &&
				<DeviceComponent 
				id={this.state.deviceId}
				atRequest = {{
						port : this.state.port,
						phone : this.state.number,
					}} />
						}
			
			{loading &&
                            <img src={Strings.LOADING} />
                        }
		</>
		)
	}

}

export default CSDConnectComponent