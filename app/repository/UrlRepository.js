const getConnection = require("../db/db");

let con = null;

getConnection().then((connection) => {
    con = connection;
});
