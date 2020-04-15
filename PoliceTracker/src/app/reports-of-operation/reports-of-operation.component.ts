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
        var reportsTest: Array<Status> = [];
        data.forEach(element => {
          console.log(Object.values(element)[1]);
          var type = "";
          switch ("" + Object.values(element)[1]) {
            case "1":
              type = "Einsatzbereit";
              break;
            case "2":
              type = "Bedingt Einsatzbereit";
              break;
            case "3":
              type = "Annehmen/Übernehmen";
              break;
            case "4":
              type = "Eingetroffen";
              break;
            case "5":
              type = "Sprechwunsch";
              break;
            case "6":
              type = "Bereithaltezeit";
              break;
            case "7":
              type = "Abmelden";
              break;
            case "8":
              type = "Sprechwunsch in anderen Bundesland";
              break;
          }
          reportsTest.push(new Status( 
          type,
          "" + Object.values(element)[2], 
          "" + Object.values(element)[5],
          "" + Object.values(element)[4], 
          "" + Object.values(element)[3]));
        });
      console.log(reportsTest)
      this.reports = reportsTest;
      });
  }
}
