import React from 'react';
import { BrowserRouter as Router, Route } from 'react-router-dom';

import { PrivateRoute } from '../component/PrivateRoute';
import { HomePage } from '../component/HomePage';
import { LoginPage } from '../component/LoginPage';
import ModbusReadComponent from '../component/ModbusReadComponent';
import LoadRegistersComponent from '../component/LoadRegistersComponent';
import DeviceComponent from '../component/DeviceComponent';


class App extends React.Component {
    render() {
        return (
            <div className="jumbotron">
                <div className="container">
                    <div className="col-sm-8 col-sm-offset-2">
                        <Router>
                            <div>
								<Route path="/login" component={LoginPage} />
                                <PrivateRoute exact path="/" component={HomePage} />     
								<PrivateRoute exact path="/read" component={ModbusReadComponent} />  
								<PrivateRoute exact path="/loadregisters" component={LoadRegistersComponent} />    
								<PrivateRoute exact path="/device/:id/" component={DeviceComponent} />                           
                            </div>
                        </Router>
                    </div>
                </div>
            </div>
        );
    }
}

export { App };