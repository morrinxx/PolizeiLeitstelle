import { Injectable } from "@angular/core";
import { Status } from "./status";
import { Car } from "./car";
import { CarGps } from "./carGps";

declare var ol: any;

@Injectable({
  providedIn: "root",
})
export class DataService {
  public Reports: Array<Status> = [];
  public currentOperation: String = "";
  cars: Array<Car> = [
    new Car("Eferding1", "EF", "y"),
    new Car("Wels1", "WE", "y"),
    new Car("Leonding1", "LL", "y"),
    new Car("Braunau1", "BR", "y"),
    new Car("Freistadt1", "FR", "y"),
    new Car("Gmunden1", "GM", "y"),
    new Car("Grieskirchen1", "GR", "y"),
  ];

  carGpss: Array<CarGps> = [
    new CarGps("Eferding1", "EF", 48.3095, 14.0231),
    new CarGps("Wels1", "WE", 48.1601, 14.024),
    new CarGps("Leonding1", "LL", 48.2731, 14.2536),
    new CarGps("Braunau1", "BR", 48.2553, 13.0455),
    new CarGps("Freistadt1", "FR", 48.508, 14.5023),
    new CarGps("Gmunden1", "GM", 47.9158, 13.7887),
    new CarGps("Grieskirchen1", "GR", 48.2337, 13.8268),
  ];

  markerSource = new ol.source.Vector();
  markerStyle = new ol.style.Style({
    image: new ol.style.Icon({
      anchor: [0.5, 0.5],
      anchorXUnits: "fraction",
      anchorYUnits: "fraction",
      src: "assets/img/car.png",
      imgSize: [400, 250],
    }),
  });

  vectorLayer = new ol.layer.Vector({
    source: this.markerSource,
    style: this.markerStyle,
  });

  type = "";
  currentCarName = "";
  checkIfRoute = false;
}
