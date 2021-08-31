import React, { FC, useCallback, useState } from "react";
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

    const openAnimeSelect = useCallback(() => setAnimeSelectOpen(true), [setAnimeSelectOpen]);
    const closeAnimeSelect = useCallback(() => setAnimeSelectOpen(false), [setAnimeSelectOpen]);

    const closeAnimeStatus = useCallback(() => setAnimeShown(null as unknown as AnimeStatus), [setAnimeShown]);

    const [updateAnimeStatus, { isLoading: isUpdating }] = useUpdateAnimeStatusMutation();
    const [deleteAnimeStatus, { isLoading: isDeleting }] = useDeleteAnimeStatusMutation();

    const handleAnimeSelected = useCallback(
        (animeIds: number[]) => {
            animeIds.forEach((animeId: number) => {
                updateAnimeStatus({ id: animeId, updateAnimeStatusDto: { progress: "IN_PROGRESS", comment: "" } });
            });
            setAnimeSelectOpen(false);
        },
        [updateAnimeStatus, setAnimeSelectOpen],
    );

    const handleDeleteClick = useCallback(
        () => selectedAnimeStatuses.forEach((animeId) => deleteAnimeStatus({ id: animeId })),
        [selectedAnimeStatuses, deleteAnimeStatus],
    );

    return (
        <div className="status-screen">
            <Modal className="loader-popup" open={isUpdating || isDeleting}>
                <CircularProgress />
            </Modal>
            <Modal className="search-popup" open={animeSelectOpen} onClose={closeAnimeSelect}>
                <Paper>
                    <AnimeSelect onAnimeSelected={handleAnimeSelected} onSelectCancelled={closeAnimeSelect} />
                </Paper>
            </Modal>
            <Modal className="status-popup" open={!!animeShown} onClose={closeAnimeStatus}>
                <Paper>
                    <AnimeStatusView animeStatus={animeShown} />
                </Paper>
            </Modal>
            <div className="status-table-view">
                <Box className="status-table-control">
                    <Button color="primary" onClick={openAnimeSelect}>
                        Add Anime
                    </Button>
                    <Button color="secondary" disabled={!selectedAnimeStatuses.length} onClick={handleDeleteClick}>
                        Delete Anime
                    </Button>
                </Box>
                <AnimeStatusTable onAnimeStatusSelected={setSelectedAnimeStatuses} onAnimeStatusEdit={setAnimeShown} />
            </div>
        </div>
    );
};

export default AnimeStatusScreen;
