import React, { useState, useEffect } from "react";
import Avatar from "@material-ui/core/Avatar";
import Button from "@material-ui/core/Button";
import CssBaseline from "@material-ui/core/CssBaseline";
import TextField from "@material-ui/core/TextField";
import Grid from "@material-ui/core/Grid";
import LockOutlinedIcon from "@material-ui/icons/LockOutlined";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from "@material-ui/core/styles";
import Container from "@material-ui/core/Container";
import { errorToast, successToast } from "../commons/AlertNotification";
import "react-toastify/dist/ReactToastify.css";
import axios from "axios";
import { NavLink } from "react-router-dom";
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
    padding: theme.spacing(8, 0, 6),
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
  },
  form: {
    width: "100%",
    marginTop: theme.spacing(3),
  },
  signUpButton: {
    margin: theme.spacing(3, 0, 2),
  },
  logInLink: {
    cursor: "pointer",
    color: "black",
    textDecoration: "none",
  },
}));

export default function SignUp() {
  const [userDetails, setUserDetails] = useState({
    userFirstName: "",
    userLastName: "",
    userEmail: "",
    userPassword: "",
  });

  const classes = useStyles();

  useEffect(() => {
    document.title = "Wallet | Sign Up";
  }, []);

  const handleRegisterButtonOnClick = () => {
    if (
      userDetails.userFirstName === "" ||
      userDetails.userLastName === "" ||
      !validateEmail(userDetails.userEmail) ||
      userDetails.userPassword === ""
    ) {
      if (userDetails.userFirstName === "") {
        errorToast("Invalid First Name");
      }
      if (userDetails.userLastName === "") {
        errorToast("Invalid Last Name");
      }
      if (!validateEmail(userDetails.userEmail)) {
        errorToast("Invalid Email");
      }
      if (userDetails.userPassword === "") {
        errorToast("Invalid Password");
      }
      return;
    }
    axios
      .post(`http://localhost:8080/api/user/register`, userDetails)
      .then((res) => {
        if (res.data === "USER_SAVED") {
          setUserDetails({
            userFirstName: "",
            userLastName: "",
            userEmail: "",
            userPassword: "",
          });
          successToast("User Registered");
          return;
        }
        errorToast("User with the same email already exists!");
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
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign up
          </Typography>
          <form className={classes.form} noValidate>
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <TextField
                  autoComplete="fname"
                  value={userDetails.userFirstName}
                  name="firstName"
                  variant="outlined"
                  required
                  fullWidth
                  id="userFirstName"
                  label="First Name"
                  onChange={(event) =>
                    setUserDetails({
                      ...userDetails,
                      userFirstName: event.target.value,
                    })
                  }
                  autoFocus
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  value={userDetails.userLastName}
                  variant="outlined"
                  required
                  fullWidth
                  id="lastName"
                  label="Last Name"
                  name="lastName"
                  autoComplete="lname"
                  onChange={(event) =>
                    setUserDetails({
                      ...userDetails,
                      userLastName: event.target.value,
                    })
                  }
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  value={userDetails.userEmail}
                  variant="outlined"
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
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  value={userDetails.userPassword}
                  variant="outlined"
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
              </Grid>
            </Grid>
            <Button
              fullWidth
              variant="contained"
              color="primary"
              className={classes.signUpButton}
              onClick={handleRegisterButtonOnClick}
            >
              Sign Up
            </Button>
            <Grid container justifyContent="flex-end">
              <Grid item>
                <NavLink className={classes.logInLink} to="/">
                  Already have an account? Log in
                </NavLink>
              </Grid>
            </Grid>
          </form>
        </div>
      </Container>
    </React.Fragment>
  );
}
