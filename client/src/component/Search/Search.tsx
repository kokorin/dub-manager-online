import React, { FC, useCallback } from "react";
import SearchIcon from "@material-ui/icons/Search";
import InputBase from "@material-ui/core/InputBase";

interface OwnProps {
    onChangeSearch: (newSearch: string) => void;
}

const Search: FC<OwnProps> = ({ onChangeSearch }) => {
    const handleSearchChange = useCallback(
        (event: React.ChangeEvent<HTMLInputElement>) => onChangeSearch(event.target.value),
        [onChangeSearch]
    );
    return (
        <div>
            <SearchIcon />
            <InputBase placeholder="Search…" onChange={handleSearchChange} />
        </div>
    );
};

export default Search;
