import React, { Component } from 'react';
import ReactDOM from 'react-dom'
import CSVReader from 'react-csv-reader'
import LoadRegistersService from '../service/LoadRegistersService';
import DeviceService from '../service/DeviceService';
import * as Strings from '../helpers/strings';


class LoadRegistersComponent extends Component {
	
	constructor(props) {
        super(props);

		this.state = {
					data: [],
					fileInfo: [],
					error: null,
					loading: false,
					success: null,
					deviceName: "",
					deviceAddress: ""
		        }

        this.papaparseOptions = {
	    header: true,
	    dynamicTyping: true,
	    skipEmptyLines: true,
	    transformHeader: header =>
	      	{
				header
			        .toLowerCase();
				return this.renameHeader(header)
			}
	  };

	  this.handleClick = this.handleClick.bind(this);
	  this.handleChangeDeviceName = this.handleChangeDeviceName.bind(this);
	  this.handleChangeDeviceAddress = this.handleChangeDeviceAddress.bind(this);

    }

  	handleClick() {    
		var data = this.state.data;
		this.setState({ loading: true });
		console.log("DEVICE: ", this.state.deviceName);
		
		let device = {
            name: this.state.deviceName,
           	address: this.state.deviceAddress
        }

		DeviceService.save(device)
			.then(
	                (request) => {
						data.forEach(element => element.device = request.data)
	                    LoadRegistersService.load(data)
				.then(
	                () => {
	                    this.setState( prevState => ({ success: ["Данные успешно загружены"], loading: false}));
	                }
	            )
				.catch((err) => {
					  console.log("ERROR: ", err);
						this.setState( prevState => ({ error: ["Ошибка загрузки данных"], loading: false}));
				  });
	                }
            )
			.catch((err) => {
				  console.log("ERROR: ", err);
					this.setState( prevState => ({ error: ["Ошибка загрузки данных"], loading: false}));
			  });
		
	}

	fileLoaded = (data, fileInfo) => {
		this.setState({ error: [], success: null});
		//csv validation 
		data.forEach(element => {
			if (element.max < element.min) this.setState( prevState => ({ error: [...prevState.error, "Ошибка в регистре " + element.address + " Максимальное значение не может быть меньше минимального"]}));
			if (!Number.isInteger(element.address)) this.setState( prevState => ({ error: [...prevState.error, "Ошибка в регистре " + element.address + " Адрес должен быть целым числом"]}));
			if (!Number.isInteger(element.count)) this.setState( prevState => ({ error: [...prevState.error, "Ошибка в регистре " + element.address + " Количество регистров должно быть целым числом"]}));
		});
		
		if (this.state.error.length == 0) this.setState({ error: null});
		
		console.log(data, fileInfo);
		this.setState({data: data, fileInfo: fileInfo});
	};
	
	onError = (e) => {
		console.log("ERRORLOADFILE", e);
	};
	
	renderErrors() {
      return this.state.error.map((e) => {
         return (
            <p>
				{e}
            </p>
         )
      })
   }
	
	
	renderTableData() {
      return this.state.data.map((current, index) => {
         const { name, address, count, isRead, isWrite, type, multiplier, suffix, min, max } = current;
		console.log("current", current);
         return (
            <tr key={index}>
               <td>{name}</td>
               <td>{address}</td>
               <td>{count}</td>
               <td>{String(isRead)}</td>
			   <td>{String(isWrite)}</td>
               <td>{type}</td>
               <td>{multiplier}</td>
               <td>{suffix}</td>
			   <td>{min}</td>
               <td>{max}</td>
            </tr>
         )
      })
   }

	renameHeader(headerName) {
		if (headerName.trim().localeCompare("Название") == 0) return "name";
		if (headerName.trim().localeCompare("Адрес") == 0) return "address";
		if (headerName.trim().localeCompare("Количество") == 0) return "count";
		if (headerName.trim().localeCompare("Чтение") == 0) return "isRead";
		if (headerName.trim().localeCompare("Запись") == 0) return "isWrite";
		if (headerName.trim().localeCompare("Тип") == 0) return "type";
		if (headerName.trim().localeCompare("Множитель") == 0) return "multiplier";
		if (headerName.trim().localeCompare("Суффикс") == 0) return "suffix";
		if (headerName.trim().localeCompare("Мин") == 0) return "min";
		if (headerName.trim().localeCompare("Макс") == 0) return "max";	
	}
	
	handleChangeDeviceName(e) {
        this.setState({ deviceName: e.target.value });
    }

	handleChangeDeviceAddress(e) {
		if (e.target.value < 0 || e.target.value > 247) {
			this.setState( { error: []});
			this.setState( prevState => ({ error: [...prevState.error, "Значение адреса может быть от 0 до 247"]}));
		} else {
			this.setState( { error: null});
			this.setState({ deviceAddress: e.target.value });
		}    	
    }


	
	
	  render() {
		
		const {data, error, loading, success} = this.state;
		
	    return (
		<div>
			{error &&
                        <div className={'alert alert-danger'}>{this.renderErrors()}</div>
                    }
			
			{success &&
                        <div className={'alert alert-success'}>{this.state.success}</div>
                    }

			<label>
          		Название устройства:
          	<input type="text" onChange={this.handleChangeDeviceName} />
			</label>
			
			<label>
          		Адрес устройства:
          	<input type="number" onChange={this.handleChangeDeviceAddress} />
			</label>

		      <CSVReader
		        cssClass="csv-reader-input"
		        label="Выберите файл"
		        onFileLoaded={this.fileLoaded}
		        onError={this.onError}
		        parserOptions={this.papaparseOptions}
		        inputId="registrTable"
		        inputStyle={{color: 'red'}}
		      />

			<table border="1">
			   <caption>Таблица регистров</caption>
			   <tr>
				<th>Название</th>
			    <th>Адрес</th>
			    <th>Количество</th>
			    <th>Чтение</th>
			    <th>Запись</th>
				<th>Тип</th>
			    <th>Множитель</th>
			    <th>Суффикс</th>
				<th>Мин.</th>
				<th>Макс.</th>
			   </tr>

				<tbody>
				{this.renderTableData()}
				</tbody>

			</table>
			
			<div className="form-group">
                        <button className="btn btn-primary" onClick={this.handleClick} disabled={error || (data.length==0)}>Добавить устройство</button>
                        {loading &&
                            <img src={Strings.LOADING} />
                        }
                    </div>

		</div>
	    )
	  }
	
}

export default LoadRegistersComponent