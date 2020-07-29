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

    }

	 static getDerivedStateFromProps(props, state) {	
		if (state.currValue.length == 0) {
				state.currValue = new Array(state.legends.length).fill({})
				console.log("STATE1", state)
				return state;
		}
		
		if (props.value !== undefined && state.currValue !== props.value) {
				state.currValue = props.value
				console.log("STATE2", state)
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
		console.log("currValue", this.state.currValue)
		  return this.state.legends.map((current, index) => {
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