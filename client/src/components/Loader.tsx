import React, { FC } from "react";
import { CircularProgress } from "@material-ui/core";

const Loader: FC = () => {
    return (
        <div className="loader-content">
            <CircularProgress />
        </div>
    );
};

export default Loader;
