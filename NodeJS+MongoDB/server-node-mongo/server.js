//Schema-Modell
const Meldung = require("./src/Meldung.model") //Einsatz
const Status = require("./src/Status.meldung") //Status

//express
const express = require("express");
const app = express();
const PORT = 8080;

//mongo
const connectDb = require("./src/connection");

//MQTT
const mqtt = require('mqtt')
const mqttClient = mqtt.connect('mqtt://broker.hivemq.com')

app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});

mqttClient.on('connect', () => {
    console.log("Mqtt-Testserver")
    mqttClient.subscribe('Leitstelle/#');
})

mqttClient.on('message', async (topic, message) => {
    if(message.toString()[0] == "{"){
        const topicArray = topic.toString().split('/');
        console.log(topic.toString() + ": " + message.toString());

        const typeName = topicArray[3].toString();
        const payload = message.toString().split('"');

        console.log("test-payload:" + payload[3] + ".")

        if(typeName == "Einsatz")
        {
            const meldung = new Meldung({id: parseInt(payload[3]), description: payload[7], type: typeName,
                autoid: topicArray[2], bezirk: topicArray[1], timestamp: new Date().getTime()});

            await meldung.save().then(() => console.log("DB-insert mit Einsatz"))
        }

        else{
            const status = new Status({id: parseInt(payload[3]), description: payload[7], type: typeName,
                autoid: topicArray[2], bezirk: topicArray[1], timestamp: new Date().getTime()});

            await status.save().then(() => console.log("DB-insert mit Status"))
        }
    }
})

app.listen(PORT, function() {
 console.log(`Hört dem Port: ${PORT} zu `);
connectDb().then(() => {
 console.log("MongoDb verbunden");
 });
});


app.get("/meldung", async (req, res) => {
    res.header("Access-Control-Allow-Origin", "*");
    const meldungs = await Meldung.find();
    res.json(meldungs);
});

app.get("/statuse/:einsatzid", async (req, res) => {
    res.header("Access-Control-Allow-Origin", "*");
    const einsatzid = req.params.einsatzid;
    const meldung = await Meldung.findOne({ id: einsatzid })

    const status = await Status.findOne({ timestamp: {$gt: meldung.timestamp}, bezirk: meldung.bezirk, autoid: meldung.autoid, id: ["1", "7"]});
    console.log("Timestamp: " + meldung.timestamp);
    if(status == null){ //diese Lösung, aufgrund von PromiseRejectionException
        const statusmeldungGt = await Status.find({ timestamp: { $gt: meldung.timestamp}, bezirk: meldung.bezirk, autoid: meldung.autoid});
        res.json(statusmeldungGt);
    }
    else{
        const statusmeldungLt = await Status.find({ timestamp: { $gt: meldung.timestamp, $lt: status.timestamp}, bezirk: meldung.bezirk, autoid: meldung.autoid});
        res.json(statusmeldungLt);
    }
});


app.get("/idealid", async (req,res) => {
    res.header("Access-Control-Allow-Origin", "*");
    const nextid = await (await Meldung.find()).length + 1; //Liste aller Einsätze + 1 = freie ID

    res.send("" + nextid);
});
