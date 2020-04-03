import { TestBed } from '@angular/core/testing';

import { TenantAdminService } from './tenant-admin.service';

describe('TenantAdminService', () => {
  let service: TenantAdminService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TenantAdminService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
