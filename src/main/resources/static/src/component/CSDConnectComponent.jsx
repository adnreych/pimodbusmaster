import React, { Component } from 'react';
import DeviceService from '../service/DeviceService';
import * as Strings from '../helpers/strings';



class CSDConnectComponent extends Component {
	
	constructor(props) {
        super(props);

		this.state = {
					loading: false,
					deviceId: this.props.match.params.id,
					number: "",
					port: "",
					error : null
		        }

		this.handleNumberInput = this.handleNumberInput.bind(this);
		this.handlePortInput = this.handlePortInput.bind(this);

    }	

    componentDidMount() {
        
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
		const {loading, error} = this.state;
		
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
						// показываем таблицу регистров устройства
						this.setState({ 
							loading: false,
							error: null
						 });
					})
					.catch((err) => {
							  console.log("ERROR: ", err);
							  this.setState({ 
								loading: false,
								error: "Ошибка при подключении устройства " + err });
						  });	
			}}>Соединение</button>
			
			{loading &&
                            <img src={Strings.LOADING} />
                        }
		</>
		)
	}

}

export default CSDConnectComponent