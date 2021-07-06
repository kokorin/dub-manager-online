import React, { FC, useState } from "react";
import AnimeStatusTable from "./AnimeStatusTable";
import { Button, Modal } from "@material-ui/core";

const Main: FC = () => {
    const [searchOpen, setSearchOpen] = useState(false);

    const style = {
        top: "10%",
        left: "10%",
        //transform: "translate(-10%, -10%)",
    };
    const body = <div>Hello Modal</div>;
    return (
        <>
            <Modal style={style} open={searchOpen} onClose={() => setSearchOpen(false)}>
                {body}
            </Modal>
            <Button onClick={() => setSearchOpen(true)}>Add Anime</Button>
            <AnimeStatusTable />
        </>
    );
};

export default Main;
