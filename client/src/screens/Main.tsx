import React, { FC } from "react";
import AnimeStatusScreen from "./AnimeStatusScreen";

const Main: FC = () => {
    return (
        <div className="height-100" style={{ padding: "2em" }}>
            <AnimeStatusScreen />
        </div>
    );
};

export default Main;
