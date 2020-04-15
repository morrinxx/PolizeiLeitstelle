import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";

import { MaterialModule } from "./material.module";

import { AppRoutingModule } from "./app-routing.module";
import { FormsModule } from "@angular/forms";
import { AppComponent } from "./app.component";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { NewestTabComponent } from "./newest-tab/newest-tab.component";
import { LogTabComponent } from "./log-tab/log-tab.component";
import { NewOperationTabComponent } from "./new-operation-tab/new-operation-tab.component";
import { Status } from "./status";
import { HttpClientModule } from "@angular/common/http";
import { MqttModule, IMqttServiceOptions } from "ngx-mqtt";
import { ReportsOfOperationComponent } from "./reports-of-operation/reports-of-operation.component";
export const MQTT_SERVICE_OPTIONS: IMqttServiceOptions = {
  hostname: "broker.hivemq.com",
  port: 8000,
  path: "/mqtt",
};

@NgModule({
  declarations: [
    AppComponent,
    NewestTabComponent,
    LogTabComponent,
    NewOperationTabComponent,
    ReportsOfOperationComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MaterialModule,
    NoopAnimationsModule,
    MqttModule.forRoot(MQTT_SERVICE_OPTIONS),
    HttpClientModule,
    FormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
