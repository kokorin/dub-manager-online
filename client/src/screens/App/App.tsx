import React, { FC } from "react";
import { useSelector } from "react-redux";
import { Routes } from "components/Routes";
import { selectAuth } from "../../auth";
import { Modal } from "@material-ui/core";
import { selectIsFetching } from "../../api";
import Loader from "../../components/Loader";

const LOGIN_PATH = "/login";

const App: FC = () => {
    const { isAuthenticated } = useSelector(selectAuth);
    const isFetching = useSelector(selectIsFetching);
    return (
        <>
            <Modal open={isFetching}>
                <Loader />
            </Modal>
            <Routes isAuthenticated={isAuthenticated} authenticationPath={LOGIN_PATH} />
        </>
    );
};

export default App;
