import React from 'react';
import './App.css';
import {Route, Switch} from "react-router-dom";
import AnimeTable from "./component/AnimeTable";
import AnimeView from "./component/AnimeView";

interface AnimeViewRouteProps {
    match: {
        params: {
            animeId:number
        }
    }
}

class AnimeViewRoute extends React.Component<AnimeViewRouteProps, any> {

    render() {
        return (
            <AnimeView animeId={this.props.match.params.animeId} />
        );
    }
}

class App extends React.Component {
    render() {
        return (
            <main>
                <Switch>
                    <Route path="/" component={AnimeTable} exact/>
                    <Route path="/anime/:animeId" component={AnimeViewRoute}/>
                </Switch>
            </main>
            /*<AnimeViewContainer animeId={1}/>*/
        );
    }
}

export default App;
