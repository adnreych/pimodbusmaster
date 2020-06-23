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
					fileInfo: [],
					error: null,
					loading: false,
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

    }

  	handleClick() {    
		this.setState({ loading: true });
		LoadRegistersService.load(this.state.data)  
			.then(
                () => {
                    this.props.history.push("/");
                }
            )
			.catch((err) => {
				  console.log("ERROR: ", err);
					this.setState({ error: err, loading: false });
			  });
	}

	fileLoaded = (data, fileInfo) => {
		this.setState({ error: []});
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
		
		const {data, error, loading} = this.state;
		
	    return (
		<div>
			{error &&
                        <div className={'alert alert-danger'}>{this.renderErrors()}</div>
                    }

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
                            <img src="data:image/gif;base64,R0lGODlhEAAQAPIAAP///wAAAMLCwkJCQgAAAGJiYoKCgpKSkiH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCgAAACwAAAAAEAAQAAADMwi63P4wyklrE2MIOggZnAdOmGYJRbExwroUmcG2LmDEwnHQLVsYOd2mBzkYDAdKa+dIAAAh+QQJCgAAACwAAAAAEAAQAAADNAi63P5OjCEgG4QMu7DmikRxQlFUYDEZIGBMRVsaqHwctXXf7WEYB4Ag1xjihkMZsiUkKhIAIfkECQoAAAAsAAAAABAAEAAAAzYIujIjK8pByJDMlFYvBoVjHA70GU7xSUJhmKtwHPAKzLO9HMaoKwJZ7Rf8AYPDDzKpZBqfvwQAIfkECQoAAAAsAAAAABAAEAAAAzMIumIlK8oyhpHsnFZfhYumCYUhDAQxRIdhHBGqRoKw0R8DYlJd8z0fMDgsGo/IpHI5TAAAIfkECQoAAAAsAAAAABAAEAAAAzIIunInK0rnZBTwGPNMgQwmdsNgXGJUlIWEuR5oWUIpz8pAEAMe6TwfwyYsGo/IpFKSAAAh+QQJCgAAACwAAAAAEAAQAAADMwi6IMKQORfjdOe82p4wGccc4CEuQradylesojEMBgsUc2G7sDX3lQGBMLAJibufbSlKAAAh+QQJCgAAACwAAAAAEAAQAAADMgi63P7wCRHZnFVdmgHu2nFwlWCI3WGc3TSWhUFGxTAUkGCbtgENBMJAEJsxgMLWzpEAACH5BAkKAAAALAAAAAAQABAAAAMyCLrc/jDKSatlQtScKdceCAjDII7HcQ4EMTCpyrCuUBjCYRgHVtqlAiB1YhiCnlsRkAAAOwAAAAAAAAAAAA==" />
                        }
                    </div>

		</div>
	    )
	  }
	
}

export default LoadRegistersComponent