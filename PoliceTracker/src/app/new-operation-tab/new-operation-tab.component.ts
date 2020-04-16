import { Component, OnInit } from "@angular/core";
import { MqttService } from "ngx-mqtt";
import { LogServiceService } from "../log-tab/log-service.service";
import { Car } from "../car";
import { DataService } from "../data-service.service";

@Component({
  selector: "app-new-operation-tab",
  templateUrl: "./new-operation-tab.component.html",
  styleUrls: ["./new-operation-tab.component.css"],
})
export class NewOperationTabComponent implements OnInit {
  cars: Array<Car> = [];
  districts: Array<String> = [
    "BR",
    "EF",
    "FR",
    "GM",
    "GR",
    "KI",
    "L",
    "LL",
    "PE",
    "RI",
    "RO",
    "SD",
    "SR",
    "SE",
    "UU",
    "VB",
    "WE",
    "WL",
  ];

  districtInput;
  nameInput;
  carInput;
  constructor(
    private _mqttService: MqttService,
    public logService: LogServiceService,
    public dataService: DataService
  ) {}

  ngOnInit() {
    this.cars = this.dataService.cars;
  }

  id = "";

  async delay(ms: number) {
    await new Promise((resolve) => setTimeout(() => resolve(), ms)).then(() => {
      var topic =
        "Leitstelle/" + this.districtInput + "/" + this.carInput + "/Einsatz";
      console.log(topic);
      var msg =
        '{"id":"' + this.id + '","description":"' + this.nameInput + '"}';
      console.log(msg);
      this._mqttService.unsafePublish(topic, msg, {
        qos: 1,
        retain: true,
      });
      this.dataService.cars.forEach((car) => {
        if (car.name == this.carInput) {
          car.avaible = "n";
        }
      });
    });
  }

  refresh() : void{
    window.location.reload();
  }

  createClicked() {
    console.log(this.districtInput);
    console.log(this.carInput);
    console.log(this.nameInput);

    this.logService.getIdForOperation().subscribe(async (data: string) => {
      (this.id = data), console.log("Data: " + data);
    });
    console.log("id: " + this.id);

    this.delay(500);
  }
}
