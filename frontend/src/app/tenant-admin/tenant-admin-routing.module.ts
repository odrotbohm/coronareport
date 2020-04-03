import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {DashboardComponent} from './dashboard/dashboard.component';
import {IsTenantAdminGuard} from '../guards/is-tenant-admin.guard';
import {LoginComponent} from './login/login.component';
import {IsNotTenantAdminGuard} from '../guards/is-not-tenant-admin.guard';
import {ClientsListComponent} from './clients-list/clients-list.component';
import {ClientsCreateComponent} from './clients-create/clients-create.component';
import {TenantAdminComponent} from './tenant-admin.component';


const routes: Routes = [
  {
    path: '',
    component: TenantAdminComponent,
    children: [
      {
        path: 'login',
        component: LoginComponent,
        canActivate: [IsNotTenantAdminGuard]
      },
      {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [IsTenantAdminGuard],
      },
      {
        path: 'clients',
        canActivate: [IsTenantAdminGuard],
        children: [
          {
            path: 'list',
            component: ClientsListComponent
          },
          {
            path: 'new',
            component: ClientsCreateComponent
          },
          {
            path: '',
            redirectTo: 'list',
            pathMatch: 'full'
          }
        ]
      },
      {
        path: '',
        redirectTo: 'clients'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TenantAdminRoutingModule {
}
