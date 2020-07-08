import React, { Component } from 'react';

class SpecialModbusTypesComponent extends Component {
	
	constructor(props) {
        super(props);

		this.state = {
					targetType: this.props.targetType,
					data: [],
					loading: false,					
		        }

		this.renderVariable = this.renderVariable.bind(this);
		this.renderByte = this.renderByte.bind(this);
		this.renderByteElements = this.renderByteElements.bind(this);
		this.renderVarElements = this.renderVarElements.bind(this);
		this.renderPossibleValues = this.renderPossibleValues.bind(this);

    }


	renderTargetModbusTypeForm() {
		if (this.state.targetType == "Variable") {
			return(
				<div>
					{this.renderVariable()}
				</div>
			)
		} else if (this.state.targetType == "Bit") {
			return(
				<div>
					{this.renderByte()}
				</div>
			)
		} else {
			return(
				<p>
					Для данного типа нет описания
				</p>
			)
		}
	}
	
	renderByte() {	
		return (
			<table border="1">
			   <tr>
				<th>Название</th>
				<th>Начальный бит</th>
				<th>Количество бит</th>
				<th>Возможные значения</th>
			   </tr>

				<tbody>
				{this.renderByteElements()}
				</tbody>

			</table>
		)
	}
	
	renderByteElements() {
		if (this.state.data.length != 0) {
			return this.state.data.map((current, index) => {
	         const { startBit, bitQuantity, description, possibleValues } = current;
	         return (
	            <tr>
					<td>{description}</td>
					<td>{startBit}</td>
					<td>{bitQuantity}</td>
					<td>{this.renderPossibleValues(possibleValues)}</td>
				</tr>
	         )
	      })
		} else {
			return(
			<tr>
				<td><input type="text" placeholder="Название" className="form-control" onChange={(event) => {}}/></td>
				<td><input type="text" placeholder="Начальный бит" className="form-control" onChange={(event) => {}}/></td>
				<td><input type="text" placeholder="Количество бит" className="form-control" onChange={(event) => {}}/></td>
				<td><input type="text" placeholder="Возможные значения" className="form-control" onChange={(event) => {}}/></td>
			</tr>
			)
		}
		
	}
	
	renderPossibleValues(possibleValues) {
		return possibleValues.map((current, index) => {
         return (
            <input type="text" className="form-control" defaultValue={current} onChange={(event) => {}}/>
         )
      })
	}
	
	renderVariable() {
		return (
			<table border="1">
			   <tr>
				<th>Описание</th>
				<th>Значение</th>
			   </tr>

				<tbody>
				{this.renderVarElements()}
				</tbody>

			</table>
		)
	}
	
	renderVarElements() {
		if (this.state.data.length != 0) {
			return this.state.data.map((current, index) => {
	         const {description, value } = current;
	         return (
	            <tr>
					<td>{description}</td>
					<td>{value}</td>
				</tr>
	         )
	      })
		} else {
			return(
				<tr>
					<td><input type="text" placeholder="Описание" className="form-control" onChange={(event) => {}}/></td>
					<td><input type="text" placeholder="Значение" className="form-control" onChange={(event) => {}}/></td>
				</tr>
			)
		}
		
	}


	render() {	
	  return (
		<div>
			{this.renderTargetModbusTypeForm()}
		</div>
	    )
	  }


}

export default SpecialModbusTypesComponent