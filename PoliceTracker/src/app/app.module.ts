import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";

import { MaterialModule } from "./material.module";

import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { NewestTabComponent } from "./newest-tab/newest-tab.component";
import { LogTabComponent } from "./log-tab/log-tab.component";
import { NewOperationTabComponent } from "./new-operation-tab/new-operation-tab.component";
import { Report } from "./report";

@NgModule({
  declarations: [
    AppComponent,
    NewestTabComponent,
    LogTabComponent,
    NewOperationTabComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MaterialModule,
    NoopAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
