import React from "react";
import http from "../http-common";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import Button from "@mui/material/Button";
import "../styles/BlockList.scss"
import classNames from "classnames";
import { useStyles } from './BookingList'

const BlocksList = ({ blocks, setBlocks }) => {
  const classes = useStyles();

  const handleDelete = (id) => {
    http
      .delete(`/blocks/${id}`)
      .then(() => setBlocks(blocks.filter((block) => block.id !== id)))
      .catch((error) => console.error("Error deleting block:", error));
  };

  const blocksWithId = blocks.map((blocks) => ({ ...blocks, id: blocks.id }));

  const columns = [
    {
      field: "id",
      headerName: "ID",
      width: 100,
      renderCell: (params) => <strong>{params.row.id}</strong>,
    },
    {
      field: "startDateTime",
      headerName: "Start",
      width: 200,
      valueFormatter: (params) =>
        new Date(params.value).toLocaleDateString("en-US"),
    },
    {
      field: "endDateTime",
      headerName: "End",
      width: 200,
      valueFormatter: (params) =>
        new Date(params.value).toLocaleDateString("en-US"),
    },
    {
      field: "actions",
      headerName: "Actions",
      width: 300,
      renderCell: (params) => (
        <Button
          variant="contained"
          color="error"
          onClick={() => handleDelete(params.row.id)}
        >
          Delete
        </Button>
      ),
    },
  ];

  return (
    <div className={classNames(classes.root, "block-list")}>
      <DataGrid
        rows={blocksWithId}
        columns={columns}
        getRowId={(row) => row.id}
        pageSize={5}
        pagination
        rowsPerPageOptions={[5, 10, 20]}
        components={{
          Toolbar: GridToolbar,
        }}
      />
    </div>
  );
};

export default BlocksList;