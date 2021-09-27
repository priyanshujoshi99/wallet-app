import React, { useState, useEffect } from "react";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import CssBaseline from "@material-ui/core/CssBaseline";
import TextField from "@material-ui/core/TextField";
import { GiReceiveMoney } from "react-icons/gi";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";
import Container from "@material-ui/core/Container";
import { useHistory } from "react-router-dom";
import { errorToast, successToast } from "../commons/AlertNotification";
import axios from "axios";

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
  rechargeButton: {
    margin: theme.spacing(3, 0, 2),
  },
  cancelButton: {
    margin: theme.spacing(0, 0, 2),
  },
}));

export default function Recharge({ userEmail, getUserDetails }) {
  const [rechargeTransaction, setRechargeTransaction] = useState({
    userEmail: userEmail,
    transactionAmount: 0,
    transactionType: "CREDIT_BY_RECHARGE",
  });
  const classes = useStyles();
  const history = useHistory();

  useEffect(() => {
    document.title = "Wallet | Recharge";
  }, []);

  const handleRechargeButtonOnClick = () => {
    axios
      .post(
        "http://localhost:8080/api/transaction/recharge",
        rechargeTransaction
      )
      .then((res) => {
        if (res.data === "TRANSACTION_SUCCESS") {
          successToast(
            `Recharge successful with amount ₹${rechargeTransaction.transactionAmount}!`
          );
          rechargeTransaction.transactionAmount >= 100 &&
            successToast(
              `Cashback received of amount ₹${
                0.1 * rechargeTransaction.transactionAmount
              }!`
            );
          getUserDetails(rechargeTransaction.userEmail);
          setRechargeTransaction({
            ...rechargeTransaction,
            transactionAmount: 0,
          });
        } else {
          errorToast(`Recharge Failed!`);
        }
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <GiReceiveMoney />
        </Avatar>
        <Typography component="h1" variant="h5">
          Wallet Recharge
        </Typography>
        <form className={classes.form} noValidate>
          <TextField
            value={rechargeTransaction.transactionAmount}
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
                ? setRechargeTransaction({
                    ...rechargeTransaction,
                    transactionAmount: 0,
                  })
                : setRechargeTransaction({
                    ...rechargeTransaction,
                    transactionAmount: event.target.value,
                  });
            }}
            helperText="10% cashback on recharge of ₹100 or more"
            autoFocus
          />
          <Button
            fullWidth
            variant="contained"
            color="primary"
            className={classes.rechargeButton}
            onClick={handleRechargeButtonOnClick}
          >
            Recharge
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
