const app = require("./app");
const mysql = require("mysql2/promise")
require("dotenv").config();

const port = process.env.PORT || "3000";

mysql
    .createConnection(process.env.DATABASE_URL)
    .then(() => {
        console.log("Connected to the database");
        app.listen(port, () => {
            console.log(`Server listening on port ${port}`);
        });
    })
    .catch((err) => {
        console.error(`Error connecting to the database. ${err}`);
    });
