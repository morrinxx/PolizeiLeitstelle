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
  reports: Array<Operation> = [new Operation(0, 1,"test","Einsatz","5","LL","18213812381", 0)];

  constructor(
    public logService: LogServiceService,
    public dataService: DataService,
    private router: Router
  ) {}

  ngOnInit() {
    this.logService.getOperations().subscribe((data: []) => {
      console.log(data);
      var reportsTest: Array<Operation> = [];
      data.forEach(element => {
        console.log(Object.values(element)[1]),
        reportsTest.push(new Operation(parseInt("" + Object.values(element)[0]), 
        parseInt("" + Object.values(element)[1]),
        "" + Object.values(element)[2], 
        "" + Object.values(element)[3],
        "" + Object.values(element)[4], 
        "" + Object.values(element)[5],
        "" + Object.values(element)[6], 
        parseInt("" + Object.values(element)[7])))
      });
      console.log(reportsTest)
      this.reports = reportsTest;
    });
  }

  clickedReportsButton(operation) {
    this.dataService.currentOperation = operation;
    this.router.navigate(["/reportsOfOperation"]);
  }
}
