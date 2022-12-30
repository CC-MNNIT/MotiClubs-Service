const express = require('express');
const mongoose = require('mongoose');
require('dotenv').config();

const app = express();

mongoose.connect(process.env.URL)
    .then( () => {
        console.log('Connected to the database');
        app.listen(process.env.PORT, () => {
        	console.log(`Server listening on port ${process.env.PORT}`)
        })
    })
    .catch( (err) => {
        console.error(`Error connecting to the database. n${err}`);
    })
