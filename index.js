const express = require("express");
const mongoose = require("mongoose");
const userRoute = require("./routes/UserRoute");
const clubRoute = require("./routes/ClubRoute");
require("dotenv").config();

const app = express();

app.use(express.json());
app.use(userRoute);
app.use(clubRoute);

mongoose
  .connect(process.env.URL)
  .then(() => {
    console.log("Connected to the database");
    app.listen(process.env.PORT, () => {
      console.log(`Server listening on port ${process.env.PORT}`);
    });
  })
  .catch((err) => {
    console.error(`Error connecting to the database. n${err}`);
  });
