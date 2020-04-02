const mongoose = require("mongoose");

const policeSchema = new mongoose.Schema({
 policeName: {
 type: String
 }
});
const Police = mongoose.model("Police", policeSchema);
module.exports = Police;