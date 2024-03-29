import React, { FC } from "react";
import { Route, Switch } from "react-router-dom";
import { SecureRoute } from "components/SecureRoute";
import { NotFound } from "screens/NotFound";
import { Login } from "../../screens/Login";
import { AnimeTable } from "../../screens/AnimeTable";
import Main from "../../screens/Main";

interface OwnProps {
    isAuthenticated: boolean;
    authenticationPath: string;
}

const Routes: FC<OwnProps> = ({ isAuthenticated, authenticationPath }) => {
    return (
        <Switch>
            <SecureRoute
                path={"/"}
                component={Main}
                exact={true}
                isAuthenticated={isAuthenticated}
                authenticationPath={authenticationPath}
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
