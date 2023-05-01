import React from "react";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { isDateBlocked } from "../utils/dateUtils";
import FormHelperText from "@mui/material/FormHelperText";

const CustomDatePicker = ({
  label,
  value,
  onChange,
  blocks,
  required,
  error,
  helperText,
}) => {
  const shouldDisableDate = (date) => {
    return isDateBlocked(date, blocks);
  };

  const handleDateChange = (date) => {
    const dateWithoutTime = new Date(date.setHours(0, 0, 0, 0));
    onChange(dateWithoutTime);
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDateFns}>
      <div>
        <DatePicker
          label={label}
          inputFormat="MM/dd/yyyy"
          value={value}
          onChange={handleDateChange}
          textFieldProps={{
            required: required,
            error: error,
          }}
          shouldDisableDate={blocks ? shouldDisableDate : undefined}
        />
        {error && <FormHelperText error>{helperText}</FormHelperText>}
      </div>
    </LocalizationProvider>
  );
};

export default CustomDatePicker;
