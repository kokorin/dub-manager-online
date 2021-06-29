import React, { FC, useCallback, useEffect } from "react";
import GoogleLogin, { GoogleLoginResponse, GoogleLoginResponseOffline } from "react-google-login";
import { useDispatch, useSelector } from "react-redux";
import { Redirect, useHistory } from "react-router-dom";
import { selectClientId, selectIsAuthenticated } from "./userSelectors";
import { authenticateUser, fetchConfig } from "./userSlice";

const Login: FC = () => {
    const dispatch = useDispatch();
    const history = useHistory();
    const { from } = (history.location.state as { from: Location }) ?? { from: { pathname: "/" } };

    useEffect(() => {
        dispatch(fetchConfig());
    }, [dispatch]);

    const clientId = useSelector(selectClientId);
    const isAuthenticated = useSelector(selectIsAuthenticated);

    const successHandler = useCallback(
        (response: GoogleLoginResponse | GoogleLoginResponseOffline) => {
            const tokenId = (response as GoogleLoginResponse)?.tokenId;
            dispatch(authenticateUser(tokenId));
        },
        [dispatch],
    );

    const failureHandler = useCallback((error: unknown) => {
        console.log("login failure " + JSON.stringify(error));
    }, []);

    if (!clientId) {
        return <div>Unable to login</div>;
    }

    if (isAuthenticated) {
        return <Redirect to={from} />;
    }

    return (
        <div>
            <GoogleLogin clientId={clientId} onSuccess={successHandler} onFailure={failureHandler} />
        </div>
    );
};
export default Login;
