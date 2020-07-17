import React, { Component } from 'react';
import userService from '../service/UserService';
import * as Strings from '../helpers/strings';


class UserListComponent extends Component {
	
	constructor(props) {
        super(props);

	console.log("UserListComponent");

		this.state = {
					users: [],
					loading: false,
					error: null,		
					success: null,		
		        }

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
		this.setState({ loading: false });
		console.log("USERS", this.state.users);
    }

	renderTableData() {
      return this.state.users.map((current, index) => {
         const { username, role} = current;
         return (
            <tr key={index}>
               <td>{username}</td>
               <td>{role}</td>					          								
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