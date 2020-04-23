import { Component, OnInit, OnDestroy } from "@angular/core";
import { Observable, Observer, Subscription } from "rxjs";
import { RouterLink, Router } from "@angular/router";
import { IMqttMessage, MqttService } from "ngx-mqtt";
import { Status } from "./status";
import { DataService } from "./data-service.service";
<<<<<<< Updated upstream
import { NewOperationTabComponent } from './new-operation-tab/new-operation-tab.component';
=======
import { NewOperationTabComponent } from "./new-operation-tab/new-operation-tab.component";
>>>>>>> Stashed changes

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"],
})
export class AppComponent implements OnInit, OnDestroy {
  private subscription: Subscription;
  navLinks: any[];
  msg;
  activeLinkIndex = -1;
  constructor(
    private router: Router,
    private _mqttService: MqttService,
    public dataservice: DataService,
    public newOp: NewOperationTabComponent
  ) {
    this.navLinks = [
      {
        label: "Neue",
        link: "./newTab",
        index: 0,
      },
      {
        label: "Log",
        link: "./logTab",
        index: 1,
      },
      {
        label: "Neuer Einsatz",
        link: "./newOperationTab",
        index: 2,
      },
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
      .observe("Leitstelle/+/+/Status")
      .subscribe((message: IMqttMessage) => {
        console.log(message.payload);
        var splitString = message.topic.toString();
        var splitArray = splitString.split("/");
        var type;
        switch (message.payload.slice(7, 8).toString()) {
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

        this.msg = message.payload.slice(25).toString().split('"}')[0];
        var newReport = new Status(
          type,
          this.msg,
          splitArray[1],
          splitArray[2],
          splitArray[3]
        );
        this.dataservice.cars.forEach((car) => {
          console.log("carname: " + car.name + "newreport: " + newReport.carId)
          if (car.name == newReport.carId) {
            console.log("carname: " + car.name)
            if (
              newReport.id == "Einsatzbereit" ||
              newReport.id == "Bedingt Einsatzbereit"
            ) {
              car.avaible = "y";
            } else if (newReport.id == "Annehmen/Übernehmen"|| newReport.id == "Abmelden") {
              car.avaible = "n";
            }
          }
        });
<<<<<<< Updated upstream
        this.router.navigateByUrl('/newOperationTab', { skipLocationChange: true }).then(() => {
          this.router.navigate(['/newTab']);
      });

=======
        this.newOp.refresh();
>>>>>>> Stashed changes
        this.dataservice.Reports.push(newReport);
        console.log("Report: ", newReport);
      });
  }
}
