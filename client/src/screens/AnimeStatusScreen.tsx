import React, { FC, useCallback, useState } from "react";
import AnimeStatusTable from "./AnimeStatusTable";
import { Box, Button, Modal, Paper } from "@mui/material";
import AnimeSelect from "./AnimeSelect";
import { useDeleteAnimeStatusMutation, useUpdateAnimeStatusMutation } from "../api";
import { AnimeStatusView } from "./AnimeStatusView";

const AnimeStatusScreen: FC = () => {
    const [selectedAnimeStatuses, setSelectedAnimeStatuses] = useState([] as number[]);
    const [showAnimeId, setShowAnimeId] = useState(0);
    const [animeSelectOpen, setAnimeSelectOpen] = useState(false);

    const openAnimeSelect = useCallback(() => setAnimeSelectOpen(true), [setAnimeSelectOpen]);
    const closeAnimeSelect = useCallback(() => setAnimeSelectOpen(false), [setAnimeSelectOpen]);

    const closeAnimeStatus = useCallback(() => setShowAnimeId(0), [setShowAnimeId]);

    const [updateAnimeStatus] = useUpdateAnimeStatusMutation();
    const [deleteAnimeStatus] = useDeleteAnimeStatusMutation();

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
        <>
            <Modal className="search-popup" open={animeSelectOpen} onClose={closeAnimeSelect}>
                <Paper>
                    <AnimeSelect onAnimeSelected={handleAnimeSelected} onSelectCancelled={closeAnimeSelect} />
                </Paper>
            </Modal>
            <Modal className="status-popup" open={showAnimeId > 0} onClose={closeAnimeStatus}>
                <Paper>
                    <AnimeStatusView animeId={showAnimeId} />
                </Paper>
            </Modal>
            <div className="status-screen">
                <Box className="status-table-control">
                    <Button color="primary" onClick={openAnimeSelect}>
                        Add Anime
                    </Button>
                    <Button color="secondary" disabled={!selectedAnimeStatuses.length} onClick={handleDeleteClick}>
                        Delete Anime
                    </Button>
                </Box>
                <AnimeStatusTable onAnimeStatusSelected={setSelectedAnimeStatuses} onAnimeStatusEdit={setShowAnimeId} />
            </div>
        </>
    );
};

export default AnimeStatusScreen;
