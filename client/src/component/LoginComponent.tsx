import React from "react";
import GoogleLogin, {GoogleLoginResponse, GoogleLoginResponseOffline} from "react-google-login";
import axios from "axios";

// TODO request from server
const googleClientId = "242595272379-usrfmeccak59k76v3ghq1di30ogt2q0p.apps.googleusercontent.com";

export default class LoginComponent extends React.Component<any, any> {

    private successHandler = async (response: GoogleLoginResponse | GoogleLoginResponseOffline) => {
        const tokenId = (response as GoogleLoginResponse)?.tokenId;

        // TODO do we need to use qs library to convert params object to url-form-encoded string?
        const params = `id_token=${tokenId}`;
        const res = await axios.post("/login/google", params);

        console.log("dmo response " + res);
    }

    private failureHandler = (error:any) => {
        console.log("login failure " + error);
    }

    render() {
        return (
            <div>
                <GoogleLogin clientId={googleClientId}
                             onSuccess={this.successHandler}
                             onFailure={this.failureHandler}/>
            </div>
        );
    }
}
