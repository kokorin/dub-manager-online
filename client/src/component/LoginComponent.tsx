import React, {FC, useEffect, useState} from "react";
import GoogleLogin, {GoogleLoginResponse, GoogleLoginResponseOffline} from "react-google-login";
import axios from "axios";
import {getConfig} from "../api";

const LoginComponent: FC = () => {

    const [conf, setConf] = useState({googleOAuthClientId: ""});
    useEffect(() => {
        // run once
        getConfig().then(res => setConf({
            googleOAuthClientId: res.googleOAuthClientId || ""
        }));
    }, [])

    const successHandler = async (response: GoogleLoginResponse | GoogleLoginResponseOffline) => {
        const tokenId = (response as GoogleLoginResponse)?.tokenId;

        // TODO do we need to use qs library to convert params object to url-form-encoded string?
        const params = `id_token=${tokenId}`;
        const res = await axios.post("/login/google", params);

        console.log("dmo response " + res);

        let search = window.location.search
        if (search.startsWith('?')) {
            search = search.substring(1)
        }
        const redirect = search.split("&")
            .map(kv => kv.split('=', 2))
            .filter(([k, v]) => k === "redirect")
            .map(([k, v]) => v)
            .map(v => decodeURIComponent(v))
            .pop()

        if (redirect) {
            window.location.href = redirect
        }
    }

    const failureHandler = (error: any) => {
        console.log("login failure " + error);
    }

    if (!conf.googleOAuthClientId) {
        return <div/>;
    }

    return (
        <div>
            <GoogleLogin clientId={conf.googleOAuthClientId}
                         onSuccess={successHandler}
                         onFailure={failureHandler}/>
        </div>
    );
}

export default LoginComponent
