import * as dotenv from 'dotenv';
import { createPool } from 'mysql2';

dotenv.config();

export const db = createPool({
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
        rejectUnauthorized: Boolean(process.env.SSL), // TODO: change to true in production
    },
});
