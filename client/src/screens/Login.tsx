import React, { FC } from "react";
import { GoogleLoginButton } from "react-social-login-buttons";

export const Login: FC = () => {
    //const history = useHistory();
    //const { from } = (history.location.state as { from: Location }) ?? { from: { pathname: "/" } };

    // force redirect to bypass React Router
    const loginWith = (oAuthProvider: string) => {
        //const redirect = `/login/oauth2/code/${oAuthProvider}`;
        const to = `/oauth2/authorization/${oAuthProvider}`;
        window.location.href = to;
    };

    return (
        <div>
            <GoogleLoginButton onClick={() => loginWith("google")} />
        </div>
    );
};
