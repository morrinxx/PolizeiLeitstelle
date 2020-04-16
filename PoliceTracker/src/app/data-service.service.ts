import { Injectable } from "@angular/core";
import { Status } from "./status";
import { Car } from "./car";

@Injectable({
  providedIn: "root",
})
export class DataService {
  public Reports: Array<Status> = [];
  public currentOperation: String = "";
  cars: Array<Car> = [
    new Car("Eferding1", "EF", "y"),
    new Car("Wels1", "WE", "y"),
    new Car("Leonding1", "LL", "n"),
    new Car("Braunau1", "BR", "y"),
    new Car("Freistadt1", "FR", "y"),
    new Car("Gmunden1", "GM", "y"),
    new Car("Grieskirchen1", "GR", "n"),
  ];
}
