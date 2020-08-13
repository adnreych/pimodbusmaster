import React, { Component } from 'react';

export class Tabs extends Component {
    constructor(){
        super();
        this.state = {
            activeTabIndex: 1
        }
    }


    render() {
        const {children} = this.props;
        return (
            <div>
            <nav>
                {children.map((el,i) => <button key={i} className={this.state.activeTabIndex == el.props.tab ? 'active button' : 'button'} onClick={() => this.setState({activeTabIndex: parseInt(el.props.tab,10)})}>Tab {el.props.tab} </button>)}
            </nav>
            <hr />
            <div>{children[this.state.activeTabIndex-1]}</div>
            </div>
        );
    }
}


export class TabPane extends Component {
    render() {
        return (
            <div className='tabContent'>
                {this.props.children}
            </div>
        );
    }
}