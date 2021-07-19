import React, { FC, useCallback } from "react";
import SearchIcon from "@material-ui/icons/Search";
import InputBase from "@material-ui/core/InputBase";

interface OwnProps {
    text: string;
    onChangeSearch: (newSearch: string) => void;
}

const Search: FC<OwnProps> = (props: OwnProps) => {
    const handleSearchChange = useCallback(
        (event: React.ChangeEvent<HTMLInputElement>) => props.onChangeSearch(event.target.value),
        [props],
    );
    return (
        <div>
            <SearchIcon />
            <InputBase placeholder="Search…" onChange={handleSearchChange} value={props.text} />
        </div>
    );
};

export default Search;
