import {Injectable} from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router, CanLoad, Route, UrlSegment} from '@angular/router';
import {Observable} from 'rxjs';
import {UserService} from '../services/user.service';
import {TenantService} from '../services/tenant.service';
import {map, take, tap} from 'rxjs/operators';
import {Tenant} from '../models/tenant';

@Injectable({
  providedIn: 'root'
})
export class IsNotTenantAdminGuard implements CanActivate, CanLoad {

  constructor(private tenantService: TenantService,
              private router: Router) {
  }

  canLoad(route: Route, segments: UrlSegment[]): Observable<boolean> | Promise<boolean> | boolean {
    return this.check();
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.check();
  }

  private check(): Observable<boolean> {
    return this.tenantService.tenant$$
      .pipe(
        take(1),
        map((tenant: Tenant) => tenant === null),
        tap((isNotLoggedIn) => {
          if (!isNotLoggedIn) {
            this.router.navigate(['/tenant-admin/login']);
          }
        })
      );
  }

}
