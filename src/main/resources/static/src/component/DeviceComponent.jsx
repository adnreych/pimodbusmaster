import React, { Component } from 'react';
import * as Strings from '../helpers/strings';


class DeviceComponent extends Component {
	
	constructor(props) {
        super(props);

		this.state = {
					data: [],
					loading: false,
		        }

		this.handleClickRead = this.handleClickRead.bind(this);
		this.handleClickWrite = this.handleClickWrite.bind(this);

    }


	componentDidMount() {
        console.log("props: ", this.props);
    }

	handleClickRead = (address, count) => {    
			
	}
		
	handleClickWrite = (address, value) => {    
			
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
			   <td>{min}</td>
               <td>{max}</td>
				
				<td>{suffix}</td>
				<div className="form-group">
						<input type="text" placeholder="Значение" ref={index} />
                        <button className="btn btn-primary" onClick={() => this.handleClickRead(address, count)} disabled={!isRead}>Чтение</button>
                        {loading &&
                            <img src={Strings.LOADING} />
                        }
                        <button className="btn btn-primary" onClick={() => this.handleClickWrite(address, this.refs.index.value)} disabled={!isWrite}>Запись</button>
                        {loading &&
                            <img src={Strings.LOADING} />
                        }
                        <button className="btn btn-primary">Изменить</button>
                        <button className="btn btn-primary">Удалить</button>
                 </div>
            </tr>
         )
      })
   }


	
	
  	render() {
		
		const {data, loading} = this.state;
		
	    return (
		<div>

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
                        <button className="btn btn-primary">Удалить устройство</button>
                        {loading &&
                            <img src={Strings.LOADING} />
                        }
                    </div>

		</div>
	    )
	  }
	
}

export default DeviceComponent