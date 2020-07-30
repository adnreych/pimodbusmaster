import React, { Component } from 'react';
import 'react-confirm-alert/src/react-confirm-alert.css'; 
import Option from 'muicss/lib/react/option';
import Select from 'muicss/lib/react/select';
import _times from 'lodash/times';



class BitTypeValuesComponent extends Component {
	
	constructor(props) {
        super(props);

		this.state = {
					loading: false,
					legends: JSON.parse(this.props.legends),
					index: this.props.index,
					currValue: []
		        }

		this.handleChangeCurrentValue = this.handleChangeCurrentValue.bind(this);
		this.prepareValueToWrite = this.prepareValueToWrite.bind(this);

    }

	 static getDerivedStateFromProps(props, state) {	
		if (state.currValue.length == 0) {
				state.currValue = new Array(state.legends.length).fill({})
				return state;
		}
		
		if (props.value !== undefined && state.currValue !== props.value) {
				state.currValue = props.value
			    return state;
			}
			
	    return null;   
	}
	
	prepareValueToWrite(legends, currVal) {
		var result = "";
		
		legends.forEach((e) => {
			var bitQuantity = e.bitQuantity
			console.log("bitQuantity", bitQuantity)
			var i = e.possibleValues.indexOf(currVal[e.description]).toString(2)
			console.log("i before", i)
			if (i.length < bitQuantity) {
				i = i.padStart((bitQuantity - i.length) + i.length, "0")
			}
			console.log("i after", i)
			result = result + String(i);
			console.log("result", result)
		})
		
		var cv = this.state.currValue.strToWrite = result
		this.setState({ currValue: cv })
		return result;

	}

	

	handleChangeCurrentValue(event, current) {
		var value = event.target.value
		var currValue = this.state.currValue
		currValue[current.description] = current.possibleValues[value]
		this.setState({ currValue: currValue })
		this.props.callbackFromParent(currValue, this.state.index)
		console.log("STR TO WRITE", this.prepareValueToWrite(this.state.legends, this.state.currValue))
	}

	renderPossibleValues(values) {
		return values.map((value, index) => {
					return (
						<Option value={index} label={value} />
						)
				})
	}

	render() {	
		  return this.state.legends.map((current) => {
			return (
				<>
					<div>{current.description}</div>
						<Select 
							name="input" 
							value={this.state.currValue != null ? 
								current.possibleValues.indexOf(this.state.currValue[current.description]) : current.possibleValues.indexOf(current.possibleValues[0])}
							onChange={(event) => this.handleChangeCurrentValue(event, current)} > 
								{this.renderPossibleValues(current.possibleValues)}					
				    	</Select>
				</>
			)
		})
	}

}

export default BitTypeValuesComponent