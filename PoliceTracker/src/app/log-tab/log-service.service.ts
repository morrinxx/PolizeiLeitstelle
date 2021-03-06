import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { retry, catchError } from "rxjs/operators";

@Injectable({
  providedIn: "root",
})
export class LogServiceService {
  constructor(private httpClient: HttpClient) {}
  baseUrl = "http://localhost:8080";
  httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json",
    }),
  };

  getOperations(): Observable<any> {
    return this.httpClient
      .get(this.baseUrl + "/meldung")
      .pipe(retry(1), catchError(this.handleError));
  }

  getReportsOfOperation(operation): Observable<any> {
    let params = new HttpParams().set("operationName", operation);
    return this.httpClient
      .get(this.baseUrl + "/statuse/" + operation)
      .pipe(retry(1), catchError(this.handleError));
  }

  getIdForOperation(): Observable<any> {
    return this.httpClient
      .get(this.baseUrl + "/idealid")
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
