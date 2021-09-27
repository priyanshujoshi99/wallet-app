import React, { useEffect, useState } from "react";
import CssBaseline from "@material-ui/core/CssBaseline";
import ViewTransactionContent from "./ViewTransactionContent";
import TableComponent from "../commons/TableComponent";
import axios from "axios";

export default function ViewTransactions({ userEmail, userBalance }) {
  const [rows, setRows] = useState([]);
  useEffect(() => {
    document.title = "Wallet | View Transactions";
  }, []);

  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/transaction/viewTransaction/${userEmail}`)
      .then((res) => {
        setRows(res.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }, [userEmail]);

  const columns = [
    { id: "transactionType", label: "Transaction Type", minWidth: 170 },
    { id: "transactionNote", label: "Transaction Note", minWidth: 170 },
    {
      id: "transactionAmount",
      label: "Transaction Amount",
      minWidth: 170,
      align: "right",
      format: (value) => "₹" + value.toFixed(2).toLocaleString("en-US"),
    },
    {
      id: "transactionDateTime",
      label: "Transaction Date & Time",
      minWidth: 170,
      align: "right",
    },
    {
      id: "balanceAfterTransaction",
      label: "Balance",
      minWidth: 170,
      align: "right",
      format: (value) => "₹" + value.toFixed(2).toLocaleString("en-US"),
    },
  ];

  return (
    <React.Fragment>
      <CssBaseline />
      <ViewTransactionContent userBalance={userBalance} />
      <TableComponent rows={rows} columns={columns} />
    </React.Fragment>
  );
}
