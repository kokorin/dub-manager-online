import React, { FC, useCallback, useState } from "react";
import GoogleLogin, { GoogleLoginResponse, GoogleLoginResponseOffline } from "react-google-login";
import { Redirect, useHistory } from "react-router-dom";
import { useGetConfigurationQuery } from "../api";
import { loginUser } from "../service/auth";

export const Login: FC = () => {
    const [authenticated, setAuthenticated] = useState(false);
    const { data: conf, isLoading } = useGetConfigurationQuery({});
    const history = useHistory();
    const { from } = (history.location.state as { from: Location }) ?? { from: { pathname: "/" } };

    const successHandler = useCallback(async (response: GoogleLoginResponse | GoogleLoginResponseOffline) => {
        const tokenId = (response as GoogleLoginResponse)?.tokenId;
        const token = await loginUser(tokenId);
        setAuthenticated(token != null);
    }, []);

    const failureHandler = useCallback((error: unknown) => {
        console.log("login failure " + JSON.stringify(error));
    }, []);

    if (!conf || isLoading) {
        return <div>Loading...</div>;
    }

    if (authenticated) {
        return <Redirect to={from} />;
    }

    return (
        <div>
            <GoogleLogin clientId={conf.googleOAuthClientId} onSuccess={successHandler} onFailure={failureHandler} />
        </div>
    );
};
