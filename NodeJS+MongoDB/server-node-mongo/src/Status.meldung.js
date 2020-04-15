const mongoose = require("mongoose");

const statusSchema = new mongoose.Schema({
 id: { //Status oder Einsatzid
    type: Number
 },
 description:{  //Description
     type: String
 },
 type:{ //Status oder Einsatz
     type: String
 },
 autoid:{ //AUTOID
     type: String
 },
 bezirk:{ //Bezirk: WE,LL ...
     type: String
 },
 timestamp:{
     type: Number
 }
});
const Status = mongoose.model("Status", statusSchema);
module.exports = Status;