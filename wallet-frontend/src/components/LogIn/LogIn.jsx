import React, { useState, useEffect } from "react";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import CssBaseline from "@material-ui/core/CssBaseline";
import TextField from "@material-ui/core/TextField";
import Grid from "@material-ui/core/Grid";
import { BiLogInCircle } from "react-icons/bi";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";
import Container from "@material-ui/core/Container";
import { errorToast } from "../commons/AlertNotification";
import { NavLink } from "react-router-dom";
import axios from "axios";
import { validateEmail } from "../commons/EmailValidator";

const useStyles = makeStyles((theme) => ({
  "@global": {
    ul: {
      margin: 0,
      padding: 0,
      listStyle: "none",
    },
  },
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
  logInButton: {
    margin: theme.spacing(3, 0, 2),
  },
  signUpLink: {
    cursor: "pointer",
    color: "black",
    textDecoration: "none",
  },
}));

export default function LogIn({ getUserDetails }) {
  const [userDetails, setUserDetails] = useState({
    userEmail: "",
    userPassword: "",
  });

  const classes = useStyles();

  useEffect(() => {
    document.title = "Wallet | Log In";
  }, []);

  const handleLogInButtonOnClick = () => {
    if (
      !validateEmail(userDetails.userEmail) ||
      userDetails.userPassword === ""
    ) {
      if (!validateEmail(userDetails.userEmail)) {
        errorToast("Invalid Email");
      }
      if (userDetails.userPassword === "") {
        errorToast("Invalid Password");
      }
      return;
    }
    axios
      .put("http://localhost:8080/api/user/login", userDetails)
      .then((res) => {
        if (res.data === "LOGGED_IN") {
          getUserDetails(userDetails.userEmail);
        } else if (res.data === "ALREADY_LOGGED_IN") {
          errorToast("User Already Logged In!");
        } else {
          errorToast("Invalid User Email or Password!");
        }
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <React.Fragment>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <div className={classes.paper}>
          <Avatar className={classes.avatar}>
            <BiLogInCircle />
          </Avatar>
          <Typography component="h1" variant="h5">
            Log in
          </Typography>
          <form className={classes.form} noValidate>
            <TextField
              value={userDetails.userEmail}
              variant="outlined"
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              onChange={(event) =>
                setUserDetails({
                  ...userDetails,
                  userEmail: event.target.value,
                })
              }
              autoFocus
            />
            <TextField
              value={userDetails.userPassword}
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
              onChange={(event) =>
                setUserDetails({
                  ...userDetails,
                  userPassword: event.target.value,
                })
              }
            />
            <Button
              fullWidth
              variant="contained"
              color="primary"
              className={classes.logInButton}
              onClick={handleLogInButtonOnClick}
            >
              Log In
            </Button>
            <Grid container justifyContent="flex-end">
              <Grid item>
                <NavLink className={classes.signUpLink} to="/signUp">
                  Don't have an account? Sign Up
                </NavLink>
              </Grid>
            </Grid>
          </form>
        </div>
      </Container>
    </React.Fragment>
  );
}
