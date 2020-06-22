import React from 'react';
import { Link } from 'react-router-dom';

class HomePage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            user: {},
            users: []
        };
    }

    componentDidMount() {
        this.setState({ 
            user: JSON.parse(localStorage.getItem('user')),
            users: { loading: true }
        });
  //      userService.getAll().then(users => this.setState({ users }));
    }

    render() {
        const { user, users } = this.state;
        return (
            <div className="col-md-6 col-md-offset-3">
                <h1>Здравствуйте {user.username}!</h1>
                <h3>Что вы хотите сделать?</h3>

				<p>
                    <Link to="/read">Прочитать регистры</Link>
                </p>

				
				<p>
                    <Link to="/login">Выйти</Link>
                </p>


            </div>
        );
    }
}

export { HomePage };