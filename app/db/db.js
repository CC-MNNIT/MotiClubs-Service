const mysql = require("mysql2/promise");
require("dotenv").config();

let connection = null;

/* 
    Create connection object, if null
    Return the connection object 
*/
const getConnection = async () => {
    try {
        if (!connection) {
            connection = await mysql.createConnection(process.env.DATABASE_URL);
            console.log("Connected to the database");
        }
        return connection;
    } catch (error) {
        console.log(`Database connection failed. ${error}`);
    }
};

module.exports = getConnection;
