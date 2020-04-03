import {Component, OnInit, ViewChild} from '@angular/core';
import {BackendClient} from '../../models/backend-client';
import {MatTableDataSource} from '@angular/material/table';
import {TenantClient} from '../../models/tenant-client';
import {MatSort} from '@angular/material/sort';
import {ApiService} from '../../services/api.service';
import {TenantService} from '../../services/tenant.service';
import {DiaryEntryDto} from '../../models/diary-entry';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-clients-list',
  templateUrl: './clients-list.component.html',
  styleUrls: ['./clients-list.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class ClientsListComponent implements OnInit {
  public displayedColumns: string[] = ['surename', 'firstname', 'phone', 'zipCode', 'infected', 'monitoringStatus'];
  public expandedElement: BackendClient | null;
  public dataSource = new MatTableDataSource<TenantClient>();

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private apiService: ApiService,
              private tenantService: TenantService) { }

  ngOnInit(): void {
    this.apiService.getReport(this.tenantService.tenant$$.getValue().tenantId)
      .subscribe((val: Array<TenantClient>) => { this.dataSource.data = val; });
    this.dataSource.sort = this.sort;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  toStringSymptoms(entry: DiaryEntryDto): string {
    return entry.symptoms.map(s => s.name).join(', ');
  }
}
