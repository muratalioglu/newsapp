import React from 'react';
import './App.css';

class App extends React.Component {

  constructor(props) {
    super(props)
    this.state = { news: [] }
  }

  componentDidMount() {
    fetch("http://localhost:8080/news").then(res => res.json())
    .then(data => this.setState({ news: data }))
  }

  render() {
    return <div>
      <ul>
        {this.state.news.map(n => 
          <li key={n.id}>
            <h3>{n.title}</h3>
            <p>{n.content}</p>
            <p><img src={n.imageUrl} /></p>
          </li>)
        }
      </ul>
      </div>
  }
}

export default App;
