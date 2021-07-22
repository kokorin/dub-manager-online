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
        <div style={{ height: "100%", width: "100%" }}>
            <AnimeTable onAnimeSelected={setSelectedAnimeIds} />
            <div>
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
