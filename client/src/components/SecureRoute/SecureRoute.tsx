import React, { FC } from "react";
import { Redirect, Route, RouteProps, useHistory } from "react-router-dom";

interface OwnProps extends RouteProps {
    isAuthenticated: boolean;
    authenticationPath: string;
}

const SecureRoute: FC<OwnProps> = ({ isAuthenticated, authenticationPath, ...restOfProps }) => {
    const history = useHistory();
    const from = history.location;
    if (isAuthenticated) {
        return <Route {...restOfProps} />;
    } else {
        return <Redirect to={{ pathname: authenticationPath, state: { from } }} />;
    }
};

export default SecureRoute;
