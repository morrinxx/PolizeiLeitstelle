import { Component, OnInit } from "@angular/core";
import { Report } from "../report";

@Component({
  selector: "app-newest-tab",
  templateUrl: "./newest-tab.component.html",
  styleUrls: ["./newest-tab.component.css"]
})
export class NewestTabComponent implements OnInit {
  reports: Array<Report> = [
    new Report(1, "Einsatzbereit", "BR", "Braunau1", "Einsatz"),
    new Report(2, "Eingetroffen", "L", "Linz1", "Einsatz"),
    new Report(3, "Annehmen", "LL", "LinzLand1", "Einsatz")
  ];

  constructor() {}

  ngOnInit() {}
}
