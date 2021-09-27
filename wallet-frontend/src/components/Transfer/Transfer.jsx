import React, { useState, useEffect } from "react";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import CssBaseline from "@material-ui/core/CssBaseline";
import TextField from "@material-ui/core/TextField";
import { FcMoneyTransfer } from "react-icons/fc";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";
import Container from "@material-ui/core/Container";
import { useHistory } from "react-router-dom";
import { errorToast, successToast } from "../commons/AlertNotification";
import axios from "axios";
import { validateEmail } from "../commons/EmailValidator";

const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(8),
    padding: theme.spacing(8, 0, 12),
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
  },
  form: {
    width: "100%", // Fix IE 11 issue.
    marginTop: theme.spacing(1),
  },
  sendButton: {
    margin: theme.spacing(3, 0, 2),
  },
  cancelButton: {
    margin: theme.spacing(0, 0, 2),
  },
}));

export default function Transfer({ userEmail, getUserDetails, userBalance }) {
  const [transferTransaction, setTransferTransaction] = useState({
    userEmail: userEmail,
    receiverEmail: "",
    transactionAmount: 0,
    transactionType: "DEBIT",
  });
  const classes = useStyles();
  const history = useHistory();

  useEffect(() => {
    document.title = "Wallet | Transfer";
  }, []);

  const handletransferButtonOnClick = () => {
    if (!validateEmail(transferTransaction.receiverEmail)) {
      errorToast("Invalid Receiver Email Address");
    }
    if (transferTransaction.receiverEmail === transferTransaction.userEmail) {
      errorToast("User and Receiver can't be same!!");
    }
    if (
      transferTransaction.transactionAmount === null ||
      transferTransaction.transactionAmount <= 0
    ) {
      errorToast("Invalid Transaction Amount");
    }
    if (transferTransaction.transactionAmount > userBalance) {
      errorToast("Insufficient funds!");
    } else {
      axios
        .post(
          "http://localhost:8080/api/transaction/transfer",
          transferTransaction
        )
        .then((res) => {
          if (res.data === "TRANSACTION_SUCCESS") {
            successToast(
              `Money Transfer Successful With Amount ₹${transferTransaction.transactionAmount}!`
            );
            setTransferTransaction({
              ...transferTransaction,
              receiverEmail: "",
              transactionAmount: 0,
            });
          } else {
            errorToast("Money Transfer Failed!");
          }
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <FcMoneyTransfer />
        </Avatar>
        <Typography component="h1" variant="h5">
          Transfer Money
        </Typography>
        <form className={classes.form} noValidate>
          <TextField
            value={transferTransaction.receiverEmail}
            variant="outlined"
            margin="normal"
            required
            fullWidth
            id="email"
            label="Receiver Address"
            name="email"
            autoComplete="email"
            onChange={(event) => {
              setTransferTransaction({
                ...transferTransaction,
                receiverEmail: event.target.value,
              });
            }}
            autoFocus
          />
          <TextField
            value={transferTransaction.transactionAmount}
            variant="outlined"
            margin="normal"
            required
            fullWidth
            id="recharge_amount"
            label="Amount"
            type="number"
            InputLabelProps={{
              shrink: true,
            }}
            onChange={(event) => {
              event.target.value < 0
                ? setTransferTransaction({
                    ...transferTransaction,
                    transactionAmount: 0,
                  })
                : setTransferTransaction({
                    ...transferTransaction,
                    transactionAmount: event.target.value,
                  });
            }}
          />
          <Button
            fullWidth
            variant="contained"
            color="primary"
            className={classes.sendButton}
            onClick={handletransferButtonOnClick}
          >
            Send
          </Button>
          <Button
            fullWidth
            variant="contained"
            color="secondary"
            className={classes.cancelButton}
            onClick={() => history.push("/home")}
          >
            Cancel
          </Button>
        </form>
      </div>
    </Container>
  );
}
