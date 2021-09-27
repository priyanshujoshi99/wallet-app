import React, { useEffect, useState } from "react";
import CssBaseline from "@material-ui/core/CssBaseline";
import CashbackContent from "./CashbackContent";
import TableComponent from "../commons/TableComponent";
import axios from "axios";

export default function ViewTransactions({ userEmail }) {
  const [rows, setRows] = useState([]);
  const [rowsForCashback, setRowsForCashback] = useState([]);

  useEffect(() => {
    document.title = "Wallet | Cashbacks";
  }, []);

  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/transaction/viewTransaction/${userEmail}`)
      .then((res) => {
        setRows(res.data);
        setRowsForCashback(
          rows.filter((row) => row.transactionType === "CREDIT_BY_CASHBACK")
        );
      })
      .catch((error) => {
        console.log(error);
      });
  }, [userEmail, rows]);

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
      <CashbackContent />
      <TableComponent rows={rowsForCashback} columns={columns} />
    </React.Fragment>
  );
}
