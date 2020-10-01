import React, { Component } from 'react';
import * as Strings from '../helpers/strings';
import LoadRegistersService from '../service/LoadRegistersService';
import ModbusService from '../service/ModbusService';
import DeviceService from '../service/DeviceService';
import BitTypeValuesComponent from './BitTypeValuesComponent';
import BoxTypeComponent from './BoxTypeComponent';
import MultipleTypeComponent from './MultipleTypeComponent';
import { confirmAlert } from 'react-confirm-alert';
import 'react-confirm-alert/src/react-confirm-alert.css'; 
import _pull from 'lodash/pull';
import _times from 'lodash/times';
import RGL, { WidthProvider } from "react-grid-layout";
const ReactGridLayout = WidthProvider(RGL);


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
		this.renderDescriptionSpecialTypes = this.renderDescriptionSpecialTypes.bind(this);
		this.callbackFromSpecialType = this.callbackFromSpecialType.bind(this);
		this.callbackFromBitType = this.callbackFromBitType.bind(this);
		this.prepareBitValueToWrite = this.prepareBitValueToWrite.bind(this);
		this.handleChangeGroupList = this.handleChangeGroupList.bind(this);
		this.readCallbackFromMultipleType = this.readCallbackFromMultipleType.bind(this);
		this.writeCallbackFromMultipleType = this.writeCallbackFromMultipleType.bind(this);
		this.handleReadGroup = this.handleReadGroup.bind(this);
		this.handleWriteGroup = this.handleWriteGroup.bind(this);
		this.prepareCommaFloatValueToWrite = this.prepareCommaFloatValueToWrite.bind(this);

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
					if (groups.filter(e => e.name == element.subDevice.name).length == 0) {
						groups.push({
							name : element.subDevice.name,
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
						name: this.state.device[0].subDevice.device.name, 
						address: this.state.device[0].subDevice.device.address, 
						loading: false,
						inputValues: new Array(this.state.device.length),
						dataFromSpecialType: [],
						editedNow: Array.apply(false, Array(this.state.device.length))});
				}
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
	
	handleReadGroup(group) {
		var targetRegisters = this.state.device
			.filter(e => e.registerGroup == group)
			.sort((a, b) => a.address - b.address)
		console.log("targetRegisters", targetRegisters)
		if (targetRegisters.length != 0) {
			var index = this.state.device.indexOf(targetRegisters[0])
			this.handleClickRead(targetRegisters[0].address, targetRegisters.length, index, true)
		}	
	}
	
	handleWriteGroup(group) {
		var values = []
		var targetRegisters = this.state.device
			.filter(e => e.registerGroup == group)
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
		
		var slave = this.state.device[index].subDevice.address == 0 ? this.state.address : this.state.device[index].subDevice.address
		var funcNumber = this.state.device[index].subDevice.address == 0 ? 3 : this.state.device[index].subDevice.function
		
		let readRequest = {
            slave: slave,
           	address: address,
			deviceId: this.state.deviceId,
			count: count,
			type: this.state.device[index].type,
			isCSD: this.state.CSD,
			atConnectionRequest: this.state.ATRequest,
			readGroups: readGroups == undefined ? false : readGroups,	
			function: funcNumber
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
		
		var slave = this.state.device[index].subDevice.address == 0 ? this.state.address : this.state.device[index].subDevice.address
		var funcNumber = this.state.device[index].subDevice.address == 0 ? 10 : this.state.device[index].subDevice.function
		
		let writeRequest = {
            slave: slave,
           	address: address,
			values: Array.isArray(value) ? value : [value],
			type: this.state.device[index].type,
			isCSD: this.state.CSD,
			atConnectionRequest: this.state.ATRequest,
			function: funcNumber
        }

		console.log("writeRequest: ", writeRequest);
		
		if (writeRequest.type == "Bit") {
			var binaryStr = this.prepareBitValueToWrite(this.state.device[index].legends, value)
			writeRequest.values = [parseInt(binaryStr, 2)]
		}
		
		if (writeRequest.type == "CommaFloat") {
			var values = this.prepareCommaFloatValueToWrite(value)
			writeRequest.values = values
		}
		
		if (writeRequest.type == "Box") {
			var first, second
			if (this.state.device[index].legends.first == "Bit") {
				var binaryStr = this.prepareBitValueToWrite(this.state.device[index].legends.first, value.first)
				first = parseInt(binaryStr, 2)
			} else if (writeRequest.type == "CommaFloat") {
				var values = this.prepareCommaFloatValueToWrite(value.first)
				first = values
			} else {
				first = value.first
			}
			
			if (this.state.device[index].legends.second == "Bit") {
				var binaryStr = this.prepareBitValueToWrite(this.state.device[index].legends.second, value.second)
				second = parseInt(binaryStr, 2)
			} else if (writeRequest.type == "CommaFloat") {
				var values = this.prepareCommaFloatValueToWrite(value.second)
				second = values
			}  else {
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
	
	prepareBitValueToWrite(legends, currVal) {
		var result = ""		
		legends.forEach((e) => {
			var bitQuantity = e.bitQuantity
			var i = e.possibleValues.indexOf(currVal[e.description]).toString(2)
			if (i.length < bitQuantity) {
				i = i.padStart((bitQuantity - i.length) + i.length, "0")
			}
			result = result + String(i)
		})
		
		var cv = this.state.currValue.strToWrite = result
		this.setState({ currValue: cv })
		return result
	}
	
	prepareCommaFloatValueToWrite(currVal) {
		var result = []
		var currValStr = String(currVal)
		currValStr = currValStr.replace("\.", "")
		switch (currValStr.length % 4) {
			case 0:
				currValStr = currValStr
				break
			case 1:
				currValStr = "000" + currValStr
				break
			case 2:
				currValStr = "00" + currValStr
				break
			case 3:
				currValStr = "0" + currValStr
				break
			default:
				currValStr = currValStr
				break
		}
		while (currValStr.length > 0) {
			var subStr = currValStr.substring(0, 4)
			result.push(Number(subStr))
			currValStr = currValStr.replace(subStr, "")
		}
		return result
	}
	
	
	handleChange = (event, index) => {    
		var inputValues = this.state.inputValues;
		inputValues[index] = event.target.value;
		this.setState({inputValues: inputValues});
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
		console.log("currentGroup",currentGroup)
		return currentGroup
		.map(
			(current) => {		
			 const {loading, editedNow} = this.state;
	         const {id, name, address, count, isRead, isWrite, type, registerGroup, legends} = current;
			 console.log("current",current)
			 var index = this.state.device.indexOf(this.state.device.filter(e => { return e.address === address })[0])
			 var borderClass = ""
		
			 if (registerGroup == "") {
				borderClass = "no-borderless-td-val"
			 } else {
				borderClass = "borderless-td-val"		
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
			
	         return (
				<>
	            <tr key={index}>
	               <td class={borderClass}>{name}
						{editedNow[index] &&
								<div>
								<br />
	                            <input type="text" className="form-control" defaultValue={name} onChange={(event) => this.handleChangeCurrentRegister(event, "name")}/>
								</div>
	                        }
					</td>
								
					<td class={borderClass}>
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
									
	                        <td class={borderClass}><button className="btn btn-primary" onClick={() => this.handleClickRead(address, count, index)} disabled={!isRead}>Чтение</button>
								<button className="btn btn-primary" onClick={() => this.handleClickWrite(address, this.state.inputValues[index], index)} disabled={!isWrite}>Запись</button>
								</td>
	                        {loading &&
	                            <img src={Strings.LOADING} />
	                        }
														
								
								
								
								          								
	            </tr>
				{(registerGroup != "") && (address == currentGroup[currentGroup.length - 1].address) && <tr align="right">
											<td></td>
											<td></td>
											<td>
												<button className="btn btn-primary" onClick={() => this.handleReadGroup(registerGroup)}>Чтение всей группы ({registerGroup})</button>
												<button className="btn btn-primary" onClick={() => this.handleWriteGroup(registerGroup)}>Запись всей группы ({registerGroup})</button>
											</td>
										</tr> }	
				</>
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
			var registerGroups = new Set();
			var registersByGroups = []
			var currGroupRegisters = this.state.device.filter(e => e.subDevice.name == this.state.groups[index].name)
			console.log("currGroupRegisters",currGroupRegisters)
			currGroupRegisters.forEach(e => registerGroups.add(e.registerGroup))
			console.log("registerGroups",registerGroups)
			registerGroups.forEach(e => registersByGroups.push(currGroupRegisters.filter(e2 => e2.registerGroup == e)))
			console.log("registersByGroups",registersByGroups)
			return (		
					<ReactGridLayout
							          className="layout"
							          cols="4"
							          rowHeight="3"
							        >	
						{this.renderRegistersGrid(registersByGroups)}						
					</ReactGridLayout>
				)		
		}
		
	}
	
	renderRegistersGrid(registersByGroups) {
		var key = 1
		var y = 0
		var x = 0
		return registersByGroups.map(
						(current) => {
							key = key + 1
							if (key % 2 == 0) {
								x = 0
								if (key != 2) y = y + 1
							} else {
								x = 3
							}
							
							return (							
							<div key={key} data-grid={{ w: 1, h: 1, x: x, y: y }}>
								<table border="1">
										<tr>
											<th>{current[0].registerGroup}</th>
									   </tr>
									<tbody>
										{this.renderTableData(current)}			  												
									</tbody>
								</table>
									
							</div>
							
							)
						}
					)
	}
	
	
	render() {	
		const {loading, error, success, name, address} = this.state;
		console.log("DEVICE", this.state.device)
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
	                        {loading &&
	                            <img src={Strings.LOADING} />
	                        }
	            </div>
			</div>
	    )
	  }
	
}

export default DeviceComponent