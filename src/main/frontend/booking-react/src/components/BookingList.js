import React, { useState } from "react";
import http from "../http-common";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import Button from "@mui/material/Button";
import ButtonGroup from "@mui/material/ButtonGroup";
import Stack from "@mui/material/Stack";
import RebookingDialog from "./RebookingDialog";
import { makeStyles } from "@mui/styles";
import classNames from "classnames";
import "../styles/BookingList.scss";

export const useStyles = makeStyles(() => ({
  root: {
    "& .MuiDataGrid-iconSeparator": {
      display: "none",
    },
    "& .MuiDataGrid-columnHeader, .MuiDataGrid-cell": {
      borderRight: "1px solid #e0e0e0",
    },
    "& .MuiDataGrid-columnsContainer, .MuiDataGrid-cell": {
      borderBottom: "1px solid #e0e0e0",
    },
    "& .MuiDataGrid-row:hover": {
      backgroundColor: "#d6e9f9 !important",
    },
  },
}));

const BookingsList = ({ bookings, setBookings }) => {
  const [rebookingDialogOpen, setRebookingDialogOpen] = useState(false);
  const [selectedBooking, setSelectedBooking] = useState(null);
  const classes = useStyles();

  const handleRebook = (booking) => {
    console.log(booking);
    setSelectedBooking(booking);
    setRebookingDialogOpen(true);
  };

  const handleCloseRebookingDialog = () => {
    setRebookingDialogOpen(false);
    setSelectedBooking(null);
  };

  const handleDelete = (id) => {
    http
      .delete(`/bookings/${id}`)
      .then(() => setBookings(bookings.filter((booking) => booking.id !== id)))
      .catch((error) => console.error("Error deleting booking:", error));
  };

  const bookingsWithId = bookings.map((booking) => ({ ...booking, id: booking.id }));

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
    { field: "customerName", headerName: "Name", width: 200 },
    {
      field: "actions",
      headerName: "Actions",
      width: 300,
      renderCell: (params) => (
        <Stack direction="row" spacing={1}>
          <ButtonGroup variant="contained">
            <Button color="primary" onClick={() => handleRebook(params.row)}>
              Rebook
            </Button>
            <Button color="error" onClick={() => handleDelete(params.row.id)}>
              Cancel
            </Button>
          </ButtonGroup>
        </Stack>
      ),
    },
  ];

  return (
    <div className={classNames(classes.root, "bookings-list")}>
      <DataGrid
        rows={bookingsWithId}
        columns={columns}
        getRowId={(row) => row.id}
        pageSize={5}
        pagination
        rowsPerPageOptions={[5, 10, 20]}
        components={{
          Toolbar: GridToolbar,
        }}
      />
      <RebookingDialog
        open={rebookingDialogOpen}
        onClose={handleCloseRebookingDialog}
        initialValues={selectedBooking}
        bookings={bookings}
        setBookings={setBookings}
      />
    </div>
  );
};

export default BookingsList;
