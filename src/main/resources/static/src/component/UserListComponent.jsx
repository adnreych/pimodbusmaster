import React, { Component } from 'react';
import userService from '../service/UserService';
import * as Strings from '../helpers/strings';
import { confirmAlert } from 'react-confirm-alert';
import Option from 'muicss/lib/react/option';
import Select from 'muicss/lib/react/select';


class UserListComponent extends Component {
	
	constructor(props) {
        super(props);

	console.log("UserListComponent");

		this.state = {
					users: [],
					addedUser: {},
					loading: false,
					error: null,		
					success: null,	
					roles: []	
		        }

		this.changeUser = this.changeUser.bind(this);
		this.deleteUser = this.deleteUser.bind(this);
		this.addUser = this.addUser.bind(this);
		this.handleChangeAddedUser = this.handleChangeAddedUser.bind(this);

    }


	componentDidMount() {
		this.setState({ loading: true });
		userService.getAll()
            .then(
                (response) => {
					console.log("USERSresp", response);
                   this.setState({users: response.data})
                }
            )
			.catch((err) => {
				  console.log("ERROR: ", err);
					this.setState({ error: err, loading: false });
			  });
		userService.getRoles()
            .then(
                (response) => {
					console.log("RolesResp", response);
                   this.setState({roles: response.data})
                }
            )
			.catch((err) => {
				  console.log("ERROR: ", err);
					this.setState({ error: err, loading: false });
			  });
		this.setState({ loading: false });
		console.log("USERS", this.state.users);
    }

	handleChangeAddedUser(event, key) {
		var addedUser = this.state.addedUser;
		addedUser[key] = event.target.value;
		if (key == "roles") {
			addedUser.roles = []
			addedUser.roles.push({id : event.target.value, name : event.target.label})
		}	
		this.setState({ addedUser: addedUser });	
	}

	addUser() {
		confirmAlert({
		  closeOnClickOutside: true,
		  customUI: ({ onClose }) => {
		    return (
		      <div className='custom-ui'>
					<tr>
					<td>
			        <label>Имя</label>
	                <input type="text" className="form-control" onChange={(event) => this.handleChangeAddedUser(event, "username")}/>
					</td>
					<td>
					<label>Пароль</label>
	                <input type="text" className="form-control" onChange={(event) => this.handleChangeAddedUser(event, "password")} />
					</td>
					<td>
					<label>Группа</label>
	                <Select name="input" onChange={(event) => this.handleChangeAddedUser(event, "roles")} >
				          <Option value="1" label="ADMIN" />
						  <Option value="2" label="EMPLOYEE" />
						  <Option value="3" label="CLIENT" />
						  <Option value="4" label="CLIENTCUT" />
				    </Select>
					</td>
					<td>
			        <button 
					  onClick={() => {
							onClose();	
						}}>Отмена</button>
					</td>
					<td>
			        <button
			          onClick={() => {
								this.setState({ loading: true });
								userService.addUser(this.state.addedUser)
									.then(() => {	
										var users = this.state.users;
										users.push(this.state.addedUser);								
										this.setState({
												users: users,
												addedUser: {},
												loading: false,
												error: null,
												success: "Пользователь успешно добавлен"
											})	
									})
									.catch((err) => {
											 console.log("ERROR: ", err);
											  this.setState({ 
												loading: false, 
												error: "Ошибка добавления пользователя",
												success: null })
										  });
								onClose();		
							}}>Сохранить</button>
						</td>
					</tr>
		      </div>
		    );
		  }
		});
	}
	
	deleteUser() {
		
	}
	

	changeUser() {
		
	}


	renderTableData() {
      return this.state.users.map((current, index) => {
         const { username, roles} = current;
         return (
            <tr key={index}>
               <td>{username}</td>
               <td>{roles.map((current) => {return (<p class="simple-text">{current.name}</p>)})}</td>	
				<td>
					<button className="btn btn-primary" onClick={() => this.changeUser()}>Изменить</button>
				</td>	
				<td>
					<button className="btn btn-primary" onClick={() => this.deleteUser()}>Удалить</button>
				</td>				          								
            </tr>
					
         )
      })
   }

	
	 render() {
		const {loading, error, success} = this.state;		
	    return (
			<div>
				{error &&
	                        <div className={'alert alert-danger'}>{this.state.error}</div>
	                    }
				
				{success &&
	                        <div className={'alert alert-success'}>{this.state.success}</div>
	                    }

				<table border="1">
				   <caption>Управление пользователями</caption>
				   <tr>
					<th>Имя</th>
				    <th>Роли</th>
				   </tr>
	
					<tbody>
					{this.renderTableData()}
					</tbody>
	
				</table>
				
				<button className="btn btn-primary" onClick={() => this.addUser()}>Добавить нового</button>
				
				<div className="form-group">
	                        {loading &&
	                            <img src={Strings.LOADING} />
	                        }
	                    </div>
			</div>
	    )
	  }

}

export default UserListComponent