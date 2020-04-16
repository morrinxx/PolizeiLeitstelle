const mongoose = require("mongoose");

const meldungSchema = new mongoose.Schema({
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
const Meldung = mongoose.model("Meldung", meldungSchema);
module.exports = Meldung;