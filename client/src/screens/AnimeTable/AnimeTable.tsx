import React, { FC, useCallback, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { TableWithSearch } from "components/TableWithSearch";
import { AnimeTableHead } from "./components/AnimeTableHead";
import { AnimeTableRows } from "./components/AnimeTableRows";
import { selectAnimeList, selectNumber, selectSize, selectTotalElements } from "./animeTableSelectors";
import { loadAnimeList, updatePage, updateRowsPerPage, updateSearch } from "./animeTableSlice";

const AnimeTable: FC = () => {
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch(loadAnimeList());
    }, [dispatch]);

    const number = useSelector(selectNumber);
    const size = useSelector(selectSize);
    const totalElements = useSelector(selectTotalElements);
    const content = useSelector(selectAnimeList);

    const handleChangeSearch = useCallback(
        (nextSearch: string) => {
            dispatch(updateSearch(nextSearch));
        },
        [dispatch],
    );
    const handleChangePage = useCallback(
        (nextPage: number) => {
            dispatch(updatePage(nextPage));
        },
        [dispatch],
    );
    const handleChangeRowsPerPage = useCallback(
        (nextRowsPerPage: number) => {
            dispatch(updateRowsPerPage(nextRowsPerPage));
        },
        [dispatch],
    );
    return (
        <TableWithSearch
            head={<AnimeTableHead />}
            number={number}
            size={size}
            totalElements={totalElements}
            onChangeSearch={handleChangeSearch}
            onChangePage={handleChangePage}
            onChangeRowsPerPage={handleChangeRowsPerPage}
        >
            <AnimeTableRows content={content} />
        </TableWithSearch>
    );
};

export default AnimeTable;
