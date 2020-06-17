import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import ModbusReadComponent from './ModbusReadComponent';

class InstructorApp extends Component {
    render() {
        return (
			 <Router>
                <>
                    <h1>Instructor Application</h1>
                    <Switch>
						<Route path="/read" exact component={ModbusReadComponent} /> 
                    </Switch>
					<ModbusReadComponent />
                </>
            </Router>
        )
    }
}
export default InstructorApp