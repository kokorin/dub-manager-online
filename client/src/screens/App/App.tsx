import React, { FC } from "react";
import { useSelector } from "react-redux";
import { Routes } from "components/Routes";
import { selectAuth } from "../../auth";

const LOGIN_PATH = "/login";

const App: FC = () => {
    const { isAuthenticated } = useSelector(selectAuth);
    return <Routes isAuthenticated={isAuthenticated} authenticationPath={LOGIN_PATH} />;
};

export default App;
