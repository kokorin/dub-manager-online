import React, { FC } from "react";
import { useSelector } from "react-redux";
import { selectUserName, selectUserPicture } from "../Login/userSelectors";

const Main: FC = () => {
    const userName: string = useSelector(selectUserName);
    const userPicture: string = useSelector(selectUserPicture);
    return (
        <main style={{ maxWidth: "1024px", margin: "0 auto" }}>
            <header style={{ display: "flex", justifyContent: "space-between" }}>
                <nav style={{ flexGrow: 1 }}>
                    <ul style={{ display: "flex", justifyContent: "space-between" }}>
                        <li>Home</li>
                        <li>Search</li>
                    </ul>
                </nav>
                <div>
                    <img src={userPicture} alt={userName} />
                </div>
            </header>
        </main>
    );
};

export default Main;
