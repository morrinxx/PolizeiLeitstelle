import { Component } from "@angular/core";
import { Observable, Observer } from "rxjs";
import { RouterLink, Router } from "@angular/router";
import { Report } from "./report";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"]
})
export class AppComponent {
  navLinks: any[];
  activeLinkIndex = -1;
  constructor(private router: Router) {
    this.navLinks = [
      {
        label: "Newest",
        link: "./newTab",
        index: 0
      },
      {
        label: "Log",
        link: "./logTab",
        index: 1
      },
      {
        label: "New operation",
        link: "./newOperationTab",
        index: 2
      }
    ];
  }
}
