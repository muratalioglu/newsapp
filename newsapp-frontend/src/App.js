import React from 'react';
import { Link } from 'react-router-dom';
import './App.css';
import NewsList from './components/NewsList';

class App extends React.Component {

  render() {
    return (
      <div>
        <NewsList />
        <Link to="/news/create">Create News</Link>
      </div>
    );
  }
}

export default App;
