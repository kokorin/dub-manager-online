import React, { FC, useState } from "react";
import { Button } from "@material-ui/core";
import { AnimeTable } from "./AnimeTable";

interface OwnProps {
    onAnimeSelected: (animeIds: number[]) => void;
    onSelectCancelled: () => void;
}

const AnimeSelect: FC<OwnProps> = (props) => {
    const [selectedAnimeIds, setSelectedAnimeIds] = useState([] as number[]);

    return (
        <div className="anime-selector">
            <AnimeTable onAnimeSelected={setSelectedAnimeIds} />
            <div className="anime-selector-controls">
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
            </div>
        </div>
    );
};

export default AnimeSelect;
