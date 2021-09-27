import React, { useState } from "react";
import { makeStyles, useTheme } from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import IconButton from "@material-ui/core/IconButton";
import useMediaQuery from "@material-ui/core/useMediaQuery";
import MenuIcon from "@material-ui/icons/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import Menu from "@material-ui/core/Menu";
import { errorToast, successToast } from "./AlertNotification";
import { NavLink } from "react-router-dom";
import axios from "axios";

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(2),
  },
  title: {
    cursor: "pointer",
    flexGrow: 1,
  },
  linkTypography: {
    flexGrow: 1,
  },
  link: {
    cursor: "pointer",
    color: "white",
    textDecoration: "none",
  },
  mobileMenuLink: {
    cursor: "pointer",
    color: "black",
    textDecoration: "none",
  },
}));

export default function NavBar({ isActive, userEmail, setIsActive }) {
  const classes = useStyles();
  const [anchorEl, setAnchorEl] = useState(null);
  const open = Boolean(anchorEl);
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("xs"));

  const menuItems = [
    {
      menuTitle: "Recharge",
      to: "/recharge",
    },
    {
      menuTitle: "Transfer Money",
      to: "/transfer",
    },
    {
      menuTitle: "View Transactions",
      to: "/viewTransactions",
    },
    {
      menuTitle: "Cashbacks",
      to: "/cashbacks",
    },
  ];

  const handleMenu = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClick = () => {
    setAnchorEl(null);
  };

  const handleLogOutButtonOnClick = () => {
    axios
      .get(`http://localhost:8080/api/user/logout/${userEmail}`)
      .then((res) => {
        if (res.data === "LOGOUT_SUCCESS") {
          successToast("Logout Success");
          setIsActive(false);
        } else {
          errorToast("User can't be logged out!");
        }
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <div className={classes.root}>
      <AppBar position="static">
        <Toolbar>
          {isMobile ? (
            <>
              {isActive ? (
                <IconButton
                  edge="start"
                  className={classes.menuButton}
                  color="inherit"
                  aria-label="menu"
                  onClick={handleMenu}
                >
                  <MenuIcon />
                </IconButton>
              ) : null}
              <Typography variant="h6" className={classes.title}>
                <NavLink className={classes.link} to="/home">
                  Wallet
                </NavLink>
              </Typography>
              <Menu
                id="menu-appbar"
                className={classes.link}
                anchorEl={anchorEl}
                anchorOrigin={{
                  vertical: "top",
                  horizontal: "right",
                }}
                keepMounted
                transformOrigin={{
                  vertical: "top",
                  horizontal: "right",
                }}
                open={open}
                onClose={() => setAnchorEl(null)}
              >
                {menuItems.map((menuItem, index) => {
                  return (
                    <NavLink
                      key={index}
                      className={classes.mobileMenuLink}
                      to={menuItem.to}
                      onClick={handleMenuClick}
                    >
                      <MenuItem>{menuItem.menuTitle}</MenuItem>
                    </NavLink>
                  );
                })}
              </Menu>
              <Button color="inherit" onClick={handleLogOutButtonOnClick}>
                Log Out
              </Button>
            </>
          ) : (
            <>
              <Typography variant="h6" className={classes.title}>
                <NavLink className={classes.link} to="/home">
                  Wallet
                </NavLink>
              </Typography>
              {isActive ? (
                <>
                  {menuItems.map((menuItem, index) => {
                    return (
                      <Typography
                        key={index}
                        className={classes.linkTypography}
                      >
                        <NavLink className={classes.link} to={menuItem.to}>
                          {menuItem.menuTitle}
                        </NavLink>
                      </Typography>
                    );
                  })}
                  <Button color="inherit" onClick={handleLogOutButtonOnClick}>
                    Log Out
                  </Button>
                </>
              ) : null}
            </>
          )}
        </Toolbar>
      </AppBar>
    </div>
  );
}
