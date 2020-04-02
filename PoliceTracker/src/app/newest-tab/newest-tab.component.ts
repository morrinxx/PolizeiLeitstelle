import { Component, OnInit } from "@angular/core";
import { Report } from "../report";
import { DataService } from "../data-service.service";

@Component({
  selector: "app-newest-tab",
  templateUrl: "./newest-tab.component.html",
  styleUrls: ["./newest-tab.component.css"]
})
export class NewestTabComponent implements OnInit {
  reports: Array<Report> = [
    new Report(1, "Einsatzbereit", "BR", "Braunau1", "Status"),
    new Report(2, "Eingetroffen", "L", "Linz1", "Status"),
    new Report(3, "Annehmen", "LL", "LinzLand1", "Status")
  ];

  constructor(public dataService: DataService) {}

  ngOnInit() {
    this.reports = this.dataService.Reports;
  }
}
