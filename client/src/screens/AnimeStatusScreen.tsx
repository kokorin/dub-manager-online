import React, { FC, useCallback, useState } from "react";
import AnimeStatusTable from "./AnimeStatusTable";
import { Button, Modal, Paper } from "@mui/material";
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
            <Modal open={animeSelectOpen} onClose={closeAnimeSelect}>
                <Paper className="padding-4em">
                    <AnimeSelect
                        className="height-100"
                        onAnimeSelected={handleAnimeSelected}
                        onSelectCancelled={closeAnimeSelect}
                    />
                </Paper>
            </Modal>
            <Modal open={showAnimeId > 0} onClose={closeAnimeStatus}>
                <Paper className="padding-4em">
                    <AnimeStatusView className="height-100" animeId={showAnimeId} />
                </Paper>
            </Modal>
            <div className="flex-column height-100">
                <div className="flex">
                    <Button color="primary" onClick={openAnimeSelect}>
                        Add Anime
                    </Button>
                    <Button color="secondary" disabled={!selectedAnimeStatuses.length} onClick={handleDeleteClick}>
                        Delete Anime
                    </Button>
                </div>
                <AnimeStatusTable
                    className="flex-grow"
                    onAnimeStatusSelected={setSelectedAnimeStatuses}
                    onAnimeStatusEdit={setShowAnimeId}
                />
            </div>
        </>
    );
};

export default AnimeStatusScreen;
