import React, { FC } from "react";
import { TableCell, TableRow } from "@material-ui/core";

const AnimeTableHead: FC = () => (
    <TableRow>
        <TableCell>ID</TableCell>
        <TableCell align="center">TYPE</TableCell>
        <TableCell align="center">TITLE</TableCell>
    </TableRow>
);

export default AnimeTableHead;
