import React, { FC } from "react";
import { CircularProgress } from "@mui/material";

const Loader: FC = () => {
    return (
        <div className="loader-content">
            <CircularProgress />
        </div>
    );
};

export default Loader;
