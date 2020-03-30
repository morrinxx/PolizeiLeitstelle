import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { NewestTabComponent } from "./newest-tab/newest-tab.component";
import { LogTabComponent } from "./log-tab/log-tab.component";
import { NewOperationTabComponent } from "./new-operation-tab/new-operation-tab.component";

const routes: Routes = [
  { path: "newTab", component: NewestTabComponent },
  { path: "logTab", component: LogTabComponent },
  { path: "newOperationTab", component: NewOperationTabComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
