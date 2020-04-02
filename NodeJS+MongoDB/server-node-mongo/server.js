//Instanzen
const Police = require("./src/Polizei.model")

//server
const express = require("express");
const app = express();
const PORT = 8080;

//mongo
const connectDb = require("./src/connection");


app.listen(PORT, function() {
 console.log(`Hört dem Port ${PORT} zu `);
connectDb().then(() => {
 console.log("MongoDb verbunden");
 });
});
app.get("/polices", async (req, res) => {
 const polices = await Police.find();
 res.json(polices);
});
app.get("/police-create", async (req, res) => {
 const police = new Police({ policeName: "polizist" });
 await police.save().then(() => console.log("Police created"));
 res.send("Police-officer created \n");
});