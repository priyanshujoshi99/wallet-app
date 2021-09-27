import React from "react";
import Avatar from "@material-ui/core/Avatar";
import CssBaseline from "@material-ui/core/CssBaseline";
import { GiCash } from "react-icons/gi";
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
}));

export default function LogIn() {
  const classes = useStyles();

  return (
    <React.Fragment>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <div className={classes.paper}>
          <Avatar className={classes.avatar}>
            <GiCash />
          </Avatar>
          <Typography component="h1" variant="h5">
            Cashbacks
          </Typography>
        </div>
      </Container>
    </React.Fragment>
  );
}
