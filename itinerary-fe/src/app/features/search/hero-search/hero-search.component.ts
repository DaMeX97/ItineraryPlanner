import { Component, OnInit, Input, Output } from '@angular/core';

import {FormControl, FormGroup} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { City } from 'src/app/models/city';
import { CityService } from 'src/app/services/city.service';
import { InfoModalComponent } from 'src/app/shared/uikit/info-modal/info-modal.component';

@Component({
  selector: 'app-hero-search',
  templateUrl: './hero-search.component.html',
  styleUrls: ['./hero-search.component.scss'],
  providers: [CityService]
})
export class HeroSearchComponent implements OnInit {

  luogo!: City;
  range: FormGroup = new FormGroup({
    start: new FormControl(),
    end: new FormControl()
  });

  @Input()
  onSubmitCallback! : Function;

  luogoFormControl = new FormControl('', []);

  minDate: Date;

  searchOptions: City[] = [];

  luogoSelected: boolean = false;

  constructor(private cityService : CityService, public dialog: MatDialog) { 
    this.minDate = new Date();
  }

  ngOnInit(): void {
    
  }

  getSelectText(option: City) {
    return option.name + " (" + option.provinceCode + ")";
  }

  getTooltipText(option: City) {
    return option.name + " (" + option.provinceCode + ")";
  }

  loadCities = () => {
    if(this.luogoFormControl.value.length >= 3) {
      const currentSearch = this.luogoFormControl.value;
      this.cityService.getCities(currentSearch).subscribe((response: City[]) => {
        if(this.luogoFormControl.value === currentSearch) {
          this.searchOptions = response;
        }
      }, (err) => {
        // Errore generico
        this.searchOptions = [];
        const dialogRef = this.dialog.open(InfoModalComponent, {
          data: {
            "title": "Ops!",
            "message": "Qualcosa è andato storto nel cercare le città... Riprova più tardi!",
            "showDeny": false,
            "acceptButtonText": "OK"
          }
        });
      })
    }
  }

  onLuogoKeyupEvent(event: KeyboardEvent): void {
    this.luogoSelected = false;
    this.loadCities();
  }

  luogoSelectionChange = (option: City, event: any) => {
    this.luogo = option;
    this.luogoSelected = true;
  }

  onSubmit = () => {
    this.onSubmitCallback(this.luogoSelected ? this.luogo : null, this.range);
  }
}
