import React, { FC } from "react";
import { GithubLoginButton, GoogleLoginButton } from "react-social-login-buttons";
import { useGetOAuthClientsQuery } from "../api";
import { Modal } from "@material-ui/core";
import Loader from "../components/Loader";

export const Login: FC = () => {
    //const history = useHistory();
    //const { from } = (history.location.state as { from: Location }) ?? { from: { pathname: "/" } };
    const { data: clients, isLoading } = useGetOAuthClientsQuery();

    // force redirect to bypass React Router
    const loginWith = (oAuthProvider: string) => {
        //const redirect = `/login/oauth2/code/${oAuthProvider}`;
        const to = `/oauth2/authorization/${oAuthProvider}`;
        window.location.href = to;
    };

    const buttons = [];

    if (clients) {
        if (clients.includes("google")) {
            buttons.push(<GoogleLoginButton onClick={() => loginWith("google")} />);
        }

        if (clients.includes("github")) {
            buttons.push(<GithubLoginButton onClick={() => loginWith("github")} />);
        }
    }

    return (
        <>
            <Modal open={isLoading}>
                <Loader />
            </Modal>
            <div>{buttons}</div>
        </>
    );
};
