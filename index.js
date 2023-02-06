const app = require("./app");
const getConnection = require("./app/db/db");
require("dotenv").config();

const port = process.env.PORT || "3000";

getConnection()
    .then(() => {
        app.listen(port, () => {
            console.log(`Server listening on port ${port}`);
        });
    })
    .catch((err) => {
        console.error(`Server could not start. ${err}`);
    });
