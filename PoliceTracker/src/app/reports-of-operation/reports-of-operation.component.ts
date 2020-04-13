import { Component, OnInit } from "@angular/core";
import { DataService } from "../data-service.service";
import { Status } from "../status";
import { LogServiceService } from "../log-tab/log-service.service";

@Component({
  selector: "app-reports-of-operation",
  templateUrl: "./reports-of-operation.component.html",
  styleUrls: ["./reports-of-operation.component.css"],
})
export class ReportsOfOperationComponent implements OnInit {
  reports: Array<Status> = [];

  constructor(
    public dataService: DataService,
    public logService: LogServiceService
  ) {}

  ngOnInit() {
    this.logService
      .getReportsOfOperation(this.dataService.currentOperation)
      .subscribe((data: []) => {
        console.log(data);
        //this.reports = data;
      });
  }
}
