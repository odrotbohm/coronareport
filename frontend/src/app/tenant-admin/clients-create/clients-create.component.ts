import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {SnackbarService} from '../../services/snackbar.service';

@Component({
  selector: 'app-clients-create',
  templateUrl: './clients-create.component.html',
  styleUrls: ['./clients-create.component.scss']
})
export class ClientsCreateComponent implements OnInit {

  public createForm = new FormGroup({
    firstname: new FormControl(null, [
      Validators.minLength(2),
      Validators.maxLength(100),
      Validators.required
    ]),
    surename: new FormControl(null, [
      Validators.minLength(2),
      Validators.maxLength(100),
      Validators.required]),
    phone: new FormControl(null, Validators.required),
    dateTest: new FormControl(null, Validators.required),
    dateQuarantine: new FormControl(null, Validators.required),
    comment: new FormControl()
  });
  public today = new Date();

  constructor(private snackbarService: SnackbarService) {
  }

  ngOnInit(): void {
  }

  public submitForm() {
    if (this.createForm.invalid) {
      this.snackbarService.warning('Ihre Eingaben sind unvollständig');
      return;
    }

    this.snackbarService.success('Der Fall wurde angelegt');
  }

}
