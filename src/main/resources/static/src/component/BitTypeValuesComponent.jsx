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
	


	

	handleChangeCurrentValue(event, current) {
		var value = event.target.value
		var currValue = this.state.currValue
		currValue[current.description] = current.possibleValues[value]
		this.setState({ currValue: currValue })
		this.props.callbackFromParent(currValue, this.state.index)
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
			if (this.state.currValue == null || this.state.currValue[current.description] == null) {
				var currValue = this.state.currValue
				currValue[current.description] = current.possibleValues[0]
				this.setState({ currValue : currValue })
			}
			return (
				<>
					<div>{current.description}</div>
						<Select 
							name="input" 
							value={current.possibleValues.indexOf(this.state.currValue[current.description])}
							onChange={(event) => this.handleChangeCurrentValue(event, current)} > 
								{this.renderPossibleValues(current.possibleValues)}					
				    	</Select>
				</>
			)
		})
	}

}

export default BitTypeValuesComponent