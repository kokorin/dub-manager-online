import React, { FC, useState } from "react";
import AnimeStatusTable from "./AnimeStatusTable";
import { Box, Button, CircularProgress, Modal, Paper } from "@material-ui/core";
import AnimeSelect from "./AnimeSelect";
import { useDeleteAnimeStatusMutation, useUpdateAnimeStatusMutation } from "../api";
import AnimeStatusView from "./AnimeStatusView";
import { AnimeStatus } from "../domain";

const AnimeStatusScreen: FC = () => {
    const [selectedAnimeStatuses, setSelectedAnimeStatuses] = useState([] as number[]);
    const [animeShown, setAnimeShown] = useState(null as unknown as AnimeStatus);
    const [animeSelectOpen, setAnimeSelectOpen] = useState(false);

    const [updateAnimeStatus, { isLoading: isUpdating }] = useUpdateAnimeStatusMutation();
    const [deleteAnimeStatus, { isLoading: isDeleting }] = useDeleteAnimeStatusMutation();

    const handleAnimeSelected = (animeIds: number[]) => {
        animeIds.forEach((animeId: number) => {
            updateAnimeStatus({ id: animeId, updateAnimeStatusDto: { progress: "IN_PROGRESS", comment: "" } });
        });
        setAnimeSelectOpen(false);
    };

    const handleDeleteClick = () => {
        selectedAnimeStatuses.forEach((animeId) => {
            deleteAnimeStatus({ id: animeId });
        });
    };

    return (
        <div className="anime_status_screen">
            <Modal open={isUpdating || isDeleting}>
                <CircularProgress />
            </Modal>
            <Modal style={{ margin: "10%" }} open={animeSelectOpen} onClose={() => setAnimeSelectOpen(false)}>
                <Paper style={{ height: "100%", width: "100%" }}>
                    <AnimeSelect
                        onAnimeSelected={handleAnimeSelected}
                        onSelectCancelled={() => setAnimeSelectOpen(false)}
                    />
                </Paper>
            </Modal>
            <Modal
                style={{ margin: "10%" }}
                open={!!animeShown}
                onClose={() => setAnimeShown(null as unknown as AnimeStatus)}
            >
                <Paper style={{ height: "100%", width: "100%" }}>
                    <AnimeStatusView animeStatus={animeShown} style={{ height: "100%", width: "100%" }} />
                </Paper>
            </Modal>
            <div className="status_table_parent" style={{ height: "100%", width: "100%" }}>
                <Box justifyContent="right">
                    <Button color="primary" onClick={() => setAnimeSelectOpen(true)}>
                        Add Anime
                    </Button>
                    <Button color="secondary" disabled={!selectedAnimeStatuses.length} onClick={handleDeleteClick}>
                        Delete Anime
                    </Button>
                </Box>
                <div style={{ display: "flex", height: "100%" }}>
                    <AnimeStatusTable
                        style={{ height: "100%", width: "100%" }}
                        onAnimeStatusSelected={setSelectedAnimeStatuses}
                        onAnimeStatusEdit={setAnimeShown}
                    />
                </div>
            </div>
        </div>
    );
};

export default AnimeStatusScreen;
