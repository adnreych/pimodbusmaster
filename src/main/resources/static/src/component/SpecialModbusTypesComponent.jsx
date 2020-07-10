import React, { Component } from 'react';
import AddIcon from '@material-ui/icons/Add';
import ClearIcon from '@material-ui/icons/Clear';
import { green, red } from '@material-ui/core/colors';
import times from 'lodash/times';


class SpecialModbusTypesComponent extends Component {
	
	constructor(props) {
        super(props);

		this.state = {
					targetType: this.props.targetType,
					data: JSON.parse(this.props.data),
					loading: false,	
					emptyCellsCount: 1,			
		        }

		this.renderVariable = this.renderVariable.bind(this);
		this.renderByte = this.renderByte.bind(this);
		this.renderByteElements = this.renderByteElements.bind(this);
		this.renderVarElements = this.renderVarElements.bind(this);
		this.renderPossibleValues = this.renderPossibleValues.bind(this);
		this.handleChangeByte = this.handleChangeByte.bind(this);
		this.handleAddDelete = this.handleAddDelete.bind(this);

    }

	handleAddDelete(add) {
		add ? this.setState({emptyCellsCount : this.state.emptyCellsCount + 1}) : this.setState({emptyCellsCount : this.state.emptyCellsCount - 1})
	}

	handleChangeByte(event, field, index) {
		console.log("DATABEFORE", this.state.data)
		var data = this.state.data
		data[index][field] = event.target.value;
		this.setState({ data: data });	
		console.log("DATAAFTER", this.state.data)
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
				<button className="btn btn-primary" onClick={() => {this.handleAddDelete(true)}} ><AddIcon style={{ color: green[500] }} /></button>
				<button className="btn btn-primary" onClick={() => {this.handleAddDelete(false)}} ><ClearIcon style={{ color: red[500] }} /></button>
				</tbody>

			</table>
		)
	}
	
	renderByteElements() {
		if (this.state.data != null) {
			return this.state.data.map((current, index) => {
	         const { startBit, bitQuantity, description, possibleValues } = current;
	         return (
	            <tr>
					<td><input type="text" className="form-control" defaultValue={description} onChange={(event) => this.handleChangeByte(event, "description", index)}/></td>
					<td><input type="text" className="form-control" defaultValue={startBit} onChange={(event) => this.handleChangeByte(event, "startBit", index)}/></td>
					<td><input type="text" className="form-control" defaultValue={bitQuantity} onChange={(event) => this.handleChangeByte(event, "bitQuantity", index)}/></td>
					<td>{this.renderPossibleValues(possibleValues)}</td>
				</tr>
	         )
	      })
		} else {
			let emptyCells = [];
			times(this.state.emptyCellsCount, () => {
			  emptyCells.push(
					<tr>
						<td><input type="text" placeholder="Название" className="form-control" onChange={(event) => this.handleChangeByte(event, "description", 0)}/></td>
						<td><input type="text" placeholder="Начальный бит" className="form-control" onChange={(event) => this.handleChangeByte(event, "startBit", 0)}/></td>
						<td><input type="text" placeholder="Количество бит" className="form-control" onChange={(event) => this.handleChangeByte(event, "bitQuantity", 0)}/></td>
						<td><input type="text" placeholder="Возможные значения (через запятую)" className="form-control" onChange={(event) => this.handleChangeByte(event, "possibleValues", 0)}/></td>
					</tr>);
			});
			console.log("emptyCells", emptyCells)
			return emptyCells.map((e) => {
				return(
					e
			)
			})
			
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
				<button className="btn btn-primary" onClick={() => {this.handleAddDelete(true)}} ><AddIcon style={{ color: green[500] }} /></button>
				<button className="btn btn-primary" onClick={() => {this.handleAddDelete(false)}} ><ClearIcon style={{ color: red[500] }} /></button>
				</tbody>

			</table>
		)
	}
	
	renderVarElements() {
		if (this.state.data != null) {
			return this.state.data.map((current, index) => {
	         const {description, value } = current;
	         return (
		            <tr>
						<td><input type="text" className="form-control" defaultValue={description} onChange={(event) => this.handleChangeByte(event, "description", index)}/></td>
						<td><input type="text" className="form-control" defaultValue={value} onChange={(event) => this.handleChangeByte(event, "value", index)}/></td>				        				      
					</tr>
	         )
	      })
		} else {
			return this.state.emptyCellsCount.map(() => {
				return(
					<tr>
						<td><input type="text" placeholder="Описание" className="form-control" onChange={(event) => this.handleChangeByte(event, "description", 0)}/></td>
						<td><input type="text" placeholder="Значение" className="form-control" onChange={(event) => this.handleChangeByte(event, "value", 0)}/></td>
					</tr>
				)
			})
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