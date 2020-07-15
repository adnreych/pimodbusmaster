import React, { Component } from 'react';
import AddIcon from '@material-ui/icons/Add';
import ClearIcon from '@material-ui/icons/Clear';
import SaveIcon from '@material-ui/icons/Save';
import { green, red } from '@material-ui/core/colors';
import _times from 'lodash/times';
import _isEmpty from 'lodash/isEmpty';
import _compact from 'lodash/compact';
import _remove from 'lodash/remove';


class SpecialModbusTypesComponent extends Component {
	
	constructor(props) {
        super(props);

		this.state = {
					targetType: this.props.targetType,
					index: this.props.index,
					data: [{}],
					loading: false,	
					error: null,	
		        }

		this.renderVariable = this.renderVariable.bind(this);
		this.renderByte = this.renderByte.bind(this);
		this.renderByteElements = this.renderByteElements.bind(this);
		this.renderVarElements = this.renderVarElements.bind(this);
		this.renderPossibleValues = this.renderPossibleValues.bind(this);
		this.handleChangeByte = this.handleChangeByte.bind(this);
		this.handleAddDelete = this.handleAddDelete.bind(this);
		this.handleSave = this.handleSave.bind(this);
		this.checkEmptyFields = this.checkEmptyFields.bind(this);

    }

	componentDidMount() {
		var data = JSON.parse(this.props.data)
		if (data != null) this.setState({data : data})
    }


	handleSave() {
		if (this.checkEmptyFields()) {
			var data = _compact(this.state.data)
			data = _remove(data, function(n) {
			  return !_isEmpty(n);
			});
			data.index = this.state.index
			this.props.callbackFromParent(data);
		} 
		console.log("this.state ",this.state)	
	}
	
	
	checkEmptyFields() {
		this.setState({error: null})
		if (this.state.targetType == "Variable") {
			this.state.data.forEach((e) => {
				console.log("checkVAR ", e)
				if (e.description == "" || e.value == "") this.setState({error: "Все поля должны быть заполнены!"})
			})
		} else if (this.state.targetType == "Bit") {
			this.state.data.forEach((e) => {
				console.log("checkBIT ", e)
				if (e.description == "" || e.startBit == "" || e.bitQuantity == "" || _isEmpty(e.possibleValues)) this.setState({error: "Все поля должны быть заполнены!"})
			})
		}
		if (this.state.erorr == null) {
			return true
		} else {
			return false
		}
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
		var data = this.state.data	
		console.log("DATABEFORE", data)
		if (field != "possibleValues") {
			data[index][field] = event.target.value;
		} else {
			data[index][field] = event.target.value.split(",");
			_compact(data[index][field])
		}
		console.log("DATAAFTER", data)
		this.setState({ data: data });	
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
				<th>Возможные значения (через запятую)</th>
			   </tr>

				<tbody>
				{this.renderByteElements()}		
				<button className="btn btn-primary" onClick={() => {this.handleAddDelete(true)}} ><AddIcon style={{ color: green[500] }} /></button>
				<button className="btn btn-primary" onClick={() => {this.handleAddDelete(false)}} ><ClearIcon style={{ color: red[500] }} /></button>
				<button className="btn btn-primary" onClick={() => {this.handleSave()}} ><SaveIcon/></button>
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
							<td>{this.renderPossibleValues(possibleValues, index)}</td>
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
	
	
	renderPossibleValues(possibleValues, index) {
		if (possibleValues != null && possibleValues.length != 0) {
			var possibleValuesStr = ""
			possibleValues.forEach((current) => {
	         	possibleValuesStr = possibleValuesStr + "," + current
	      	})
			return (
	            <input type="text" className="form-control" defaultValue={possibleValuesStr} onChange={(event) => this.handleChangeByte(event, "possibleValues", index)}/>
	         )
		} else {
			return (
	            <input type="text" className="form-control" onChange={(event) => this.handleChangeByte(event, "possibleValues", index)}/>
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
				<button className="btn btn-primary" onClick={() => {this.handleSave()}} ><SaveIcon/></button>
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
		const {error} = this.state
		  return (
			<div>
				{error &&
		                        <div className={'alert alert-danger'}>{this.state.error}</div>
		                    }
				{this.renderTargetModbusTypeForm()}
			</div>
		    )
		  }


}

export default SpecialModbusTypesComponent