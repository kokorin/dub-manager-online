import React, { FC, useState } from "react";
import AnimeStatusTable from "./AnimeStatusTable";
import { Button, Modal } from "@material-ui/core";
import { AnimeTable } from "./AnimeTable";

const Main: FC = () => {
    const [searchOpen, setSearchOpen] = useState(false);

    const style = {
        margin: "10%",
        //transform: "translate(-10%, -10%)",
    };

    return (
        <>
            <Modal style={style} open={searchOpen} onClose={() => setSearchOpen(false)}>
                <AnimeTable />
            </Modal>
            <Button onClick={() => setSearchOpen(true)}>Add Anime</Button>
            <AnimeStatusTable />
        </>
    );
};

export default Main;
