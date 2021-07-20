import React, { FC, useState } from "react";
import { useFindAnimeStatusesQuery } from "../api";
import Loader from "../components/Loader";
import GridWithSearch from "../components/GridWithSearch";
import { GridColDef } from "@material-ui/data-grid";

const AnimeStatusTable: FC = () => {
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const { data, isLoading } = useFindAnimeStatusesQuery({ page, size: pageSize });

    if (!data || isLoading) {
        return <Loader />;
    }

    const columns: GridColDef[] = [{ field: "id", headerName: "ID" }];
    return (
        <GridWithSearch
            columns={columns}
            page={page}
            pageSize={pageSize}
            rows={data.content}
            rowCount={data.totalElements}
            onPageChange={setPage}
            onPageSizeChange={setPageSize}
        />
    );
};

export default AnimeStatusTable;
