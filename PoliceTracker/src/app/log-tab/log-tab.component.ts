import { Component, OnInit } from "@angular/core";
import { Report } from "../report";

@Component({
  selector: "app-log-tab",
  templateUrl: "./log-tab.component.html",
  styleUrls: ["./log-tab.component.css"]
})
export class LogTabComponent implements OnInit {
  reports: Array<Report> = [
    new Report(1, "Banküberfall", "BR", "Braunau1", "Einsatz"),
    new Report(2, "Stecherei", "L", "Linz1", "Einsatz"),
    new Report(3, "Autounfall", "LL", "LinzLand1", "Einsatz")
  ];

  constructor() {}

  ngOnInit() {}
}
