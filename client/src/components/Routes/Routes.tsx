import React, { FC } from "react";
import { Route, Switch } from "react-router-dom";
import { SecureRoute } from "components/SecureRoute";
import { NotFound } from "screens/NotFound";
import { Login } from "../../screens/Login";
import { AnimeTable } from "../../screens/AnimeTable";
import AnimeView from "../../screens/AnimeView";
import AnimeStatusScreen from "../../screens/AnimeStatusScreen";

interface OwnProps {
    isAuthenticated: boolean;
    authenticationPath: string;
}

const Routes: FC<OwnProps> = ({ isAuthenticated, authenticationPath }) => {
    return (
        <Switch>
            <SecureRoute
                path={"/"}
                component={AnimeStatusScreen}
                exact={true}
                isAuthenticated={isAuthenticated}
                authenticationPath={authenticationPath}
            />
            <SecureRoute
                path={"/anime/:animeId"}
                isAuthenticated={isAuthenticated}
                authenticationPath={authenticationPath}
                render={(props) => <AnimeView animeId={parseInt(props.match.params.animeId || "")} />}
            />
            <SecureRoute
                path={"/search/anime/"}
                component={AnimeTable}
                isAuthenticated={isAuthenticated}
                authenticationPath={authenticationPath}
            />
            <Route path={authenticationPath} component={Login} exact={true} />
            <Route>
                <NotFound />
            </Route>
        </Switch>
    );
};

export default Routes;
