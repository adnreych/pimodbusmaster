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
		        }

		this.renderRegisterInfo = this.renderRegisterInfo.bind(this);

    }


	renderMultipleType() {
		console.log("MULTIPLESTATE", this.state)
		if (this.state.isASCII) {
			return(
				<div>
					{this.renderRegisterInfo(0)}
				</div>
			)
		} else {
			return _times(this.state.count, (index) => {
					return(
						<div>
							{this.renderRegisterInfo(index)}
						</div>	
					)					
				})		
			}
		
	}
	
	renderRegisterInfo(index) {
		const {name, address, multiplier, suffix, minValue, maxValue} = this.state.registerInfo;
		var legends = JSON.parse(this.props.registerInfo.legends).legend
		var type = this.state.single
			return (
				 <tr key={index}>
	               <td>{name} {index + 1}</td>
	               <td>{address}</td>
	               <td>{type}</td>				 	
												
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
	            </tr>
			)
	}


	render() {	
		  return (
			<div>
				{this.renderMultipleType()}
			</div>
		)
	}

}

export default MultipleTypeComponent