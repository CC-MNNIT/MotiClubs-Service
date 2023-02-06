const mysql = require("mysql2/promise");
require("dotenv").config();

// Connection pool
const pool = mysql.createPool({
    host: process.env.HOST,
    user: process.env.DB_USER,
    database: process.env.DATABASE,
    password: process.env.PASSWORD,
    waitForConnections: true,
    connectionLimit: 10,
    maxIdle: 10,
    idleTimeout: 60000,
    queueLimit: 0,
    ssl: {
        rejectUnauthorized: process.env.SSL, // TODO: change to true in production
    },
});

module.exports = pool;
