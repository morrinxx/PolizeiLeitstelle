import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { retry, catchError } from "rxjs/operators";

@Injectable({
  providedIn: "root",
})
export class LogServiceService {
  constructor(private httpClient: HttpClient) {}
  baseUrl = "http://localhost:9000/Leitstelle/log";
  httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json",
    }),
  };

  getOperations(): Observable<any> {
    return this.httpClient
      .get(this.baseUrl + "/getOperations")
      .pipe(retry(1), catchError(this.handleError));
  }

  getReportsOfOperation(operation): Observable<any> {
    let params = new HttpParams().set("operationName", operation);
    return this.httpClient
      .get(this.baseUrl + "/getReportsOfOperations", { params: params })
      .pipe(retry(1), catchError(this.handleError));
  }

  handleError(error) {
    let errorMessage = "";
    if (error.error instanceof ErrorEvent) {
      // Get client-side error
      errorMessage = error.error.message;
    } else {
      // Get server-side error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    window.alert(errorMessage);
    return throwError(errorMessage);
  }
}
