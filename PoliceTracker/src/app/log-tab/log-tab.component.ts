import { Component, OnInit } from "@angular/core";
import { Status } from "../status";
import { LogServiceService } from "./log-service.service";
import { DataService } from "../data-service.service";
import { Router } from "@angular/router";

@Component({
  selector: "app-log-tab",
  templateUrl: "./log-tab.component.html",
  styleUrls: ["./log-tab.component.css"],
})
export class LogTabComponent implements OnInit {
  reports: Array<Status> = [
    new Status("1", "Banküberfall", "BR", "Braunau1", "Einsatz"),
    new Status("2", "Stecherei", "L", "Linz1", "Einsatz"),
    new Status("3", "Autounfall", "LL", "LinzLand1", "Einsatz"),
  ];

  constructor(
    public logService: LogServiceService,
    public dataService: DataService,
    private router: Router
  ) {}

  ngOnInit() {
    this.logService.getOperations().subscribe((data: []) => {
      console.log(data);
      //this.reports = data;
    });
  }

  clickedReportsButton(operation) {
    console.log("JHI");
    this.dataService.currentOperation = operation;
    this.router.navigate(["/reportsOfOperation"]);
  }
}
