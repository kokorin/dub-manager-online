import React, { FC } from "react";
import { useSelector } from "react-redux";
import { selectIsAuthenticated } from "../Login";
import { Routes } from "components/Routes";

const LOGIN_PATH = "/login";

const App: FC = () => {
    const isAuthenticated = useSelector(selectIsAuthenticated);
    return <Routes isAuthenticated={isAuthenticated} authenticationPath={LOGIN_PATH} />;
};

export default App;
