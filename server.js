const app = require("./app");
const mongoose = require("mongoose");
require("dotenv").config();

const port = process.env.PORT || "3000";

mongoose
  .connect(process.env.URL)
  .then(() => {
    console.log("Connected to the database");
    app.listen(port, () => {
      console.log(`Server listening on port ${port}`);
    });
  })
  .catch((err) => {
    console.error(`Error connecting to the database. n${err}`);
  });
