import React, { Component } from 'react'; 
import _times from 'lodash/times';

class MultipleTypeComponent extends Component {
	
	constructor(props) {
        super(props);

		this.state = {
					loading: false,
					count: 0,
					registerInfo: {},
					isASCII: false,
					single: null,
					legend: null,					
		        }

		this.renderRegisterInfo = this.renderRegisterInfo.bind(this);

    }


	renderMultipleType() {
		if (count == 0) {
			{this.renderRegisterInfo(0)}
		} else {
			_times(this.state.count, (index) => {
				{this.renderRegisterInfo(index)}			
			})
		}
		
	}
	
	renderRegisterInfo(index) {
		const {id, name, address, count, isRead, isWrite, type, multiplier, suffix, minValue, maxValue, legends} = this.state.registerInfo;
			return (
				 <tr key={index}>
	               <td>{name} {index + 1}</td>
	               <td>{address}</td>
	               <td>{count}</td>
	               <td>{String(isRead)}</td>
				   <td>{String(isWrite)}</td>
	               <td>{type}</td>
				   <td>{(type=="Variable" || type=="Bit") && <ReactTextCollapse options={TEXT_COLLAPSE_OPTIONS}>
							{this.renderDescriptionSpecialTypes(type, legends)}
						</ReactTextCollapse>}
						
						{(type=="Box") && <ReactTextCollapse options={TEXT_COLLAPSE_OPTIONS}>
							{(boxLegends.first.type === "Variable" || boxLegends.first.type === "Bit") 
								&& <>{this.renderDescriptionSpecialTypes(boxLegends.first.type, JSON.stringify(boxLegends.first.content))}</>}
							{(boxLegends.second.type === "Variable" || boxLegends.second.type === "Bit") 
								&& <><hr />{this.renderDescriptionSpecialTypes(boxLegends.second.type, JSON.stringify(boxLegends.second.content))}</>}
						</ReactTextCollapse>}
						
						{(type!="Variable" && type!="Bit" && type!="Box" && (multiplier != null)) && <p class="small-text">*{multiplier}</p>}
						{(type!="Variable" && type!="Bit" && type!="Box" && (suffix != "")) && <p class="small-text">Единица измерения: {suffix}</p>}		
						{(type!="Variable" && type!="Bit" && type!="Box" && (minValue != null && maxValue != null)) && <p class="small-text">От {minValue} до {maxValue}</p>}
						
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
								          								
	            </tr>
			)
	}


	render() {	
		  return (
			<div>
				
			</div>
		)
	}

}

export default MultipleTypeComponent