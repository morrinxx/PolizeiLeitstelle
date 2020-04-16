// connection.js
const mongoose = require("mongoose");
const Meldung = require("./Meldung.model");
const connection = "mongodb://mongo:27017/Leitstelle";
const connectDb = () => {
 return mongoose.connect(connection);
};
module.exports = connectDb;