// connection.js
const mongoose = require("mongoose");
const Police = require("./Polizei.model");
const connection = "mongodb://mongo:27017/mongo-police";
const connectDb = () => {
 return mongoose.connect(connection);
};
module.exports = connectDb;