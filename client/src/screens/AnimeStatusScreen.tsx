import React, { FC, useState } from "react";
import AnimeStatusTable from "./AnimeStatusTable";
import { Button, CircularProgress, Modal, Paper } from "@material-ui/core";
import AnimeSelect from "./AnimeSelect";
import { UpdateAnimeStatusDto, useUpdateAnimeStatusMutation } from "../api";

const AnimeStatusScreen: FC = () => {
    const [animeSelectOpen, setAnimeSelectOpen] = useState(false);

    const [updateAnimeStatus, { isLoading: isUpdating }] = useUpdateAnimeStatusMutation();

    const handleAnimeSelected = (animeIds: number[]) => {
        animeIds.forEach((animeId: number) => {
            const updateAnimeStatusDto: UpdateAnimeStatusDto = { progress: "IN_PROGRESS", comment: "" };
            updateAnimeStatus({ animeId, updateAnimeStatusDto });
        });
        setAnimeSelectOpen(false);
    };

    if (isUpdating) {
        return <CircularProgress color="secondary" />;
    }

    const style = {
        margin: "10%",
        //transform: "translate(-10%, -10%)",
    };

    return (
        <div className="anime_status_screen">
            <Modal style={style} open={animeSelectOpen} onClose={() => setAnimeSelectOpen(false)}>
                <Paper style={{ height: "100%", width: "100%" }}>
                    <AnimeSelect
                        onAnimeSelected={handleAnimeSelected}
                        onSelectCancelled={() => setAnimeSelectOpen(false)}
                    />
                </Paper>
            </Modal>
            <div className="status_table_parent" style={{ height: "100%", width: "100%" }}>
                <Button onClick={() => setAnimeSelectOpen(true)}>Add Anime</Button>
                <div style={{ display: "flex", height: "100%" }}>
                    <AnimeStatusTable />
                </div>
            </div>
        </div>
    );
};

export default AnimeStatusScreen;
