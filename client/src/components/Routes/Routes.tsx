import React, { FC } from "react";
import { Route, Switch } from "react-router-dom";
import { SecureRoute } from "components/SecureRoute";
import { AnimeTable } from "screens/AnimeTable";
import { AnimeView } from "screens/AnimeView";
import { Login } from "screens/Login";
import { NotFound } from "screens/NotFound";

interface OwnProps {
    isAuthenticated: boolean;
    authenticationPath: string;
}

const Routes: FC<OwnProps> = ({ isAuthenticated, authenticationPath }) => {
    return (
        <Switch>
            <SecureRoute
                path={"/"}
                component={AnimeTable}
                exact={true}
                isAuthenticated={isAuthenticated}
                authenticationPath={authenticationPath}
            />
            <SecureRoute
                path={"/anime/:animeId"}
                component={AnimeView}
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
