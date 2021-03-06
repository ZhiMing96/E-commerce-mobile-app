import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse, HttpParameterCodec } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

const httpOptions = {
	headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})

export class ShoppingCartService {

  baseUrl: string = 'http://localhost:8080/ZhcqRetailSystem-war/Resources/ShoppingCart';

  constructor(private httpClient: HttpClient) { }


  retrieveShoppingCart(userId: number): Observable<any>
	{
		return this.httpClient.get<any>(this.baseUrl + '/retrieveShoppingCart?userId=' + userId).pipe
		(
			catchError(this.handleError)
		)
  }

  addToCart(cartId: number, productId: number, quantity: number): Observable<any>
    {
        
        return this.httpClient.post<any>(this.baseUrl + '/addToCart?cartId=' + cartId + '&productId=' + productId + "&quantity=" +quantity, null).pipe
        (
            catchError(this.handleError)
        );
    }

    updateCart(cartId: number, productId : number, quantity: number): Observable<any> {
        // var headers = new HttpHeaders();
        // headers.append('Access-Control-Allow-Origin', '*');

        return this.httpClient.post<any>(this.baseUrl + '/updateCart?cartId=' + cartId + '&productId=' + productId + "&quantity=" +quantity, null).pipe
        (
            catchError(this.handleError)
        );
    }

    removeFromCart(cartId: number, productId: number): Observable<any>
    {
        // const headers = new HttpHeaders();
        // headers.append('Access-Control-Allow-Origin', '*');
        // headers.append('Access-Control-Allow-Methods',  'GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH');
        // headers.append('Access-Control-Allow-Headers', 'origin, x-requested-with, content-type' );

        return this.httpClient.delete<any>(this.baseUrl + '?cartId=' + cartId + '&productId=' + productId).pipe
        (
            catchError(this.handleError)
        );
        // return this.httpClient.delete<any>(this.baseUrl + '?cartId=' + cartId + '&productId=' + productId).pipe
        // (
        //     catchError(this.handleError)
        // );
    }

    checkout(cartId: number): Observable<any>
    {
        // var headers = new HttpHeaders();
        // headers.append('Access-Control-Allow-Origin', '*');

        return this.httpClient.post<any>(this.baseUrl + '/checkout?cartId=' + cartId, null).pipe
        (
            catchError(this.handleError)
        );
    }

    checkoutWithPoints(cartId: number): Observable<any>
    {
        // var headers = new HttpHeaders();
        // headers.append('Access-Control-Allow-Origin', '*');

        return this.httpClient.post<any>(this.baseUrl + '/checkoutWithPoints?cartId=' + cartId, null).pipe
        (
            catchError(this.handleError)
        );
    }


    private handleError(error: HttpErrorResponse) {
        if (error.error !== null) {
            let errorMessage: string = '';

            if (error.error instanceof ErrorEvent)
            {
                errorMessage = 'An unknown error has occurred: ' + error.error.message;
            } else {
                errorMessage = 'A HTTP error has occurred: ' + `HTTP ${error.status}: ${error.error.message}`;
            }
            console.error(errorMessage);
            return throwError(errorMessage);
        }
    }
}
