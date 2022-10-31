import React, { FC, useState } from "react";
import { Button } from "@mui/material";
import { AnimeTable } from "./AnimeTable";
import { CommonProps } from "@mui/material/OverridableComponent";

interface OwnProps extends CommonProps {
    onAnimeSelected: (animeIds: number[]) => void;
    onSelectCancelled: () => void;
}

const AnimeSelect: FC<OwnProps> = (props) => {
    const [selectedAnimeIds, setSelectedAnimeIds] = useState([] as number[]);

    return (
        <div className={`flex-column ${props.className}`} style={props.style}>
            <AnimeTable className="flex-grow" onAnimeSelected={setSelectedAnimeIds} />
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
