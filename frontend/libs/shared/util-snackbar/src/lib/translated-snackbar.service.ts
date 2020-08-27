import { TranslateService } from '@ngx-translate/core';
import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class TranslatedSnackbarService {
  duration = 3000;

  constructor(public snackbar: MatSnackBar, private translate: TranslateService) {}

  confirm(messageKey: string): Observable<MatSnackBarRef<SimpleSnackBar>> {
    const config = new MatSnackBarConfig();
    config.panelClass = ['light-primary-color'];
    return this.translate.get(messageKey).pipe(map((result: string) => this.snackbar.open(result, 'Ok', config)));
  }

  success(messageKey: string, params?: { value: string }): Observable<MatSnackBarRef<SimpleSnackBar>> {
    const config = new MatSnackBarConfig();
    config.panelClass = ['background-green'];
    config.duration = this.duration;
    return this.translate.get(messageKey, params).pipe(
      tap((result) => console.log(result)),
      map((result: string) => this.snackbar.open(result, null, config))
    );
  }

  error(messageKey: string): Observable<MatSnackBarRef<SimpleSnackBar>> {
    const config = new MatSnackBarConfig();
    config.panelClass = ['background-red'];
    return this.translate.get(messageKey).pipe(map((result: string) => this.snackbar.open(result, 'x', config)));
  }

  warning(messageKey: string): Observable<MatSnackBarRef<SimpleSnackBar>> {
    const config = new MatSnackBarConfig();
    config.panelClass = ['background-orange'];
    config.duration = this.duration;
    return this.translate.get(messageKey).pipe(map((result: string) => this.snackbar.open(result, null, config)));
  }

  message(messageKey: string) {
    const config = new MatSnackBarConfig();
    config.duration = this.duration;
    config.panelClass = ['light-primary-color'];
    return this.translate.get(messageKey).pipe(map((result: string) => this.snackbar.open(result, null, config)));
  }
}