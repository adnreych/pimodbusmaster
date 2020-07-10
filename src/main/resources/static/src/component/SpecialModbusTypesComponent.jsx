import React, { Component } from 'react';
import AddIcon from '@material-ui/icons/Add';
import ClearIcon from '@material-ui/icons/Clear';
import { green, red } from '@material-ui/core/colors';
import _times from 'lodash/times';
import _isEmpty from 'lodash/isEmpty';


class SpecialModbusTypesComponent extends Component {
	
	constructor(props) {
        super(props);

		this.state = {
					targetType: this.props.targetType,
					data: [{}],
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

	componentDidMount() {
		var data = JSON.parse(this.props.data)
		if (data != null) this.setState({data : data})
    }

	handleAddDelete(add) {
		if (add) {
			var data = this.state.data
			data.push({})
			this.setState({data: data}) 
		} else if (!add && (this.state.data.length > 1)) {
			var data = this.state.data
			data.pop()
			this.setState({data: data}) 
		}
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
		if (this.state.data.length != 0) {
			return this.state.data.map((current, index) => {
				if (!_isEmpty(current)) {
					const { startBit, bitQuantity, description, possibleValues } = current;
			         return (
			            <tr>
							<td><input type="text" className="form-control" defaultValue={description} onChange={(event) => this.handleChangeByte(event, "description", index)}/></td>
							<td><input type="text" className="form-control" defaultValue={startBit} onChange={(event) => this.handleChangeByte(event, "startBit", index)}/></td>
							<td><input type="text" className="form-control" defaultValue={bitQuantity} onChange={(event) => this.handleChangeByte(event, "bitQuantity", index)}/></td>
							<td>{this.renderPossibleValues(possibleValues)}</td>
						</tr>
			         )
				} else {
					return(
						<tr>
							<td><input type="text" placeholder="Название" className="form-control" onChange={(event) => this.handleChangeByte(event, "description", index)}/></td>
							<td><input type="text" placeholder="Начальный бит" className="form-control" onChange={(event) => this.handleChangeByte(event, "startBit", index)}/></td>
							<td><input type="text" placeholder="Количество бит" className="form-control" onChange={(event) => this.handleChangeByte(event, "bitQuantity", index)}/></td>
							<td><input type="text" placeholder="Возможные значения (через запятую)" className="form-control" onChange={(event) => this.handleChangeByte(event, "possibleValues", index)}/></td>
						</tr>
					)
				}
	         
	      })
		} 
		
	}
	
	
	renderPossibleValues(possibleValues) {
		if (possibleValues != null && possibleValues.length != 0) {
			return possibleValues.map((current) => {
	         return (
	            <input type="text" className="form-control" defaultValue={current} onChange={(event) => {}}/>
	         )
	      })
		} else {
			return (
	            <input type="text" className="form-control" onChange={(event) => {}}/>
	         )
		}
		
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
		if (this.state.data.length != 0) {
			return this.state.data.map((current, index) => {
				if (!_isEmpty(current)) {
					const { description, value } = current;
			         return (
			            <tr>
							<td><input type="text" className="form-control" defaultValue={description} onChange={(event) => this.handleChangeByte(event, "description", index)}/></td>
							<td><input type="text" className="form-control" defaultValue={value} onChange={(event) => this.handleChangeByte(event, "value", index)}/></td>	
						</tr>
			         )
				} else {
					return(
						<tr>
							<td><input type="text" placeholder="Описание" className="form-control" onChange={(event) => this.handleChangeByte(event, "description", this.state.data.length)}/></td>
							<td><input type="text" placeholder="Значение" className="form-control" onChange={(event) => this.handleChangeByte(event, "value", this.state.data.length)}/></td>
					</tr>
					)
				}
	         
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