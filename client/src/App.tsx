import React from 'react';
import './App.css';
import AnimeViewContainer from "./AnimeView";


class App extends React.Component {
    render() {
        return (
            <AnimeViewContainer animeId={1}/>
        );
    }
}

export default App;
