import React, { Component } from 'react';
import ReactTextCollapse from 'react-text-collapse'
import * as Strings from '../helpers/strings';
import LoadRegistersService from '../service/LoadRegistersService';
import ModbusService from '../service/ModbusService';
import DeviceService from '../service/DeviceService';
import SpecialModbusTypesComponent from './SpecialModbusTypesComponent';
import BitTypeValuesComponent from './BitTypeValuesComponent';
import { confirmAlert } from 'react-confirm-alert';
import 'react-confirm-alert/src/react-confirm-alert.css'; 
import Option from 'muicss/lib/react/option';
import Select from 'muicss/lib/react/select';
import _pull from 'lodash/pull';


class DeviceComponent extends Component {
	
	constructor(props) {
        super(props);

		this.state = {
					loading: false,
					device: [],
					name: "",
					address: "",
					inputValues: [],
					editedNow: [],
					error: null,
					success: null,
					currentChange: null,
					addedRegister: {},
					currentLegend: "",
					dataFromSpecialType: [],
					editedNowSpecialTypeIndexes: []
		        }


		this.handleClickRead = this.handleClickRead.bind(this);
		this.handleClickWrite = this.handleClickWrite.bind(this);
		this.deleteDevice = this.deleteDevice.bind(this);
		this.deleteRegister = this.deleteRegister.bind(this);
		this.handleChangeRegister = this.handleChangeRegister.bind(this);
		this.handleChangeCurrentRegister = this.handleChangeCurrentRegister.bind(this);
		this.addRegister = this.addRegister.bind(this);
		this.handleChangeAddedRegister = this.handleChangeAddedRegister.bind(this);
		this.handleConfirmChangeRegister = this.handleConfirmChangeRegister.bind(this);
		this.renderDescriptionSpecialTypes = this.renderDescriptionSpecialTypes.bind(this);
		this.callbackFromSpecialType = this.callbackFromSpecialType.bind(this);
		this.callbackFromBitType = this.callbackFromBitType.bind(this);

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
						inputValues: new Array(this.state.device.length),
						dataFromSpecialType: [],
						editedNow: Array.apply(false, Array(this.state.device.length))});
				}
				var dv = this.state.device;
				dv.forEach(element => {
					element.device = element.device.id;
				});
			})
			.catch((err) => {
					  console.log("ERROR: ", err);
					  this.setState({ loading: false })
				  });
    }

	callbackFromSpecialType = (dataFromSpecialType) => {
        this.setState({ dataFromSpecialType: dataFromSpecialType });
		var device = this.state.device
		console.log("device: ", device)
		var editedNowSpecialTypeIndexes = this.state.editedNowSpecialTypeIndexes;
		_pull(editedNowSpecialTypeIndexes, dataFromSpecialType.index);
		device[dataFromSpecialType.index].legends = dataFromSpecialType
		this.setState({device : device,
					   editedNowSpecialTypeIndexes: editedNowSpecialTypeIndexes})
    }

	callbackFromBitType = (currValue, index) => {
		var inputValues = this.state.inputValues
		inputValues[index] = currValue
		this.setState({ inputValues: inputValues })
		//console.log("STATEFromBitType: ", this.state);
	}

	handleClickRead = (address, count, index) => {    
		
		this.setState({ loading: true })
		
		let readRequest = {
            slave: this.state.address,
           	address: address,
			count: count,
			type: this.state.device[index].type
        }

		console.log("readRequest: ", readRequest);
		
		ModbusService.modbusRead(readRequest)
			.then((response) => {
				var inputValues = this.state.inputValues;
				inputValues[index] = response.data;
				if (response.data == null) {
					this.setState({ loading: false,
									  error: "Не удалось прочитать значения регистров" })
				}
				else {
					inputValues[index] = response.data;
					this.setState({
						loading: false,
						error: null,
						inputValues: inputValues});
				}
			})
			.catch((err) => {
					  console.log("ERROR: ", err);
					  this.setState({ loading: false,
									  error: "Ошибка чтения значения" })
				  });
	}
		
	handleClickWrite = (address, value, index) => {    
			
		this.setState({ loading: true })
		
		let writeRequest = {
            slave: this.state.address,
           	address: address,
			values: [value],
			type: this.state.device[index].type
        }

		console.log("writeRequest: ", writeRequest);
		
		ModbusService.modbusWrite(writeRequest)
			.then((response) => {
				console.log("response: ", response);	
				if (response == "OK") {
					this.setState({ loading: false,
								    success: "Запись значений выполнена успешно" });
				} else {
					this.setState({ loading: false,
									  error: "Ошибка записи значения" });
				}
				
			})
			.catch((err) => {
					  console.log("ERROR: ", err);
					  this.setState({ loading: false,
									  error: "Ошибка записи значения" });
				  });
	}
	
	handleConfirmChangeRegister(index) {
		var editedNow = this.state.editedNow;
		var editedNowSpecialTypeIndexes = this.state.editedNowSpecialTypeIndexes;
		_pull(editedNowSpecialTypeIndexes, index);
		 editedNow[index] = false;
		 this.setState({ editedNow:  editedNow,
						 currentChange: "",
						 editedNowSpecialTypeIndexes: editedNowSpecialTypeIndexes  });
	}
	
	handleChangeRegister(current, index) {  
		 var editedNowSpecialTypeIndexes = this.state.editedNowSpecialTypeIndexes;
		 editedNowSpecialTypeIndexes.push(index);
		 var editedNow = this.state.editedNow;
		 editedNow[index] = true;
		 this.setState({ editedNow:  editedNow,
						 currentChange: current,
						 editedNowSpecialTypeIndexes: editedNowSpecialTypeIndexes });
	  }

	addRegister() {
		this.setState({
			addedRegister : {
				device: this.state.device[0].device,
				name: "",
				address: "",
				count: "",
				isRead: true,
				isWrite: false,
				type: "",
				multiplier: "",
				suffix: "",
				mix: 0,
				max: 0,
				group: "",
				legends: null
			}
		})
		let { name, address, count, isRead, isWrite, type, multiplier, suffix, min, max, group } = this.state.addedRegister;
		confirmAlert({
		  closeOnClickOutside: true,
		  customUI: ({ onClose }) => {
		    return (
		      <div className='custom-ui'>
					<tr>
					<td>
			        <label>Название</label>
	                <input type="text" className="form-control" defaultValue={name} onChange={(event) => this.handleChangeAddedRegister(event, "name")}/>
					</td>
					<td>
					<label>Адрес</label>
	                <input type="text" className="form-control" defaultValue={address} onChange={(event) => this.handleChangeAddedRegister(event, "address")} />
					</td>
					<td>
					<label>Количество</label>
	                <input type="text" className="form-control" defaultValue={count} onChange={(event) => this.handleChangeAddedRegister(event, "count")} />
					</td>
					<td>
					<label>Чтение</label>
					<Select name="input" defaultValue={String(isRead)} onChange={(event) => this.handleChangeAddedRegister(event, "isRead")} >
				          <Option value="true" label="true" />
						  <Option value="false" label="false" />
				    </Select>
					</td>
					<td>
	               	<label>Запись</label>
					<Select name="input" defaultValue={String(isWrite)} onChange={(event) => this.handleChangeAddedRegister(event, "isWrite")} >
				          <Option value="true" label="true" />
						  <Option value="false" label="false" />
				    </Select>
					</td>
					<td>
	                <label>Тип</label>
					<Select name="input" defaultValue={type} onChange={(event) => this.handleChangeAddedRegister(event, "type")} >
				          <Option value="SignedInt" label="SignedInt" />
						  <Option value="UnsignedInt" label="UnsignedInt" />
						  <Option value="Float" label="Float" />
						  <Option value="Variable" label="Variable" />
						  <Option value="Bit" label="Bit" />
				    </Select>
					</td>
					<td>
					<label>Множитель</label>
	                <input type="text" className="form-control" defaultValue={multiplier} onChange={(event) => this.handleChangeAddedRegister(event, "multiplier")} />
					</td>
					<td>
					<label>Суффикс</label>
	                <input type="text" className="form-control" defaultValue={suffix} onChange={(event) => this.handleChangeAddedRegister(event, "suffix")} />
					</td>
					<td>
					<label>Мин</label>
	                <input type="text" className="form-control" defaultValue={min} onChange={(event) => this.handleChangeAddedRegister(event, "min")} />
					</td>
					<td>
					<label>Макс</label>
	                <input type="text" className="form-control" defaultValue={max} onChange={(event) => this.handleChangeAddedRegister(event, "max")} />
					</td>
					<td>
					<label>Группа</label>
	                <input type="text" className="form-control" defaultValue={group} onChange={(event) => this.handleChangeAddedRegister(event, "group")} />
					</td>
					<td>
			        <button 
					  onClick={() => {
							onClose();	
						}}>Отмена</button>
					</td>
					<td>
			        <button
			          onClick={() => {
							this.setState({ loading: true });
								LoadRegistersService.addRegister(this.state.addedRegister)
									.then((response) => {	
										var device = this.state.device;
										var addedRegister = this.state.addedRegister;
										addedRegister.id = response.data;
											device.push(addedRegister);
											this.setState({
												device: device,
												loading: false,
												error: null,
												success: "Данные обновлены успешно"
											})	
									})
									.catch((err) => {
											 console.log("ERROR: ", err);
											  this.setState({ 
												loading: false, 
												error: "Ошибка добавления регистра",
												success: null })
										  });
								onClose();		
							}}>Сохранить</button>
						</td>
					</tr>
		      </div>
		    );
		  }
		});
	}
	
	handleChangeAddedRegister = (event, key) => {
		var addedRegister = this.state.addedRegister;
		addedRegister[key] = event.target.value;
		if (key == "type") {
			this.setState({ currentLegend: event.target.value });
		}	
		this.setState({ addedRegister: addedRegister });	
	}

	handleChangeCurrentRegister = (event, key) => {
		var currentChange = this.state.currentChange;
		currentChange[key] = event.target.value;
		if (key == "type") {
			this.setState({ currentLegend: event.target.value });
		}	
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
					})
					.catch((err) => {
							  console.log("ERROR: ", err);
							  this.setState({ 
								loading: false,
								error: "Ошибка при удалении регистра" });
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
						this.props.history.push("/");
					})
					.catch((err) => {
							  console.log("ERROR: ", err);
							  this.setState({ 
								loading: false,
								error: "Ошибка при удалении устройства" });
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
	
	renderDescriptionSpecialTypes(type, legends) {
		var legendStrings = []
		if (this.state.dataFromSpecialType.length != 0) legends = JSON.stringify(this.state.dataFromSpecialType)
		
		if (legends == "null" || legends == null) {
			return(<p class="small-text">Нет описания</p>)
		} else {
			var l = JSON.parse(legends);
			if (type == "Variable") {
			l.forEach(e => {
				var str = `${e.description} : ${e.value}`
				legendStrings.push(str)
			})
			} else if (type == "Bit") {
				l.forEach(e => {
					var end = Number(e.startBit) + Number(e.bitQuantity) - 1;
					var str = `${e.description} (биты ${e.startBit} - ${end}) : ${e.possibleValues}`
					legendStrings.push(str)
			})
			}
			return legendStrings.map((e) => {
				return (
					<p class="small-text">{e}</p>
				)
			})
		}
		
	}

	
	
	renderTableData() {
      return this.state.device.map((current, index) => {
		const TEXT_COLLAPSE_OPTIONS = {
			  collapse: false, // default state when component rendered
			  collapseText: '... Развернуть', // text to show when collapsed
			  expandText: 'Свернуть', // text to show when expanded
			  minHeight: 30, // component height when closed
			  maxHeight: 250, // expanded to
			}
		 const {loading, editedNow} = this.state;
         const { id, name, address, count, isRead, isWrite, type, multiplier, suffix, min, max, group, legends} = current;
         return (
            <tr key={index}>
               <td>{name}
					{editedNow[index] &&
							<div>
							<br />
                            <input type="text" className="form-control" defaultValue={name} onChange={(event) => this.handleChangeCurrentRegister(event, "name")}/>
							</div>
                        }
				</td>
               <td>{address}
					{editedNow[index] &&
							<div>
							<br />
                            <input type="text" className="form-control" defaultValue={address} onChange={(event) => this.handleChangeCurrentRegister(event, "address")}/>
							</div>
                        }
				</td>
               <td>{count}
					{editedNow[index] &&
							<div>
							<br />
                            <input type="text" className="form-control" defaultValue={count} onChange={(event) => this.handleChangeCurrentRegister(event, "count")}/>
							</div>
                        }
				</td>
               <td>{String(isRead)}
					{editedNow[index] &&
							<div>
							<br />
                            <Select name="input" defaultValue={String(isRead)} onChange={(event) => this.handleChangeCurrentRegister(event, "isRead")} >
						          <Option value="true" label="true" />
								  <Option value="false" label="false" />
						    </Select>
							</div>
                        }
				</td>
			   <td>{String(isWrite)}
					{editedNow[index] &&
							<div>
							<br />
                            <Select name="input" defaultValue={String(isWrite)} onChange={(event) => this.handleChangeCurrentRegister(event, "isWrite")} >
						          <Option value="true" label="true" />
								  <Option value="false" label="false" />
						    </Select>
							</div>
                        }
				</td>
               <td>{type}
					{editedNow[index] &&
							<div>
							<br />
							<Select name="input" defaultValue={type} onChange={(event) => this.handleChangeCurrentRegister(event, "type")} >
						          <Option value="SignedInt" label="SignedInt" />
								  <Option value="UnsignedInt" label="UnsignedInt" />
								  <Option value="Float" label="Float" />
								  <Option value="Variable" label="Variable" />
								  <Option value="Bit" label="Bit" />
						    </Select>
							</div>
                        }
				</td>
               <td>{multiplier}
					{editedNow[index] &&
							<div>
							<br />
                            <input type="text" className="form-control" defaultValue={multiplier} onChange={(event) => this.handleChangeCurrentRegister(event, "multiplier")}/>
							</div>
                        }
				</td>
			   <td>{suffix}
					{editedNow[index] &&
							<div>
							<br />
                            <input type="text" className="form-control" defaultValue={suffix} onChange={(event) => this.handleChangeCurrentRegister(event, "suffix")}/>
							</div>
                        }
				</td>
			   <td>{min}
					{editedNow[index] &&
							<div>
							<br />
                            <input type="text" className="form-control" defaultValue={min} onChange={(event) => this.handleChangeCurrentRegister(event, "min")}/>
							</div>
                        }
				</td>
               <td>{max}
					{editedNow[index] &&
							<div>
							<br />
                            <input type="text" className="form-control" defaultValue={max} onChange={(event) => this.handleChangeCurrentRegister(event, "max")}/>
							</div>
                        }
				</td>
				<td>{group}
					{editedNow[index] &&
							<div>
							<br />
                            <input type="text" className="form-control" defaultValue={group} onChange={(event) => this.handleChangeCurrentRegister(event, "group")}/>
							</div>
                        }
				</td>
				<td>{(type=="Variable" || type=="Bit") && <ReactTextCollapse options={TEXT_COLLAPSE_OPTIONS}>
						{this.renderDescriptionSpecialTypes(type, legends, index)}
					</ReactTextCollapse>}
					
					{editedNow[index] &&
							<div>
							{(type=="Variable" || type=="Bit") && <SpecialModbusTypesComponent targetType={type} index={index} data={legends} callbackFromParent={this.callbackFromSpecialType } />}
							</div>
                        }
				</td>
							
				<td>
						{(type=="Bit") && <BitTypeValuesComponent 
							index={index} 
							legends={legends} 
							callbackFromParent={this.callbackFromBitType} 
							value={this.state.inputValues[index]} 
							/>}
						
						{(type!="Bit") && 
						<input type="text" placeholder="Значение" 
						value={this.state.inputValues[index]} 
						ref={index}
						onChange={(event) => this.handleChange(event, index)} />}
				</td>
								
                        <td><button className="btn btn-primary" onClick={() => this.handleClickRead(address, count, index)} disabled={!isRead}>Чтение</button>
							<button className="btn btn-primary" onClick={() => this.handleClickWrite(address, this.state.inputValues[index], index)} disabled={!isWrite}>Запись</button>
							</td>
                        {loading &&
                            <img src={Strings.LOADING} />
                        }

                        <td><button className="btn btn-primary" onClick={() => this.handleChangeRegister(current, index)} disabled={editedNow[index]}>Изменить</button>
							<button className="btn btn-primary" onClick={() => {
								this.handleConfirmChangeRegister(index)
								this.setState({ loading: true });			
								console.log("currentOnChange2: ", current);
								current.legends = JSON.stringify(current.legends)
								if (!(type=="Variable" || type=="Bit")) {
									current.legends = null
								}
								LoadRegistersService.changeRegister(current)
									.then(() => {
										var device = this.state.device;
										device[index] = current;
										this.setState({
											device: device,
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
							}} disabled={!editedNow[index] || (this.state.editedNowSpecialTypeIndexes.includes(index) && (type=="Variable" || type=="Bit"))}>Сохранить</button>
							<button className="btn btn-primary" onClick={() => {
								this.handleConfirmChangeRegister(index);
							}} disabled={!editedNow[index]}>Отменить</button>
						</td>
													
							<td>
							<button className="btn btn-primary" onClick={() => this.deleteRegister(id, index)}>Удалить</button>
							</td>
							          								
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
					<th>Группа</th>
					<th>Описание</th>
				   </tr>
	
					<tbody>
					{this.renderTableData()}
					</tbody>
	
				</table>
				
				<div className="form-group">
	                        <button className="btn btn-primary" onClick={() => this.deleteDevice(this.props.match.params.id)} >Удалить устройство</button>
							<button className="btn btn-primary" onClick={() => this.addRegister()} >Добавить регистр</button>
	                        {loading &&
	                            <img src={Strings.LOADING} />
	                        }
	                    </div>
	
			</div>
	    )
	  }
	
}

export default DeviceComponent