import { Component, OnInit } from "@angular/core";
import { Status } from "../status";
import { LogServiceService } from "./log-service.service";
import { DataService } from "../data-service.service";
import { Router } from "@angular/router";
import { Operation } from "../operation";

@Component({
  selector: "app-log-tab",
  templateUrl: "./log-tab.component.html",
  styleUrls: ["./log-tab.component.css"],
})
export class LogTabComponent implements OnInit {
  reports: Array<Operation> = [
    new Operation(1, "Banküberfall", "Einsatz", "BR", "Braunau1", "Einsatz"),
    new Operation(2, "Stecherei", "Einsatz", "L", "Linz1", "Einsatz"),
    new Operation(3, "Autounfall", "Einsatz", "LL", "LinzLand1", "Einsatz"),
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
    this.dataService.currentOperation = operation;
    this.router.navigate(["/reportsOfOperation"]);
  }
}
