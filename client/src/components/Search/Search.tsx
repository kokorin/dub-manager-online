import React, { FC, useEffect, useState } from "react";
import { TextField } from "@material-ui/core";

interface OwnProps {
    text: string;
    label?: string;
    onChangeSearch: (newSearch: string) => void;
    onChangeDelay?: number;
}

const Search: FC<OwnProps> = (props: OwnProps) => {
    const [timeoutId, setTimeoutId] = useState(0);

    useEffect(
        () => () => {
            if (timeoutId) {
                window.clearTimeout(timeoutId);
            }
        },
        [props, timeoutId],
    );

    const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const text = event.target.value;
        if (timeoutId) {
            window.clearTimeout(timeoutId);
        }
        const newTimeoutId = window.setTimeout(() => props.onChangeSearch(text), props.onChangeDelay || 1500);

        setTimeoutId(newTimeoutId);
    };

    return (
        <div>
            <TextField
                type="search"
                label={props.label}
                onChange={handleSearchChange}
                autoFocus={true}
                fullWidth={true}
                defaultValue={props.text}
            />
        </div>
    );
};
export default Search;
