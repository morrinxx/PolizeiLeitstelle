import { Component, OnInit } from "@angular/core";
import { Status } from "../status";
import { DataService } from "../data-service.service";

@Component({
  selector: "app-newest-tab",
  templateUrl: "./newest-tab.component.html",
  styleUrls: ["./newest-tab.component.css"]
})
export class NewestTabComponent implements OnInit {
  reports: Array<Status> = [];

  constructor(public dataService: DataService) {}

  ngOnInit() {
    this.reports = this.dataService.Reports;
  }
}
