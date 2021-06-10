import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { City } from 'src/app/models/city';
import { TouristAttraction } from 'src/app/models/touristAttraction';
import { SearchService } from 'src/app/services/search.service';

@Component({
  selector: 'app-add-attraction-modal',
  templateUrl: './add-attraction-modal.component.html',
  styleUrls: ['./add-attraction-modal.component.scss'],
  providers: [SearchService]
})
export class AddAttractionModalComponent implements OnInit {

  name: string = "";

  loading: boolean = false;

  city: City;

  attractions: TouristAttraction[] = [];
  avoidIds: TouristAttraction[];

  constructor(private toastr: ToastrService, private searchService: SearchService, private dialogRef: MatDialogRef<AddAttractionModalComponent>, @Inject(MAT_DIALOG_DATA) private data: SearchAttractionDialogData) { 
    this.city = data.city;
    this.avoidIds = data.avoidIds;
  }

  ngOnInit(): void {
    if(this.city !== null) {
      this.loadSuggerimenti();
    }
  }

  loadSuggerimenti() : void {
    this.attractions = [];
    this.loading = true;

    this.searchService.getSuggerimenti(this.city, this.avoidIds).subscribe((data: TouristAttraction[]) => {
      this.loading = false;
      this.attractions = data;
    },
    (err) => {
      this.toastr.error('An error has occured while load the suggestions.');
      this.loading = false;
    })
  }

  onAccept(attraction: TouristAttraction) : void {
    this.dialogRef.close(attraction);
  }

  onDeny() : void {
    this.dialogRef.close(null);
  }
  
  searchAttraction() : void {
    this.attractions = [];
    this.loading = true;

    this.searchService.getSuggerimenti(this.city, this.avoidIds, this.name).subscribe((data: TouristAttraction[]) => {
      this.loading = false;
      this.attractions = data;
    },
    (err) => {
      this.toastr.error('An error has occured while load the suggestions with the name.');
      this.loading = false;
    })
  }

}

export interface SearchAttractionDialogData {
  city: City;
  avoidIds: TouristAttraction[]
}