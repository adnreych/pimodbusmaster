import React, { Component } from 'react'; 
import BitTypeValuesComponent from './BitTypeValuesComponent';

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
								
				{
					(this.state.pair.first !== undefined && this.state.pair.first.type === "Bit") 
					?
					<BitTypeValuesComponent 
								index={this.state.index} 
								legends={JSON.stringify(this.state.pair.first.content)} 
								callbackFromParent={this.state.callbackFromParent} 
								value={this.state.value.first} 
								/>
					:
					<input type="text" placeholder="Значение" 
									value={this.state.value.first} 
									ref={this.state.index}
									onChange={(event) => {}} />
				}
				
				
				{
					(this.state.pair.second !== undefined && this.state.pair.second.type === "Bit") 
					?
					<BitTypeValuesComponent 
								index={this.state.index} 
								legends={JSON.stringify(this.state.pair.second.content)} 
								callbackFromParent={this.state.callbackFromParent} 
								value={this.state.value.second} 
								/>
					:
					<input type="text" placeholder="Значение" 
									value={this.state.value.second} 
									ref={this.state.index}
									onChange={(event) => {}} />
				}
																			
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