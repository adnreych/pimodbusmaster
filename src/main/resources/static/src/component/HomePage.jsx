import React from 'react';
import { Link } from 'react-router-dom';
import DeviceService from '../service/DeviceService';
import Option from 'muicss/lib/react/option';
import Select from 'muicss/lib/react/select';

class HomePage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            user: {},
            users: [],
			devices: [],
			currDevice: 0,
			CSDConnect: false
        };

		this.handleChange = this.handleChange.bind(this);
		this.handleSubmit = this.handleSubmit.bind(this);
		this.handleChangeConnectType = this.handleChangeConnectType.bind(this);
    }

    componentDidMount() {
        this.setState({ 
            user: JSON.parse(localStorage.getItem('user')),
            users: { loading: true }
        });
        DeviceService.getAll()
			.then(devices => {
				this.setState({ 
					devices: devices.data,
					})
			})
			.catch((err) => {
					  console.log("ERROR: ", err);
				  });
    }

	renderSelectData() {
	      return this.state.devices.map((current, index) => {
	         return (
				<Option value={index} label={current.name} />
	         )
	      })
	   }

	
	handleChange(event) {    
		this.setState({currDevice: event.target.value});  
	  }
		
	handleSubmit(event) {
		event.preventDefault(); 
		if (!this.state.CSDConnect) {
			this.props.history.push(`/device/${this.state.devices[this.state.currDevice].id}`);   
		} else {
			this.props.history.push(`/csdconnect/${this.state.devices[this.state.currDevice].id}`);   
		}		
	  }

	 handleChangeConnectType() {
        this.setState({CSDConnect: !this.state.handleChange});
    }

    render() {
        const { user } = this.state;
        return (
            <div className="col-md-6 col-md-offset-3">
                <h1>Здравствуйте {user.username}!</h1>
                <h3>Что вы хотите сделать?</h3>

				<p>
                    <Link to="/read">Прочитать регистры</Link>
                </p>

				<p>
                    <Link to="/loadregisters">Загрузить регистры</Link>
                </p>

				<p>
                    <Link to="/userlist">Управление пользователями</Link>
                </p>

				
				<p>
                    <Link to="/login">Выйти</Link>
                </p>

				<form onSubmit={this.handleSubmit}>
			        <label>
			          Выберите устройство:
						<Select name="input" value={this.state.currDevice} onChange={this.handleChange}>
				          {this.renderSelectData()}
				        </Select>
			        </label>
			        <input type="submit" value="Перейти" />
			      </form>

				<div>
	                <input onChange={this.handleChangeConnectType} type="checkbox" />
	                <label> Соединение по CSD</label>
           		</div>


            </div>
        );
    }
}

export { HomePage };