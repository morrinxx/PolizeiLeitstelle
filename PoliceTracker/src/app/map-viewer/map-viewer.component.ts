import { Component, OnInit, Injectable } from "@angular/core";
import { DataService } from "../data-service.service";
import { LogServiceService } from "../log-tab/log-service.service";
import { Car } from "../car";

//import * as ol from "../../../node_modules/ol";

declare var ol: any;

@Injectable({
  providedIn: "root",
})
@Component({
  selector: "app-map-viewer",
  templateUrl: "./map-viewer.component.html",
  styleUrls: ["./map-viewer.component.css"],
})
export class MapViewerComponent implements OnInit {
  constructor(
    public dataservice: DataService,
    public logService: LogServiceService
  ) {}

  cars: Array<Car> = [];
  carInputTime;
  carInputOperation;

  latitude: number = 48.30639;
  longitude: number = 14.28611;

  map: any;

  ngOnInit() {
    this.cars = this.dataservice.cars;

    this.map = new ol.Map({
      target: "map",
      layers: [
        new ol.layer.Tile({
          source: new ol.source.OSM(),
        }),
        this.dataservice.vectorLayer,
      ],
      view: new ol.View({
        center: ol.proj.fromLonLat([this.longitude, this.latitude]),
        zoom: 10,
      }),
    });
    this.addCarMarkers();
  }

  checkToFalse() {
    this.dataservice.checkIfRoute = false;
  }

  addCarMarkers() {
    console.log(this.dataservice.checkIfRoute);
    if (this.dataservice.checkIfRoute == false) {
      this.dataservice.vectorLayer.getSource().clear();
      this.dataservice.carGpss.forEach((carGps) => {
        this.addPoint(carGps.coorY, carGps.coorX, carGps.carName);
      });
    } else {
      if (this.dataservice.type == "ti") {
        this.getPositionsCarTime(this.dataservice.currentCarName);
      } else {
        this.getPositionsCarOperation(this.dataservice.currentCarName);
      }
    }
  }

  getPositionsCarOperation(carName) {
    this.dataservice.type = "op";
    this.dataservice.currentCarName = carName;
    this.dataservice.checkIfRoute = true;

    var arrayPositions;
    console.log(carName);
    /*this.logService
      .getPositionsCarOperation(carName)
      .subscribe((data) => (arrayPositions = data));*/
    this.dataservice.vectorLayer.getSource().clear();
    arrayPositions = [
      [48.3095, 14.0231],
      [48.3085, 14.0241],
      [48.3075, 14.0221],
      [48.3065, 14.0251],
    ];
    /*arrayPositions.forEach((pos) => {
      this.addPoint(pos[1], pos[0], carName);
    });*/
    for (let i = 0; i < arrayPositions.length; i++) {
      if (i + 1 <= arrayPositions.length)
        this.addRoute(
          arrayPositions[i][1],
          arrayPositions[i][0],
          arrayPositions[i + 1][1],
          arrayPositions[i + 1][0]
        );
    }
  }

  getPositionsCarTime(carName) {
    this.dataservice.type = "ti";
    this.dataservice.currentCarName = carName;
    this.dataservice.checkIfRoute = true;
    var arrayPositions;
    /*this.logService
      .getPositionsCarTime(carName)
      .subscribe((data) => (arrayPositions = data));*/
    this.dataservice.vectorLayer.getSource().clear();
    arrayPositions = [
      [48.3095, 14.0231],
      [48.3085, 14.0241],
      [48.3075, 14.0221],
      [48.3065, 14.0251],
    ];
    /*arrayPositions.forEach((pos) => {
      this.addPoint(pos[1], pos[0], "wesl");
    });*/
    for (let i = 0; i < arrayPositions.length; i++) {
      if (i + 1 <= arrayPositions.length)
        this.addRoute(
          arrayPositions[i][1],
          arrayPositions[i][0],
          arrayPositions[i + 1][1],
          arrayPositions[i + 1][0]
        );
    }
  }

  addRoute(lonA, latA, lonB, latB) {
    var vectorLayer = new ol.layer.Vector({
      name: "trailLayer",
      type: "Vector",
      source: new ol.source.Vector({
        format: new ol.format.GeoJSON({ featureProjection: "EPSG:3857" }),
      }),
      zoomMin: 8,
      zoomMax: 18,
    });
    let coords = [
      [lonA, latA],
      [lonB, latB],
    ];
    let lineString = new ol.geom.LineString(coords);
    lineString.transform("EPSG:4326", "EPSG:3857");
    var lineFeature = new ol.Feature({
      name: "test",
    });
    lineFeature.setGeometry(lineString);
    var lineStyle = new ol.style.Style({
      stroke: new ol.style.Stroke({
        width: 10,
      }),
    });
    lineFeature.setStyle(lineStyle);
    this.dataservice.markerSource.addFeature(lineFeature);
  }

  addPoint(lon, lat, name) {
    var iconFeatures = [];
    var iconFeature = new ol.Feature({
      geometry: new ol.geom.Point(
        ol.proj.transform([lon, lat], "EPSG:4326", "EPSG:3857")
      ),
      name: name,
    });
    this.dataservice.markerSource.addFeature(iconFeature);
  }
}
