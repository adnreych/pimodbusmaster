import React, { Component } from 'react';
import ReactTextCollapse from 'react-text-collapse'
import * as Strings from '../helpers/strings';
import LoadRegistersService from '../service/LoadRegistersService';
import ModbusService from '../service/ModbusService';
import DeviceService from '../service/DeviceService';
import SpecialModbusTypesComponent from './SpecialModbusTypesComponent';
import BitTypeValuesComponent from './BitTypeValuesComponent';
import BoxTypeComponent from './BoxTypeComponent';
import MultipleTypeComponent from './MultipleTypeComponent';
import { confirmAlert } from 'react-confirm-alert';
import 'react-confirm-alert/src/react-confirm-alert.css'; 
import Option from 'muicss/lib/react/option';
import Select from 'muicss/lib/react/select';
import _pull from 'lodash/pull';
import _times from 'lodash/times';


class DeviceComponent extends Component {
	
	constructor(props) {
        super(props);

		this.state = {
					loading: false,
					device: [],
					deviceId: null,
					groups: [],
					currGroup: 0,
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
					editedNowSpecialTypeIndexes: [],
					CSD: false,
					ATRequest: {}
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
		this.prepareValueToWrite = this.prepareValueToWrite.bind(this);
		this.handleChangeGroupList = this.handleChangeGroupList.bind(this);
		this.readCallbackFromMultipleType = this.readCallbackFromMultipleType.bind(this);
		this.writeCallbackFromMultipleType = this.writeCallbackFromMultipleType.bind(this);
		this.handleReadGroup = this.handleReadGroup.bind(this);
		this.handleWriteGroup = this.handleWriteGroup.bind(this);

    }


	componentDidMount() {
		this.setState({ loading: true });
		var id;
		if (this.props.match == undefined) {
			id = this.props.id
			this.setState({ CSD : true,
							ATRequest: this.props.atRequest,
							deviceId: id
							})
		} else {
			id = this.props.match.params.id
			this.setState({ deviceId: id })
		}
		LoadRegistersService.getDevice(id)
			.then(device => {
				var groups = [];
				device.data.forEach(element => {
					if (groups.filter(e => e.name == element.group).length == 0) {
						groups.push({
							name : element.group,
							checked: true
						})
					}
				});
				var childrenTabs = []
				groups.forEach((e, index) => {
					var currChildren = {
						tab: index,
						name: e.name
					}
					childrenTabs.push(currChildren)
				})
				
				this.setState({ device: device.data,
								groups: groups,
								childrenTabs: childrenTabs});
								
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
	
	readCallbackFromMultipleType = (address, count, index) => {
		this.handleClickRead(address, count, index)
	}
	
	writeCallbackFromMultipleType = (address, value, index) => {
		this.handleClickWrite(address, value, index)		
	}
	
	handleReadGroup(groupId) {
		var targetRegisters = this.state.device
			.filter(e => e.registerGroup != null && e.registerGroup.id == groupId)
			.sort((a, b) => a.address - b.address)
		console.log("targetRegisters", targetRegisters)
		if (targetRegisters.length != 0) {
			var index = this.state.device.indexOf(targetRegisters[0])
			this.handleClickRead(targetRegisters[0].address, targetRegisters.length, index, true)
		}	
	}
	
	handleWriteGroup(groupId) {
		var values = []
		var targetRegisters = this.state.device
			.filter(e => e.registerGroup != null && e.registerGroup.id == groupId)
			.sort((a, b) => a.address - b.address)
				
		if (targetRegisters.length != 0) {
			var index = this.state.device.indexOf(targetRegisters[0])
			_times(targetRegisters.length, (i) => values.push(this.state.inputValues[index + i]))
			console.log("values", values)
			if (values.length != targetRegisters.length) {
				this.setState({ error: "Заполните все значения для записи группы регистров!" })
			} else {
				this.handleClickWrite(targetRegisters[0].address, values, index)
			}		
		}	
	}
	
	
	handleChangeGroupList = (group) => {
		var groups = this.state.groups	
		var curr = groups.filter(e => e.name == group.name)[0]
		
		if (curr.checked) {
			groups[groups.indexOf(curr)].checked = false
		} else {
			groups[groups.indexOf(curr)].checked = true
		}	
		this.setState({ groups: groups })
	}

	handleClickRead = (address, count, index, readGroups) => {    
		
		this.setState({ loading: true })
		
		let readRequest = {
            slave: this.state.address,
           	address: address,
			deviceId: this.state.deviceId,
			count: count,
			type: this.state.device[index].type,
			isCSD: this.state.CSD,
			atConnectionRequest: this.state.ATRequest,
			readGroups: readGroups == undefined ? false : readGroups,	
        }

		console.log("readRequest: ", readRequest);
		
		ModbusService.modbusRead(readRequest)
			.then((response) => {
				var inputValues = this.state.inputValues;
				if (response.data == null) {
					this.setState({ loading: false,
									  error: "Не удалось прочитать значения регистров" })
					}
				if (!readGroups) {
					inputValues[index] = response.data[0];
				} else {
					response.data.forEach((e, i) => {inputValues[index + i] = e})
				}
				this.setState({
							loading: false,
							error: null,
							inputValues: inputValues});
				console.log("inputValues: ", this.state.inputValues);
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
			values: Array.isArray(value) ? value : [value],
			type: this.state.device[index].type,
			isCSD: this.state.CSD,
			atConnectionRequest: this.state.ATRequest
        }

		console.log("writeRequest: ", writeRequest);
		
		if (writeRequest.type == "Bit") {
			var binaryStr = prepareValueToWrite(this.state.device[index].legends, value)
			writeRequest.values = [parseInt(binaryStr, 2)]
		}
		
		if (writeRequest.type == "Box") {
			var first, second
			if (this.state.device[index].legends.first == "Bit") {
				var binaryStr = prepareValueToWrite(this.state.device[index].legends.first, value.first)
				first = parseInt(binaryStr, 2)
			} else {
				first = value.first
			}
			
			if (this.state.device[index].legends.second == "Bit") {
				var binaryStr = prepareValueToWrite(this.state.device[index].legends.second, value.second)
				second = parseInt(binaryStr, 2)
			} else {
				second = value.second
			}
			
			var result = String(first) + String(second)
			writeRequest.values = [parseInt(result, 10)]
		}
		
		ModbusService.modbusWrite(writeRequest)
			.then((response) => {
				console.log("response: ", response);	
				if (response.data == "OK") {
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
	
	prepareValueToWrite(legends, currVal) {
		var result = "";
		
		legends.forEach((e) => {
			var bitQuantity = e.bitQuantity
			var i = e.possibleValues.indexOf(currVal[e.description]).toString(2)
			if (i.length < bitQuantity) {
				i = i.padStart((bitQuantity - i.length) + i.length, "0")
			}
			result = result + String(i);
		})
		
		var cv = this.state.currValue.strToWrite = result
		this.setState({ currValue: cv })
		return result;

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
						  <Option value="Box" label="Box" />
						  <Option value="Multiple" label="Multiple" />
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

	
	renderTableData(currentGroup) {
		var groupNameData = []
		return this.state.device
		.filter(e => e.group == currentGroup.name)
		.sort((a, b) => (a.registerGroup != null && b.registerGroup != null) ? a.registerGroup.id - b.registerGroup.id : 0)
		.map(
			(current) => {		
			 const {loading, editedNow} = this.state;
	         const {id, name, address, count, isRead, isWrite, type, multiplier, suffix, minValue, maxValue, group, legends} = current;
			 var index = this.state.device.indexOf(this.state.device.filter(e => { return e.address === address })[0])
			 var boxLegends = {}
		
			 var registerGroupName = current.registerGroup == null ? null : current.registerGroup.name
			 var firstGroupElement = false
		
			 if (current.registerGroup == null) {
				registerGroupName = null
			 } else {
				registerGroupName = current.registerGroup.name
				if (groupNameData.find(e => e == registerGroupName) == undefined) {
					groupNameData.push(registerGroupName)
					firstGroupElement = true
				}
			 }
		
			 if (type=="Multiple") {
				return(				
					<MultipleTypeComponent 
						registerInfo={current} 
						count={count} 
						inputValues={this.state.inputValues[index] == undefined ? new Array(count) : this.state.inputValues[index]}  
						index={index}
						callbackFromBitType={this.callbackFromBitType}
						writeClick={this.writeCallbackFromMultipleType}
						readClick={this.readCallbackFromMultipleType}
						deleteClick={this.deleteRegister} />					
				)
			 }
			
			 if (type=="Box") {
				console.log(legends)
				boxLegends = JSON.parse(legends)
			 }
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
									  <Option value="Box" label="Box" />
									  <Option value="Multiple" label="Multiple" />
							    </Select>
								</div>
	                        }
					</td>
					<td>{(type=="Variable" || type=="Bit") && <ReactTextCollapse options={Strings.TEXT_COLLAPSE_OPTIONS}>
							{this.renderDescriptionSpecialTypes(type, legends)}
						</ReactTextCollapse>}
						
						{(type=="Box") && <ReactTextCollapse options={Strings.TEXT_COLLAPSE_OPTIONS}>
							{(boxLegends.first.type === "Variable" || boxLegends.first.type === "Bit") 
								&& <>{this.renderDescriptionSpecialTypes(boxLegends.first.type, JSON.stringify(boxLegends.first.content))}</>}
							{(boxLegends.second.type === "Variable" || boxLegends.second.type === "Bit") 
								&& <><hr />{this.renderDescriptionSpecialTypes(boxLegends.second.type, JSON.stringify(boxLegends.second.content))}</>}
						</ReactTextCollapse>}
						
						{(type!="Variable" && type!="Bit" && type!="Box" && (multiplier != null)) && <p class="small-text">*{multiplier}</p>}
						{(type!="Variable" && type!="Bit" && type!="Box" && (suffix != "")) && <p class="small-text">Единица измерения: {suffix}</p>}		
						{(type!="Variable" && type!="Bit" && type!="Box" && (minValue != null && maxValue != null)) && <p class="small-text">От {minValue} до {maxValue}</p>}
						
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
								
							{(type=="Box") && <BoxTypeComponent 
								index={index} 
								pair={legends} 
								callbackFromParent={this.callbackFromBitType} 
								value={this.state.inputValues[index]} 
								/>}
							
							{(type!="Bit" && type!="Box") && 
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
								
								{(firstGroupElement) && <td rowspan="0">
										<button className="btn btn-primary" onClick={() => this.handleReadGroup(current.registerGroup.id)}>Чтение всей группы ({registerGroupName})</button>
										<button className="btn btn-primary" onClick={() => this.handleWriteGroup(current.registerGroup.id)}>Запись всей группы ({registerGroupName})</button>
									</td>}
								
								
								          								
	            </tr>
						
	         )
	      }) 
   }

	
	renderGroupsTabs() {
		return this.state.groups.map((current, index) => {
			return(
				<button className="btn btn-primary" onClick={() => this.setState({ currGroup: index })} >{current.name}</button>
			)
		})
	}
	
	renderCurrentGroup(index) {
		if (this.state.groups.length > 0) {
			return (
					<div>
						<p>{this.state.groups[index].name}</p>
						<table border="1">
								<tr>
									<th>Название</th>
								    <th>Адрес</th>
								    <th>Количество</th>
								    <th>Чтение</th>
								    <th>Запись</th>
									<th>Тип</th>
									<th>Описание</th>
							   </tr>
							<tbody>	
								{this.renderTableData(this.state.groups[index])}			  												
							</tbody>
						</table>				
					</div>
			)
		}
		
	}
	
	
	render() {	
		const {loading, error, success, name, address} = this.state;
		
	    return (
			<div>
				{error &&
	                        <div className={'alert alert-danger'}>{this.state.error}</div>
	                    }
				
				{success &&
	                        <div className={'alert alert-success'}>{this.state.success}</div>
	                    }

				

				<p>Устройство {name}. Адрес {address}.</p>

				{this.renderGroupsTabs()}	
				{this.renderCurrentGroup(this.state.currGroup)}	
					
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