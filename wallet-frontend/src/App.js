import React, { useState } from "react";
import { Route, Switch, Redirect } from "react-router-dom";
import Home from "./components/Home/Home";
import SignUp from "./components/SignUp/SignUp";
import LogIn from "./components/LogIn/LogIn";
import Recharge from "./components/Recharge/Recharge";
import Transfer from "./components/Transfer/Transfer";
import ViewTransactions from "./components/ViewTransactions/ViewTransactions";
import Footer from "./components/commons/Footer";
import axios from "axios";
import NavBar from "./components/commons/NavBar";
import { ToastContainer } from "react-toastify";
import Cashbacks from "./components/Cashbacks/Cashbacks";

function App() {
  const [userFirstName, setUserFirstName] = useState("");
  const [userEmail, setUserEmail] = useState("");
  const [userBalance, setUserBalance] = useState(0);
  const [isActive, setIsActive] = useState(false);

  const getUserDetails = (userEmail) => {
    axios
      .get(`http://localhost:8080/api/user/allUsers/${userEmail}`)
      .then((res) => {
        setUserFirstName(res.data.userFirstName);
        setUserBalance(res.data.balance);
        setIsActive(res.data.active);
        setUserEmail(res.data.userEmail);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <div className="App">
      <NavBar
        userEmail={userEmail}
        isActive={isActive}
        setIsActive={setIsActive}
      />
      <Switch>
        <Route
          exact
          path="/"
          component={
            !isActive
              ? () => <LogIn getUserDetails={getUserDetails} />
              : () => (
                  <Redirect
                    to="/home"
                    component={() => (
                      <Home
                        userFirstName={userFirstName}
                        userBalance={userBalance}
                      />
                    )}
                  ></Redirect>
                )
          }
        />
        <Route exact path="/signUp" component={() => <SignUp />} />
        <Route
          exact
          path="/home"
          component={
            isActive
              ? () => (
                  <Home
                    userFirstName={userFirstName}
                    userBalance={userBalance}
                  />
                )
              : () => (
                  <Redirect
                    to="/"
                    component={() => <LogIn getUserDetails={getUserDetails} />}
                  ></Redirect>
                )
          }
        />
        <Route
          exact
          path="/recharge"
          component={
            isActive
              ? () => (
                  <Recharge
                    userEmail={userEmail}
                    getUserDetails={getUserDetails}
                  />
                )
              : () => (
                  <Redirect
                    to="/"
                    component={() => <LogIn getUserDetails={getUserDetails} />}
                  ></Redirect>
                )
          }
        />
        <Route
          exact
          path="/transfer"
          component={
            isActive
              ? () => (
                  <Transfer
                    userEmail={userEmail}
                    getUserDetails={getUserDetails}
                    userBalance={userBalance}
                  />
                )
              : () => (
                  <Redirect
                    to="/"
                    component={() => <LogIn getUserDetails={getUserDetails} />}
                  ></Redirect>
                )
          }
        />
        <Route
          exact
          path="/viewTransactions"
          component={
            isActive
              ? () => (
                  <ViewTransactions
                    userEmail={userEmail}
                    userBalance={userBalance}
                  />
                )
              : () => (
                  <Redirect
                    to="/"
                    component={() => <LogIn getUserDetails={getUserDetails} />}
                  ></Redirect>
                )
          }
        />
        <Route
          exact
          path="/cashbacks"
          component={
            isActive
              ? () => <Cashbacks userEmail={userEmail} />
              : () => (
                  <Redirect
                    to="/"
                    component={() => <LogIn getUserDetails={getUserDetails} />}
                  ></Redirect>
                )
          }
        />
      </Switch>
      <Footer />
      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />
    </div>
  );
}

export default App;
