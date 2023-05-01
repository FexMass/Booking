import React, { useState, useEffect, useCallback } from "react";
import http from "../http-common";
import CustomDatePicker from "./DatePicker";
import Button from "@mui/material/Button";
import Grid from "@mui/material/Grid";
import ErrorDialog from "./ErrorDialog";
import { formatDate } from "../utils/dateUtils";
import "../styles/BlockForm.scss";

const BlockForm = ({ blocks, setBlocks, initialValues, onSubmit }) => {
  const [startDateTime, setStart] = useState(null);
  const [endDateTime, setEnd] = useState(null);
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
  }, [endDateTime, startDateTime]);

  useEffect(() => {
    validateFields();
  }, [startDateTime, endDateTime, validateFields]);

  useEffect(() => {
    if (initialValues) {
      setStart(initialValues.startDateTime);
      setEnd(initialValues.endDateTime);
    }
  }, [initialValues]);

  const handleSubmit = (e) => {
    e.preventDefault();
    setSubmitted(true);

    if (validateFields()) {
      console.log(startDateTime, endDateTime);
      const newBlock = {
        startDateTime: formatDate(startDateTime),
        endDateTime: formatDate(endDateTime),
      };
      if (onSubmit) {
        onSubmit(newBlock);
      } else {
        http
          .post("/blocks", newBlock)
          .then((response) => {
            setBlocks([...blocks, response.data]);
            setStart(null);
            setEnd(null);
            setSubmitted(false);
          })
          .catch((error) => {
            setErrorMessage(
              "Error creating Block: " + error.response.data.message,
            );
            setErrorOpen(true);
          });
      }
    }
  };

  return (
    <form onSubmit={handleSubmit} className="block-form">
      <Grid container spacing={2}>
        <Grid item xs={12} sm={4}>
          <CustomDatePicker
            label="Start Date"
            value={startDateTime}
            onChange={(date) => setStart(date)}
            blocks={blocks}
            error={submitted && !!errors.startDateTime}
            helperText={submitted && errors.startDateTime}
          />
        </Grid>
        <Grid item xs={12} sm={4}>
          <CustomDatePicker
            label="End Date"
            value={endDateTime}
            onChange={(date) => setEnd(date)}
            blocks={blocks}
            error={submitted && !!errors.endDateTime}
            helperText={submitted && errors.endDateTime}
          />
        </Grid>
        <Grid item xs={12} sm={4}>
          <Button type="submit" variant="contained" color="primary">
            Create Block
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

export default BlockForm;
