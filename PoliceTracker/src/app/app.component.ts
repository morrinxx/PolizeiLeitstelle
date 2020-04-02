import { Component, OnInit, OnDestroy } from "@angular/core";
import { Observable, Observer, Subscription } from "rxjs";
import { RouterLink, Router } from "@angular/router";
import { IMqttMessage, MqttService } from "ngx-mqtt";
import { Report } from "./report";
import { DataService } from "./data-service.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"]
})
export class AppComponent implements OnInit, OnDestroy {
  private subscription: Subscription;
  navLinks: any[];
  msg;
  activeLinkIndex = -1;
  constructor(
    private router: Router,
    private _mqttService: MqttService,
    public dataservice: DataService
  ) {
    this.navLinks = [
      {
        label: "Neue",
        link: "./newTab",
        index: 0
      },
      {
        label: "Log",
        link: "./logTab",
        index: 1
      },
      {
        label: "Neuer Einsatz",
        link: "./newOperationTab",
        index: 2
      }
    ];
  }
  ngOnInit(): void {
    this.subscribeToTopic();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  subscribeToTopic(): void {
    console.log("inside subscribe new topic");
    this.subscription = this._mqttService
      .observe("Leitstelle/#")
      .subscribe((message: IMqttMessage) => {
        var splitString = message.topic.toString();
        var splitArray = splitString.split("/");
        this.msg = message.payload.toString();
        var newReport = new Report(
          999,
          this.msg,
          splitArray[1],
          splitArray[2],
          splitArray[3]
        );
        if (newReport.type == "Status") {
          this.dataservice.Reports.push(newReport);
        }
        console.log("Report: ", newReport);
      });
  }
}
