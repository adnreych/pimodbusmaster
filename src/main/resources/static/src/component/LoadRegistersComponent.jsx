import React, { Component } from 'react';
import ReactDOM from 'react-dom'
import CSVReader from 'react-csv-reader'
import LoadRegistersService from '../service/LoadRegistersService';


class LoadRegistersComponent extends Component {
	// организовать загрузку файла
	
	constructor(props) {
        super(props);

		this.state = {
					data: [],
					fileInfo: []
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

    }

	fileLoaded = (data, fileInfo) => {
		console.log(data, fileInfo);
		this.setState({data: data, fileInfo: fileInfo});
	};
	
	onError = (e) => {
		console.log("ERRORLOADFILE", e);
	};
	
	renderTableData() {
      return this.state.data.map((current, index) => {
         const { name, address, count, isRead, isWrite, type, multiplier, suffix, min, max } = current;
		console.log("current", current);
         return (
            <tr key={index}>
               <td>{name}</td>
               <td>{address}</td>
               <td>{count}</td>
               <td>{isRead}</td>
			   <td>{isWrite}</td>
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

	
	
 
	  render() {
	    return (
		<div>
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

		</div>
	    )
	  }
	
}

export default LoadRegistersComponent