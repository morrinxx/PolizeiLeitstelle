import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { NewestTabComponent } from "./newest-tab/newest-tab.component";
import { LogTabComponent } from "./log-tab/log-tab.component";
import { NewOperationTabComponent } from "./new-operation-tab/new-operation-tab.component";
import { ReportsOfOperationComponent } from "./reports-of-operation/reports-of-operation.component";
import { MapViewerComponent } from "./map-viewer/map-viewer.component";

const routes: Routes = [
  { path: "", component: MapViewerComponent, pathMatch: "full" },
  { path: "newTab", component: NewestTabComponent },
  { path: "logTab", component: LogTabComponent },
  { path: "newOperationTab", component: NewOperationTabComponent },
  { path: "reportsOfOperation", component: ReportsOfOperationComponent },
  { path: "map", component: MapViewerComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
