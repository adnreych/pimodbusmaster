import React, { Component } from 'react';
import DeviceService from '../service/DeviceService';
import * as Strings from '../helpers/strings';
import DeviceComponent from '../component/DeviceComponent';
import Option from 'muicss/lib/react/option';
import Select from 'muicss/lib/react/select';



class CSDConnectComponent extends Component {
	
	constructor(props) {
        super(props);

		this.state = {
					loading: false,
					deviceId: this.props.match.params.id,
					number: "89805400316",
					port: "/dev/ttyUSB0",
					ports: [],
					error : null,
					success: false
		        }

		this.handleNumberInput = this.handleNumberInput.bind(this);
		this.handleChangePort = this.handleChangePort.bind(this);

    }

    componentDidMount() {
        DeviceService.getPorts()
			.then(ports => {
				console.log("PORTS", ports)
				this.setState({ 
					ports: ports.data,
					port: ports.data.length != 0 ? ports.data[0] : "/dev/ttyUSB0",
					})
			})
			.catch((err) => {
					  console.log("ERROR: ", err);
				  });
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
	
	
	handleChangePort(event) {    
		this.setState({port: event.target.value});  
	}
	
	renderPortsData() {
	      return this.state.ports.map((current) => {
	         return (
				<Option value={current} label={current} />
	         )
	      })
	   }

	render() {	
		const {loading, error, success} = this.state;
		
		return(
			<>
			{error &&
	                        <div className={'alert alert-danger'}>{this.state.error}</div>
	                    }

			<label>Выберите порт:</label>
				<Select name="input" value={this.state.port} onChange={this.handleChangePort}>
					  {this.renderPortsData()}
				</Select>		        
			<br/>
		  	<label>Номер телефона в формате 8ХХХХХХХХХХ</label>
	        <input type="text" className="form-control" defaultValue="89805400316" onChange={(event) => this.handleNumberInput(event)} />	
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
								error: "Ошибка при подключении устройства " + err + ". Нажмите Прервать соединение и попробуйте еще раз",
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
					DeviceService.disconnectFromCSD(request)
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
								error: "Ошибка при прерывания соединения " + err,
								success: false });
						  });	
			}}>Прервать соединение</button>
			
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