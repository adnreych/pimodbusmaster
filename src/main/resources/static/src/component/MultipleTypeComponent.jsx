import React, { Component } from 'react';
import _times from 'lodash/times';
import BitTypeValuesComponent from './BitTypeValuesComponent';
import BoxTypeComponent from './BoxTypeComponent';

class MultipleTypeComponent extends Component {
	
	constructor(props) {
        super(props);

		var legends = JSON.parse(this.props.registerInfo.legends)

		this.state = {
					loading: false,
					count: this.props.count,
					registerInfo: this.props.registerInfo,
					isASCII: legends.isASCII,
					inputValues: this.props.inputValues,
					single: legends.single == undefined ? "String" : legends.single,
					legend: null,		
					index: this.props.index
		        }

		this.renderRegisterInfo = this.renderRegisterInfo.bind(this);

    }


	renderRegisterInfo() {}



	render() {	
		console.log("MULTIPLESTATE", this.state)
		if (this.state.isASCII) {
			return(
				<div>
					{this.renderRegisterInfo(0)}
				</div>
			)
		} else {
			return _times(this.state.count, (index) => {
				const {id, name, address, isRead, isWrite, multiplier, suffix, minValue, maxValue} = this.state.registerInfo;
				var legends = JSON.parse(this.props.registerInfo.legends).legend
				var type = this.state.single
				var count = type=="Float" ? 2 : 1
					return(
					<tr>
			               <td>{name} {index + 1}</td>
			               <td>{address + index}</td>	
							<td>{count}</td>
							<td>{String(isRead)}</td>
							<td>{String(isWrite)}</td>	
							<td>{type}</td>	
							<td></td>	
 	
												
							<td>
									{(type=="Bit") && <BitTypeValuesComponent 
										index={index} 
										legends={JSON.stringify(legends)} 
										callbackFromParent={this.props.callbackFromBitType} 
										value={this.state.inputValues[index]} 
										/>}
										
									{(type=="Box") && <BoxTypeComponent 
										index={index} 
										pair={JSON.stringify(legends)} 
										callbackFromParent={this.props.callbackFromBitType} 
										value={this.state.inputValues[index]} 
										/>}
									
									{(type!="Bit" && type!="Box") && 
									<input type="text" placeholder="Значение" 
									value={this.state.inputValues[index]} 
									ref={index}
									onChange={(event) => this.handleChange(event, index)} />}
							</td>	
							<td></td>	
							{(index==0) && <td>
								<button className="btn btn-primary" 
									onClick={() => this.props.readClick(address, this.state.count, this.state.index)} 
									disabled={!isRead}>Чтение всей группы</button>
								<button className="btn btn-primary" 
									onClick={() => this.props.writeClick(address, this.state.inputValues, this.state.index)} 
									disabled={!isWrite}>Запись всей группы</button></td>	}	
							{(index==0) && <td>
								<button className="btn btn-primary" 
									onClick={() => this.props.deleteClick(id, index)}>Удалить группу</button></td>	}					          								
	          </tr>
					)					
				})		
			}
	}

}

export default MultipleTypeComponent