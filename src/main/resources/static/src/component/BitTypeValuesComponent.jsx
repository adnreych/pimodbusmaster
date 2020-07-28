import React, { Component } from 'react';
import * as Strings from '../helpers/strings';
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
					currValue: this.props.value != undefined ? this.props.value : null
		        }

		this.handleChangeCurrentValue = this.handleChangeCurrentValue.bind(this);

    }

	componentWillMount() {
		if (this.state.currValue == null) {
			var currValue = new Array(this.state.legends.length)
			_times(this.state.legends.length, (i) => {currValue[i] = this.state.legends[i].possibleValues[0]})
			this.setState({ currValue: currValue })
		} 	
	}


	handleChangeCurrentValue(event, index) {
		var value = event.target.value
		var currValue = this.state.currValue
		currValue[index] = value
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
		  return this.state.legends.map((current, index) => {
			return (
				<>
					<div>{current.description}</div>
						<Select 
							name="input" 
							defaultValue={this.state.currValue[index]}
							onChange={(event) => this.handleChangeCurrentValue(event)} > 
								{this.renderPossibleValues(current.possibleValues)}					
				    	</Select>
				</>
			)
		})
	}

}

export default BitTypeValuesComponent