import React, { FC } from "react";
import { GithubLoginButton, GoogleLoginButton } from "react-social-login-buttons";
import { useGetOAuthClientsQuery } from "../api";
import Loader from "../components/Loader";

export const Login: FC = () => {
    //const history = useHistory();
    //const { from } = (history.location.state as { from: Location }) ?? { from: { pathname: "/" } };
    const { data: clients, isLoading } = useGetOAuthClientsQuery({});

    // force redirect to bypass React Router
    const loginWith = (oAuthProvider: string) => {
        //const redirect = `/login/oauth2/code/${oAuthProvider}`;
        const to = `/oauth2/authorization/${oAuthProvider}`;
        window.location.href = to;
    };

    if (!clients || isLoading) {
        return <Loader />;
    }

    const buttons = [];

    if (clients.includes("google")) {
        buttons.push(<GoogleLoginButton onClick={() => loginWith("google")} />);
    }

    if (clients.includes("github")) {
        buttons.push(<GithubLoginButton onClick={() => loginWith("github")} />);
    }

    return <div>{buttons}</div>;
};
