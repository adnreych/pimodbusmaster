import React, { Component } from 'react';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import ModbusService from '../service/ModbusService';


class ModbusReadComponent extends Component {
	
	constructor(props) {
        super(props)
        this.state = {
            slave: 247,
            address: 0,
			count: 1,
			responses: []
        }
		this.onSubmit = this.onSubmit.bind(this)
		this.validate = this.validate.bind(this)
    }

    componentDidMount() {
        this.getModbusResponse();
    }

    getModbusResponse() {
		const _this = this;
	        ModbusService.getModbusResponse()
	            .then(
	                response => {
	                    console.log(response);
	                    _this.setState({ responses: response.data })
	                }
	            )
    }

	
	onSubmit(values) {
		const _this = this;

        let modbusRequest = {
            slave: values.slave,
           	address: values.address,
			count: values.count
        }

		console.log("values" + values);
	
		ModbusService.modbusRequest(modbusRequest)
			.then(() => {
				_this.props.history.push(`/read`);
				_this.getModbusResponse();
			})
						     
    }
	
	validate = values => {
		  const errors = {};
		  if (!values.slave) {
		    errors.slave = 'Введите имя устройства!';
		  } 

		  if (!values.address && values.address != 0) {
		    errors.address = 'Введите начальный адрес!';
		  } 
		
		  if (!values.count) {
		    errors.count = 'Введите количество регистров!';
		  } 
		
		  return errors;
	}

     render() {
		console.log("resp " + this.state.responses);
		let { slave, address, count } = this.state
        return (
            <div className="container">
                    <Formik
					initialValues={{ slave, address, count }}
					onSubmit={this.onSubmit}
					validateOnChange={false}
			      	validateOnBlur={false}
			      	validate={this.validate}
			      	enableReinitialize={true}>
                    {
                        (props) => (
                            <Form>
								<ErrorMessage name="slave" component="div"
												 className="alert alert-warning" />
                                <fieldset className="form-group">
                                    <label>Slave</label>
                                    <Field className="form-control" type="text" name="slave" />
                                </fieldset>
								<ErrorMessage name="address" component="div"
												 className="alert alert-warning" />
                                <fieldset className="form-group">
                                    <label>Address</label>
                                    <Field className="form-control" type="text" name="address" />
                                </fieldset>
								<ErrorMessage name="count" component="div"
												 className="alert alert-warning" />
								<fieldset className="form-group">
                                    <label>Count</label>
                                    <Field className="form-control" type="text" name="count" />
                                </fieldset>
                                <button className="btn btn-success" type="submit">Save</button>
                            </Form>
                        )
                    }
                </Formik>


                    <table className="table">
                        <tbody>
                            {
                                this.state.responses.map(
                                    rsp =>
                                        <p>{rsp}</p>
                                )
                            }
                        </tbody>
                    </table>
                </div>
        )
    }

}

export default ModbusReadComponent