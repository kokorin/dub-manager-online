import React, {FC} from 'react';
import './App.css';
import {Route, Switch} from "react-router-dom";
import AnimeTable from "./component/AnimeTable";
import AnimeView from "./component/AnimeView";
import LoginComponent from "./component/LoginComponent";
import NotFound from "./NotFound";

interface AnimeViewRouteProps {
    match: {
        params: {
            animeId: number
        }
    }
}

class AnimeViewRoute extends React.Component<AnimeViewRouteProps, any> {

    render() {
        return (
            <AnimeView animeId={this.props.match.params.animeId}/>
        );
    }
}


const App: FC = () => {
    return (
        <main>
            <Switch>
                <Route path="/" component={AnimeTable} exact/>
                <Route path="/login" component={LoginComponent} exact/>
                <Route path="/anime/:animeId" component={AnimeViewRoute}/>
                <Route>
                    <NotFound/>
                </Route>
            </Switch>
        </main>
        /*<AnimeViewContainer animeId={1}/>*/
    );
}

export default App;
