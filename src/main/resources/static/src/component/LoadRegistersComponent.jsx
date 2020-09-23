import React, { Component } from 'react';
import LoadRegistersService from '../service/LoadRegistersService';
import DeviceService from '../service/DeviceService';
import GroupRegistersService from '../service/GroupRegistersService';
import * as Strings from '../helpers/strings';


class LoadRegistersComponent extends Component {
	
	constructor(props) {
        super(props);

		this.state = {
					data: [],
					fileInfo: [],
					error: null,
					loading: false,
					success: null,
					deviceName: "",
					deviceAddress: "",
					groupsToSave: []
		        }

	  this.handleClick = this.handleClick.bind(this);
	  this.handleChangeDeviceName = this.handleChangeDeviceName.bind(this);
	  this.handleChangeDeviceAddress = this.handleChangeDeviceAddress.bind(this);
	  this.onFileUploadHandler = this.onFileUploadHandler.bind(this);
	  this.handleModbusTypeLegend = this.handleModbusTypeLegend.bind(this);
	  this.prepareBoxElement = this.prepareBoxElement.bind(this);
	  this.handleBoxType = this.handleBoxType.bind(this);
	  this.handleBitType = this.handleBitType.bind(this);
	  this.handleVarType = this.handleVarType.bind(this);
	  this.handleRegisterGroup = this.handleRegisterGroup.bind(this);
	  this.handleXMLData = this.handleXMLData.bind(this);
	  this.handleCommaFloatType = this.handleCommaFloatType.bind(this);

    }


	onFileUploadHandler = event => {
			const reader = new FileReader()
			reader.readAsText(event.target.files[0])
	    	reader.onloadend = evt => {
		      const readerData = evt.target.result
		      const parser = new DOMParser()
		      const xmlStr = parser.parseFromString(readerData, "text/xml")
		      var XMLParser = require("react-xml-parser")
		      var xml = new XMLParser().parseFromString(
		        new XMLSerializer().serializeToString(xmlStr.documentElement)
		      )
			  this.setState({ deviceName : xml.attributes.deviceName,
							  deviceAddress : xml.attributes.deviceAddress})
					
			  var groupRegistersXmlData = xml.getElementsByTagName('RegisterGroup')
			  var inGroupData = []
			  if (groupRegistersXmlData.length != 0) {
			      groupRegistersXmlData.forEach(e => {
					var xmlDataGroup = e.getElementsByTagName('Register')
					inGroupData = inGroupData.concat(this.handleXMLData(xmlDataGroup, e.attributes.name))
					var groupsToSave = this.state.groupsToSave
					groupsToSave.push(e.attributes.name)
					this.setState({ data : inGroupData,
									groupsToSave: groupsToSave})
				})
				  
			  }
			  
			  var xmlData = xml.getElementsByTagName('Register')
			  var data = this.handleXMLData(xmlData).concat(inGroupData)
			  
			
			this.setState({ error: [], success: null})
			//xml validation 
			var groupValidationMap = {}
			data.forEach(element => {
				
			});
			console.log("DATA", data)
			data.forEach((element, index) => {
				if (element.registerGroup != undefined) {				
					if((index != data.length -1) && (data[index + 1].registerGroup != undefined) && (element.address + element.count != data[index + 1].address)) {
						this.setState( prevState => ({ error: [...prevState.error, "Регистры, состоящие в одной группе должны идти друг за другом."]}))
					} 
					if (groupValidationMap[element.registerGroup] != undefined && (groupValidationMap[element.registerGroup] != element.group)) {
						this.setState( prevState => ({ error: [...prevState.error, "Регистры, состоящие в одной группе не должны принадлежать разным вкладкам."]}))
					} else {
						groupValidationMap[element.registerGroup] = element.group
					}				
				}
				if (+element.max < +element.min) this.setState( prevState => ({ error: [...prevState.error, "Ошибка в регистре " + element.address + " Максимальное значение не может быть меньше минимального"]}));
				if (!Number.isInteger(+element.address)) this.setState( prevState => ({ error: [...prevState.error, "Ошибка в регистре " + element.address + " Адрес должен быть целым числом"]}));
				if (!Number.isInteger(+element.count)) this.setState( prevState => ({ error: [...prevState.error, "Ошибка в регистре " + element.address + " Количество регистров должно быть целым числом"]}));
				if (!Number.isInteger(+element.maxValue)) this.setState( prevState => ({ error: [...prevState.error, "Ошибка в регистре " + element.address + " Максимальное значение должно быть целым числом"]}));
				if (!Number.isInteger(+element.minValue)) this.setState( prevState => ({ error: [...prevState.error, "Ошибка в регистре " + element.address + " Минимальное значение должно быть целым числом"]}));
				if ((element.isWrite != "false" && element.isWrite != "true") || (element.isRead != "false" && element.isRead != "true"))
					this.setState( prevState => ({ error: [...prevState.error, "Ошибка в регистре " + element.address + " Допустимые значения чтения и записи: true, false"]}));
			});
			
			if (this.state.error.length == 0) this.setState({ error: null})
			this.setState({ data : data})
    	};
	}
	
	handleXMLData(xmlData, registerGroup) {
		var data = [];
			  xmlData.forEach(element => {
				var dataElement = {};
				element.children.forEach(e =>  {				
					switch(e.name) {
						case "Name":
							dataElement.name = e.value;
							break;
						case "Address":
							dataElement.address = Number(e.value);
							break;
						case "Count":
							dataElement.count = Number(e.value);
							break;
						case "IsRead":
							dataElement.isRead = e.value;
							break;
						case "IsWrite":
							dataElement.isWrite = e.value;
							break;
						case "Type":
							dataElement.type = e.value; 
							// обработка нестандартных типов
							if (e.value == "Bit" || e.value == "Variable" || e.value == "Box" || e.value == "Multiple" || e.value == "CommaFloat") 
								dataElement.legends = this.handleModbusTypeLegend(element, e.value);
							break;
						case "Multiplier":
							dataElement.multiplier = Number(e.value);
							break;
						case "Suffix":
							dataElement.suffix = e.value;
							break;
						case "Min":
							dataElement.minValue = Number(e.value);
							break;
						case "Max":
							dataElement.maxValue = Number(e.value);
							break;
						case "Group":
							dataElement.group = e.value == "" ? "Без группы" : e.value;
							break;
					}
				})
				if (registerGroup != undefined) dataElement.registerGroup = registerGroup
				if (this.state.data.find(e => { return e.address === dataElement.address}) == undefined) data.push(dataElement)			
			})
			return data
	}
	
	handleRegisterGroup() {
		
	}
	
	handleModbusTypeLegend(legend, value) {
		var legends = {};
		if (value == "Variable") {
			return this.handleVarType(legend)
		} else if (value == "Bit") {
			return this.handleBitType(legend)
		} else if (value == "Box") {
			return this.handleBoxType(legend)			
		} else if (value == "CommaFloat") {
			return this.handleCommaFloatType(legend)
		} else if (value == "Multiple") {		
			legends = legend.children.find(obj => {
			  return obj.name == "Multiple"
			})
			var legend = {}
			legend.isASCII = JSON.parse(legends.attributes.isASCII.toLowerCase());
			if (legend.isASCII) {
				
			} else {
				legend.single = legends.attributes.single
				if (legend.single == "Variable") {
					legend.legend = this.handleVarType(legends.children[0])	
				} else if (legend.single == "Bit") {
					legend.legend = this.handleBitType(legends.children[0], true)
				} else if (legend.single == "Box") {
					legend.legend = this.handleBoxType(legends.children[0])			
				} else if (legend.single == "CommaFloat") {
					legend.legend = this.handleCommaFloatType(legends.children[0], true)			
				} 
			}	
			return legend
		}
	}
	
	handleCommaFloatType(legend, fromMultiple) {
		var legends = {}
		if (fromMultiple) {
			legends = legend.attributes
		} else {
			legends = legend.children.find(obj => {
			  return obj.name == "CommaFloat"
			})
			legends = legends.attributes
		}
		return legends
	}
	
	handleBoxType(legend, fromMultiple) {
		var legends = {}
		legend.first = legend.children.find(obj => {
			  return obj.name == "First"
			})
			legend.second = legend.children.find(obj => {
			  return obj.name == "Second"
			})
			
			if (legend.first.attributes.type == "Bit" || legend.first.attributes.type == "Variable") {
				legends.first = {}
				legends.first.content = this.prepareBoxElement(legend.first.children[0], legend.first.attributes.type)
				legends.first.type = legend.first.attributes.type
			} else {
				legends.first = {}
				legends.first.type = legend.first.attributes.type
			}
			
			if (legend.second.attributes.type == "Bit" || legend.second.attributes.type == "Variable") {
				legends.second = {}
				legends.second.content = this.prepareBoxElement(legend.second.children[0], legend.second.attributes.type)
				legends.second.type = legend.second.attributes.type
			} else {
				legends.second = {}
				legends.second.type = legend.second.attributes.type
			}
			return legends
	}
	
	handleBitType(legend, fromMultiple) {
		var legends = {}
		if (fromMultiple) {
			legends = legend
		} else {
			legends = legend.children.find(obj => {
			  return obj.name == "Bits"
			})
		}		
			var bits = []
			legends.children.forEach(e => {
				var possibleValues = [];
				e.children.forEach(possibleValue => possibleValues.push(possibleValue.value))
				var bit = {
		            startBit: e.attributes.start,
		           	bitQuantity: e.attributes.quantity,
					description: e.attributes.bitName,
					possibleValues: possibleValues,
		        }
				bits.push(bit)
			})
			return bits;
	}
	
	handleVarType(legend, fromMultiple) {		
		var legends = {}
		if (fromMultiple) {
			legends = legend
		} else {
			legends = legend.children.find(obj => {
			  return obj.name == "Vars"
			})
		}
			var variables = []
			legends.children.forEach(e => {
				var variable = {			
					description: e.value,
					value : e.attributes.value,
				}
				variables.push(variable)
			})
			return variables;
	}
	
	prepareBoxElement(legend, type) {	// преобразование аргумента в arg[0] для handleModbusTypeLegend
		var result = []
		if (type == "Bit") {
			legend.children.forEach((e) => {
				var description = []
				e.children.forEach((el) => {
					description.push(el.value)			
				})
				var curr = {	
					startBit: e.attributes.start,
					bitQuantity: e.attributes.quantity,
					description: e.attributes.bitName,
					possibleValues: description,
					type: "bitType"
				}
				result.push(curr)	
			})
			return result
		} else if (type == "Variable") {
			legend.children.forEach((e) => {
				var curr = {			
					description: e.value,
					value : e.attributes.value,
					type: "varType"
				}
				result.push(curr)
			})
			return result
		}		
	}

  	handleClick() {    
		var data = this.state.data;
		this.setState({ loading: true });
		console.log("DEVICE: ", this.state.deviceName);
		
		let device = {
            name: this.state.deviceName,
           	address: this.state.deviceAddress
        }

		var groupData = []
		this.state.groupsToSave.forEach(e => groupData.push({name: e}))

		GroupRegistersService.save(groupData)
			.then(
				(response) => {
						console.log("group response data: ", response.data);
						data.forEach(element => {
							response.data.forEach(e => {
								if (element.registerGroup == e.name) {
									element.registerGroup = e.id
								}
							})
						})
						console.log("data after group add: ", data);
						
						DeviceService.save(device)
						.then(
			                (request) => {
								console.log("data before load: ", data);
								data.forEach(element => {
									element.device = request.data;
									if (element.legends != null) element.legends = JSON.stringify(element.legends);
								})
			                    LoadRegistersService.load(data)
											.then(
								                () => {
													data.forEach(element => {
														element.device = request.data;
														if (element.legends != null) element.legends = JSON.parse(element.legends);
													})
								                    this.setState( prevState => ({ success: ["Данные успешно загружены"], loading: false}));
								                }
								            )
											.catch((err) => {
												  console.log("ERROR: ", err);
													this.setState( prevState => ({ error: ["Ошибка загрузки данных"], loading: false}));
											  });
								}
            )
			.catch((err) => {
				  console.log("ERROR: ", err);
					this.setState( prevState => ({ error: ["Ошибка загрузки данных"], loading: false}));
			  });
						
				}) 
			.catch((err) => {
							  console.log("ERROR: ", err);
								this.setState( prevState => ({ error: ["Ошибка добавления новой группы"], loading: false}));
						  });
				
		
	}
	
	renderErrors() {
      return this.state.error.map((e) => {
         return (
            <p>
				{e}
            </p>
         )
      })
   }
	
	
	renderTableData() {
      return this.state.data.map((current, index) => {
         const { name, address, count, isRead, isWrite, type, multiplier, suffix, minValue, maxValue, group, legends } = current;
		var legendStrings = []
		if (type == "Variable") {
			legends.forEach(e => {
				var str = `${e.description} : ${e.value} \n`
				legendStrings.push(str)
			})
		} else if (type == "Bit") {
			legends.forEach(e => {
				var end = Number(e.startBit) + Number(e.bitQuantity) - 1;
				var str = `${e.description} (биты ${e.startBit} - ${end}) : ${e.possibleValues} \n`
				legendStrings.push(str)
			})
		}
         return (
            <tr key={index}>
               <td>{name}</td>
               <td>{address}</td>
			   <td>{legendStrings}</td>
               <td>{count}</td>
               <td>{String(isRead)}</td>
			   <td>{String(isWrite)}</td>
               <td>{type}</td>
               <td>{multiplier}</td>
               <td>{suffix}</td>
			   <td>{minValue}</td>
               <td>{maxValue}</td>
			   <td>{group}</td>
            </tr>
         )
      })
   }
	
	handleChangeDeviceName(e) {
        this.setState({ deviceName: e.target.value });
    }

	handleChangeDeviceAddress(e) {
		if (e.target.value < 0 || e.target.value > 247) {
			this.setState( { error: []});
			this.setState( prevState => ({ error: [...prevState.error, "Значение адреса может быть от 0 до 247"]}));
		} else {
			this.setState( { error: null});
			this.setState({ deviceAddress: e.target.value });
		}    	
    }


	
	
	  render() {
		
		const {data, error, loading, success} = this.state;
		
	    return (
		<div>
			{error &&
                        <div className={'alert alert-danger'}>{this.renderErrors()}</div>
                    }
			
			{success &&
                        <div className={'alert alert-success'}>{this.state.success}</div>
                    }

			<label>
          		Название устройства:
          	<input type="text" value={this.state.deviceName} onChange={this.handleChangeDeviceName} />
			</label>
			
			<label>
          		Адрес устройства:
          	<input type="number" value={this.state.deviceAddress} onChange={this.handleChangeDeviceAddress} />
			</label>
			
			<label>
          		Загрузите файл
          	 <input type="file" name="file" onChange={this.onFileUploadHandler}/>
			</label>

			<table border="1">
			   <caption>Таблица регистров</caption>
			   <tr>
				<th>Название</th>
			    <th>Адрес</th>
				<th>Описание</th>
			    <th>Количество</th>
			    <th>Чтение</th>
			    <th>Запись</th>
				<th>Тип</th>
			    <th>Множитель</th>
			    <th>Суффикс</th>
				<th>Мин.</th>
				<th>Макс.</th>
				<th>Группа</th>
			   </tr>

				<tbody>
				{this.renderTableData()}
				</tbody>

			</table>
			
			<div className="form-group">
                        <button className="btn btn-primary" onClick={this.handleClick} disabled={error || (data.length==0)}>Добавить устройство</button>
                        {loading &&
                            <img src={Strings.LOADING} />
                        }
                    </div>

		</div>
	    )
	  }
	
}

export default LoadRegistersComponent