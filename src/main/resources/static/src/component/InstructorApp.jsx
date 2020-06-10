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
						<Route path="/" exact component={ModbusReadComponent} />
                    </Switch>
                </>
            </Router>
        )
    }
}

export default InstructorApp