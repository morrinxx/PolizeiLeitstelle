import { Injectable } from "@angular/core";
import { Status } from "./status";

@Injectable({
  providedIn: "root",
})
export class DataService {
  public Reports: Array<Status> = [];
  public currentOperation: String = "";
}
