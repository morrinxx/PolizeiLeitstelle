import { Injectable } from "@angular/core";
import { Report } from "./report";

@Injectable({
  providedIn: "root"
})
export class DataService {
  public Reports: Array<Report> = [];
}
