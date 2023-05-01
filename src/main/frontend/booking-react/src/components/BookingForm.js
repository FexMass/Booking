import React, { useState, useEffect, useCallback } from "react";
import http from "../http-common";
import CustomDatePicker from "./DatePicker";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Grid from "@mui/material/Grid";
import ErrorDialog from "./ErrorDialog";
import "../styles/BookingForm.scss";
import { formatDate } from "../utils/dateUtils";

const BookingForm = ({
  bookings,
  setBookings,
  initialValues,
  onSubmit,
  blocks,
}) => {
  const [startDateTime, setStart] = useState(null);
  const [endDateTime, setEnd] = useState(null);
  const [customerName, setName] = useState("");
  const [errors, setErrors] = useState({
    customerName: "",
    startDateTime: "",
    endDateTime: "",
  });
  const [submitted, setSubmitted] = useState(false);
  const [errorOpen, setErrorOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const validateFields = useCallback(() => {
    const newErrors = { customerName: "", startDateTime: "", endDateTime: "" };

    if (!customerName.trim()) {
      newErrors.customerName = "Name is required";
    }
    if (!startDateTime) {
      newErrors.startDateTime = "Start date is required";
    }
    if (!endDateTime) {
      newErrors.endDateTime = "End date is required";
    } else if (endDateTime <= startDateTime) {
      newErrors.endDateTime = "End date must be after start date";
    }

    setErrors(newErrors);
    return Object.values(newErrors).every((error) => error === "");
  }, [customerName, endDateTime, startDateTime]);

  useEffect(() => {
    validateFields();
  }, [customerName, startDateTime, endDateTime, validateFields]);

  useEffect(() => {
    if (initialValues) {
      setStart(initialValues.startDateTime);
      setEnd(initialValues.endDateTime);
      setName(initialValues.customerName);
    }
  }, [initialValues]);

  const handleSubmit = (e) => {
    e.preventDefault();
    setSubmitted(true);
    if (validateFields()) {
      const newBooking = {
        startDateTime: formatDate(startDateTime),
        endDateTime: formatDate(endDateTime),
        customerName,
      };
      if (onSubmit) {
        onSubmit(newBooking);
      } else {
        http
          .post("/bookings", newBooking)
          .then((response) => {
            setBookings([...bookings, response.data]);
            setStart(null);
            setEnd(null);
            setName("");
            setSubmitted(false);
          })
          .catch((error) => {
            setErrorMessage(
              "Error creating booking: " + error.response.data.message,
            );
            setErrorOpen(true);
          });
      }
    }
  };

  return (
    <form onSubmit={handleSubmit} className="booking-form">
      <Grid container spacing={2}>
        <Grid item xs={12} sm={6} md={4}>
          <CustomDatePicker
            label="Start Date"
            value={startDateTime}
            onChange={(date) => setStart(date)}
            blocks={blocks}
            error={submitted && !!errors.startDateTime}
            helperText={submitted && errors.startDateTime}
          />
        </Grid>
        <Grid item xs={12} sm={6} md={4}>
          <CustomDatePicker
            label="End Date"
            value={endDateTime}
            onChange={(date) => setEnd(date)}
            blocks={blocks}
            error={submitted && !!errors.endDateTime}
            helperText={submitted && errors.endDateTime}
          />
        </Grid>
        <Grid item xs={12} sm={2}>
          <TextField
            label="Customer Name"
            value={customerName}
            onChange={(e) => setName(e.target.value)}
            variant="outlined"
            fullWidth
            error={submitted && !!errors.customerName}
            helperText={submitted && errors.customerName}
          />
        </Grid>
        <Grid item xs={12} sm={2}>
          <Button type="submit" variant="contained" color="primary">
            {initialValues ? "Update Booking" : "Create Booking"}
          </Button>
        </Grid>
      </Grid>
      <ErrorDialog
        open={errorOpen}
        onClose={() => setErrorOpen(false)}
        errorMessage={errorMessage}
      />
    </form>
  );
};

export default BookingForm;
