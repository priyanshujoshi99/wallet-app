import React, { useEffect } from "react";
import HomeCard from "./HomeCard";
import Grid from "@material-ui/core/Grid";
import { makeStyles } from "@material-ui/core/styles";
import Typography from "@material-ui/core/Typography";
import Container from "@material-ui/core/Container";
import Avatar from "@material-ui/core/Avatar";
import { AiFillHome } from "react-icons/ai";
import CssBaseline from "@material-ui/core/CssBaseline";

const useStyles = makeStyles((theme) => ({
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
  cardsDiv: {
    padding: theme.spacing(0, 0, 8),
  },
}));

export default function Home({ userFirstName, userBalance }) {
  const classes = useStyles();
  const userBalnceFormatted = userBalance
    .toFixed(2)
    .replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");

  useEffect(() => {
    document.title = "Wallet | Home";
  }, []);

  return (
    <React.Fragment>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <div className={classes.paper}>
          <Avatar className={classes.avatar}>
            <AiFillHome />
          </Avatar>
          <Typography component="h1" variant="h5">
            Hello {userFirstName}!
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
              Current Balance:
              <br />₹{userBalnceFormatted}
            </span>
          </Typography>
        </div>
      </Container>

      <Container maxWidth="md" component="main">
        <div className={classes.cardsDiv}>
          <Grid container>
            <Grid item xs={12} sm={4}>
              <HomeCard
                cardTitle="Recharge and Add Money To Wallet"
                cardLinkTitle="Recharge"
                to="/recharge"
              />
            </Grid>
            <Grid item xs={12} sm={4}>
              <HomeCard
                cardTitle="Transfer Money To Other's Wallet"
                cardLinkTitle="Transfer Money"
                to="/transfer"
              />
            </Grid>
            <Grid item xs={12} sm={4}>
              <HomeCard
                cardTitle="View Your Wallet's Account Statement"
                cardLinkTitle="View Transactions"
                to="/viewTransactions"
              />
            </Grid>
          </Grid>
        </div>
      </Container>
    </React.Fragment>
  );
}
