import { Component, OnInit } from '@angular/core';
import {TenantService} from '../services/tenant.service';

@Component({
  selector: 'app-tenant-admin',
  templateUrl: './tenant-admin.component.html',
  styleUrls: ['./tenant-admin.component.scss']
})
export class TenantAdminComponent implements OnInit {

  public tenant$$ = this.tenantService.tenant$$;

  constructor(private tenantService: TenantService) { }

  ngOnInit(): void {
  }

}
