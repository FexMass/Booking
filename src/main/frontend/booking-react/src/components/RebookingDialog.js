import React, { useState } from "react";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";
import BookingForm from "./BookingForm";
import http from "../http-common";
import ErrorDialog from "./ErrorDialog";
import "../styles/RebookingDialog.scss";

const RebookingDialog = ({
  open,
  onClose,
  initialValues,
  bookings,
  setBookings,
}) => {
  const [errorOpen, setErrorOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const handleRebook = (newBooking) => {
    http
      .put(`/bookings/${initialValues.id}`, newBooking)
      .then((response) => {
        setBookings(
          bookings.map((booking) =>
            booking.id === initialValues.id ? response.data : booking,
          ),
        );
        onClose();
      })
      .catch((error) => {
        setErrorMessage("Error creating booking: " + error.response.data.message);
        setErrorOpen(true);
      });
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="xl">
      <DialogTitle>
        <div className="dialog-title">
          <h2>Rebooking</h2>
        </div>
      </DialogTitle>
      <DialogContent>
        <div className="dialog-content">
          <BookingForm
            initialValues={{
              customerName: initialValues?.customerName || "",
              startDateTime: new Date(initialValues?.startDateTime) || null,
              endDateTime: new Date(initialValues?.endDateTime) || null,
            }}
            onSubmit={handleRebook}
            bookings={bookings}
            setBookings={setBookings}
          />
        </div>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="primary" variant="outlined">
          Cancel
        </Button>
      </DialogActions>
      <ErrorDialog
        open={errorOpen}
        onClose={() => setErrorOpen(false)}
        errorMessage={errorMessage}
      />
    </Dialog>
  );
};

export default RebookingDialog;
