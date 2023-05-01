import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Button,
} from "@mui/material";
import "../styles/ErrorDialog.scss";

const ErrorDialog = ({ open, onClose, errorMessage }) => {
  return (
    <Dialog open={open} onClose={onClose} className="error-dialog">
      <DialogTitle>Error</DialogTitle>
      <DialogContent>
        <DialogContentText>{errorMessage}</DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Close</Button>
      </DialogActions>
    </Dialog>
  );
};

export default ErrorDialog;
