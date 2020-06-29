import React, { Component } from 'react';
import * as Strings from '../helpers/strings';
import LoadRegistersService from '../service/LoadRegistersService';
import ModbusService from '../service/ModbusService';
import DeviceService from '../service/DeviceService';
import { confirmAlert } from 'react-confirm-alert';
import 'react-confirm-alert/src/react-confirm-alert.css'; 


class DeviceComponent extends Component {
	
	constructor(props) {
        super(props);

		this.state = {
					loading: false,
					device: [],
					name: "",
					address: "",
					inputValues: [],
					error: null,
					success: null,
					currentChange: null
		        }


		this.handleClickRead = this.handleClickRead.bind(this);
		this.handleClickWrite = this.handleClickWrite.bind(this);
		this.deleteDevice = this.deleteDevice.bind(this);
		this.deleteRegister = this.deleteRegister.bind(this);
		this.handleChangeRegister = this.handleChangeRegister.bind(this);
		this.handleChangeCurrentRegister = this.handleChangeCurrentRegister.bind(this);

    }


	componentDidMount() {
		this.setState({ loading: true });
		LoadRegistersService.getDevice(this.props.match.params.id)
			.then(device => {
				this.setState({ device: device.data});
				if (this.state.device[0] != null) {
					this.setState({ 
						name: this.state.device[0].device.name, 
						address: this.state.device[0].device.address, 
						loading: false,
						inputValues: new Array(this.state.device.length)});
				}
				var dv = this.state.device;
				dv.forEach(element => element.device = element.device.id);
			})
			.catch((err) => {
					  console.log("ERROR: ", err);
					  this.setState({ loading: false })
				  });
    }

	handleClickRead = (address, count, index) => {    
		
		this.setState({ loading: true })
		
		let readRequest = {
            slave: this.state.address,
           	address: address,
			count: count
        }
		
		ModbusService.modbusRead(readRequest)
			.then((response) => {
				console.log("response: ", response);
				var inputValues = this.state.inputValues;
				inputValues[index] = response.data[0];
				if (response.data[0] == null) inputValues[index] = 0;
				 else inputValues[index] = response.data[0].value;
				this.setState({
					loading: false,
					inputValues: inputValues});
				console.log("inputValues index: ", index);
				console.log("inputValues: ", inputValues);
		
			})
			.catch((err) => {
					  console.log("ERROR: ", err);
					  this.setState({ loading: false })
				  });
	}
		
	handleClickWrite = (address, value, index) => {    
			
		this.setState({ loading: true })
		
		let writeRequest = {
            slave: this.state.address,
           	address: address,
			values: [value]
        }
		
		ModbusService.modbusWrite(writeRequest)
			.then((response) => {
				console.log("response: ", response);	
				this.setState({ loading: false });
			})
			.catch((err) => {
					  console.log("ERROR: ", err);
					  this.setState({ loading: false });
				  });
	}
	
	async handleChangeRegister(current, index) {  
		await this.setState({ currentChange: current });
		let { name, address, count, isRead, isWrite, type, multiplier, suffix, min, max } = current;
		confirmAlert({
		  closeOnClickOutside: true,
		  customUI: ({ onClose }) => {
		    return (
		      <div className='custom-ui'>
			        <label>Название</label>
	                <input type="text" className="form-control" defaultValue={name} onChange={(event) => this.handleChangeCurrentRegister(event, "name")} />
					<label>Адрес</label>
	                <input type="text" className="form-control" defaultValue={address} onChange={(event) => this.handleChangeCurrentRegister(event, "address")}  />
					<label>Количество</label>
	                <input type="text" className="form-control" defaultValue={count} onChange={(event) => this.handleChangeCurrentRegister(event, "count")}  />
					<label>Чтение</label>
	                <input type="text" className="form-control" defaultValue={String(isRead)} onChange={(event) => this.handleChangeCurrentRegister(event, "isRead")}  />
					<label>Запись</label>
	                <input type="text" className="form-control" defaultValue={String(isWrite)} onChange={(event) => this.handleChangeCurrentRegister(event, "isWrite")}  />
					<label>Тип</label>
	                <input type="text" className="form-control" defaultValue={type} onChange={(event) => this.handleChangeCurrentRegister(event, "type")}  />
					<label>Множитель</label>
	                <input type="text" className="form-control" defaultValue={multiplier} onChange={(event) => this.handleChangeCurrentRegister(event, "multiplier")}  />
					<label>Суффикс</label>
	                <input type="text" className="form-control" defaultValue={suffix} onChange={(event) => this.handleChangeCurrentRegister(event, "suffix")}  />
					<label>Мин</label>
	                <input type="text" className="form-control" defaultValue={min} onChange={(event) => this.handleChangeCurrentRegister(event, "min")}  />
					<label>Макс</label>
	                <input type="text" className="form-control" defaultValue={max} onChange={(event) => this.handleChangeCurrentRegister(event, "max")}  />
			        <button 
					  onClick={onClose}>Отмена</button>
			        <button
			          onClick={() => {
							this.setState({ loading: true });
							console.log("currentOnChange: ", current);
								LoadRegistersService.changeRegister(current)
									.then(() => {
										this.setState( { success: []});
										this.setState({
											loading: false,
											error: null,
											success: "Данные обновлены успешно"});
										
									})
									.catch((err) => {
											  console.log("ERROR: ", err);
											  this.setState({ 
												loading: false, 
												error: "Ошибка изменения карты регистров",
												success: null })
										  });
								onClose();		
							}}>Сохранить</button>
		      </div>
		    );
		  }
		}); 
	  }

	handleChangeCurrentRegister = (event, key) => {
		var currentChange = this.state.currentChange;
		currentChange[key] = event.target.value;
		this.setState({ currentChange: currentChange });	
	}

	
	handleChange = (event, index) => {    
		var inputValues = this.state.inputValues;
		inputValues[index] = event.target.value;
		this.setState({inputValues: inputValues});
	  }

	deleteRegister(id, index) {
		confirmAlert({
	      title: 'Подтвердите удаление регистра',
	      buttons: [
	        {
	          label: 'Да',
	          onClick: () => {
				LoadRegistersService.deleteRegister(id)
					.then(() => {
						var device = this.state.device;
						device.splice(index, 1);
						this.setState({ device: device });	
					//	this.props.history.push(`/device/${this.props.match.params.id}`);  
					})
					.catch((err) => {
							  console.log("ERROR: ", err);
							  this.setState({ loading: false });
						  });
					}
	        },
	        {
	          label: 'Нет',
	          onClick: () => {}
	        }
	      ]
	    });
	}

	deleteDevice(id) {	
		confirmAlert({
	      title: 'Подтвердите удаление устройства',
	      buttons: [
	        {
	          label: 'Да',
	          onClick: () => {
				DeviceService.deleteDevice(id)
					.then((response) => {
						console.log("response: ", response);
						this.props.history.push("/");
					})
					.catch((err) => {
							  console.log("ERROR: ", err);
							  this.setState({ loading: false });
						  });
					}
	        },
	        {
	          label: 'Нет',
	          onClick: () => {}
	        }
	      ]
	    });		
	}

	
	
	renderTableData() {
      return this.state.device.map((current, index) => {
		 const {loading} = this.state;
         const { id, name, address, count, isRead, isWrite, type, multiplier, suffix, min, max } = current;
         return (
            <tr key={index}>
               <td>{name}</td>
               <td>{address}</td>
               <td>{count}</td>
               <td>{String(isRead)}</td>
			   <td>{String(isWrite)}</td>
               <td>{type}</td>
               <td>{multiplier}</td>
			   <td>{suffix}</td>
			   <td>{min}</td>
               <td>{max}</td>
							
						<td><input type="text" placeholder="Значение" 
								value={this.state.inputValues[index]} 
								ref={index}
								onChange={(event) => this.handleChange(event, index)} /></td>
                        <td><button className="btn btn-primary" onClick={() => this.handleClickRead(address, count, index)} disabled={!isRead}>Чтение</button></td>
                        {loading &&
                            <img src={Strings.LOADING} />
                        }
                        <td><button className="btn btn-primary" onClick={() => this.handleClickWrite(address, this.state.inputValues[index])} disabled={!isWrite}>Запись</button></td>
                        {loading &&
                            <img src={Strings.LOADING} />
                        }
                        <td><button className="btn btn-primary" onClick={() => this.handleChangeRegister(current, index)}>Изменить</button></td>
                        <td><button className="btn btn-primary" onClick={() => this.deleteRegister(id, index)}>Удалить</button></td>
            </tr>
         )
      })
   }


	
	
  	render() {
		
		const {name, address, loading, error, success} = this.state;
		
	    return (
		<div>
			{error &&
                        <div className={'alert alert-danger'}>{this.state.error}</div>
                    }
			
			{success &&
                        <div className={'alert alert-success'}>{this.state.success}</div>
                    }

			<table border="1">
			   <caption>Устройство {name}. Адрес {address}.</caption>
			   <tr>
				<th>Название</th>
			    <th>Адрес</th>
			    <th>Количество</th>
			    <th>Чтение</th>
			    <th>Запись</th>
				<th>Тип</th>
			    <th>Множитель</th>
			    <th>Суффикс</th>
				<th>Мин.</th>
				<th>Макс.</th>
			   </tr>

				<tbody>
				{this.renderTableData()}
				</tbody>

			</table>
			
			<div className="form-group">
                        <button className="btn btn-primary" onClick={() => this.deleteDevice(this.props.match.params.id)} >Удалить устройство</button>
                        {loading &&
                            <img src={Strings.LOADING} />
                        }
                    </div>

		</div>
	    )
	  }
	
}

export default DeviceComponent