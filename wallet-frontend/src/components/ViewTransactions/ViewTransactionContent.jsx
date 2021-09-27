import React from "react";
import Avatar from "@material-ui/core/Avatar";
import CssBaseline from "@material-ui/core/CssBaseline";
import { GrTransaction } from "react-icons/gr";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";
import Container from "@material-ui/core/Container";

const useStyles = makeStyles((theme) => ({
  "@global": {
    ul: {
      margin: 0,
      padding: 0,
      listStyle: "none",
    },
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
  },
  paper: {
    marginTop: theme.spacing(8),
    padding: theme.spacing(8, 0, 8),
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  currentBalanceDiv: {
    padding: theme.spacing(0, 0, 10),
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
}));

export default function LogIn({ userBalance }) {
  const userBalnceFormatted = userBalance
    .toFixed(2)
    .replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
  const classes = useStyles();

  return (
    <React.Fragment>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <div className={classes.paper}>
          <Avatar className={classes.avatar}>
            <GrTransaction />
          </Avatar>
          <Typography component="h1" variant="h5">
            View Transactions
          </Typography>
        </div>

        <div className={classes.currentBalanceDiv}>
          <Typography
            component="h3"
            variant="h4"
            align="center"
            color="textPrimary"
            gutterBottom
          >
            <span>
              Current Balance: <br />₹{userBalnceFormatted}
            </span>
          </Typography>
        </div>
      </Container>
    </React.Fragment>
  );
}
