import React, { FC, useState } from "react";
import { Box, Button } from "@material-ui/core";
import { AnimeTable } from "./AnimeTable";

interface OwnProps {
    onAnimeSelected: (animeIds: number[]) => void;
    onSelectCancelled: () => void;
}

const AnimeSelect: FC<OwnProps> = (props) => {
    const [selectedAnimeIds, setSelectedAnimeIds] = useState([] as number[]);

    return (
        <div className="anime-selector" style={{ height: "80%", width: "100%" }}>
            <AnimeTable style={{ height: "100%", flexGrow: 1 }} onAnimeSelected={setSelectedAnimeIds} />
            <Box className="anime_select_buttons" style={{ width: "100%" }} display="flex" justifyContent="center">
                <Button
                    color="primary"
                    onClick={() => props.onAnimeSelected(selectedAnimeIds)}
                    disabled={!selectedAnimeIds.length}
                >
                    Confirm
                </Button>
                <Button color="secondary" onClick={props.onSelectCancelled}>
                    Cancel
                </Button>
            </Box>
        </div>
    );
};

export default AnimeSelect;
