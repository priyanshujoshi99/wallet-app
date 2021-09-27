import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Card from "@material-ui/core/Card";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import Typography from "@material-ui/core/Typography";
import { NavLink } from "react-router-dom";

const useStyles = makeStyles({
  root: {
    position: "relative",
    margin: "0 10% 5% 10%",
    maxWidth: 345,
  },
  media: {
    height: 140,
  },
  cardButton: {
    color: "#3F51B5",
    margin: "0 auto",
    position: "relative",
    textDecoration: "none",
    fontWeight: "bold",
  },
});

export default function HomeCard({ cardTitle, cardLinkTitle, to }) {
  const classes = useStyles();

  return (
    <Card className={classes.root}>
      <CardContent>
        <Typography gutterBottom variant="h5" component="h2">
          {cardTitle}
        </Typography>
      </CardContent>
      <CardActions>
        <NavLink className={classes.cardButton} to={to}>
          {cardLinkTitle}
        </NavLink>
      </CardActions>
    </Card>
  );
}
