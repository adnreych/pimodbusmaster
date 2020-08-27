import React, { Component } from 'react'; 


class BoxTypeComponent extends Component {
	
	constructor(props) {
        super(props);

		this.state = {
					loading: false,
					pair: JSON.parse(this.props.pair),
					index: this.props.index,
					callbackFromParent: this.props.callbackFromParent,
					handleChange: this.props.handleChange,
					value: this.props.value !== undefined ? this.props.value : "" 
		        }
    }


	renderBoxType() {
		
		console.log("STATE BOX TYPE", this.state)
		
		return(
			<div>
				{(this.state.pair.first.type=="Bit") && <BitTypeValuesComponent 
								index={this.state.index} 
								legends={this.state.pair.first.legends} 
								callbackFromParent={this.state.callbackFromParent} 
								value={this.state.value.first} 
								/>}
										
				{(this.state.pair.first.type!="Bit") && 
									<input type="text" placeholder="Значение" 
									value={this.state.value.first} 
									ref={this.state.index}
									onChange={(event) => this.state.handleChange(event, this.state.index)} />}
									
				{(this.state.pair.second.type=="Bit") && <BitTypeValuesComponent 
										index={this.state.index} 
										legends={this.state.pair.second.legends} 
										callbackFromParent={this.state.callbackFromParent} 
										value={this.state.value.second} 
										/>}
										
				{(this.state.pair.second.type!="Bit") && 
									<input type="text" placeholder="Значение" 
									value={this.state.value.second} 
									ref={this.state.index}
									onChange={(event) => this.state.handleChange(event, this.state.index)} />}
			</div>
		)
		
		
	}


	render() {	
		  return (
			<div>
				{this.renderBoxType()}
			</div>
		)
	}

}

export default BoxTypeComponent