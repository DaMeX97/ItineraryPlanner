import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { ToastrService } from 'ngx-toastr';
import { TravelItineraryJpa } from 'src/app/models/travelItineraryJpa';
import { ItineraryService } from 'src/app/services/itinerary.service';
import { JwtService } from 'src/app/services/jwt.service';
import { DateUtils } from 'src/app/utils/dateUtils';
import { InfoModalComponent } from '../info-modal/info-modal.component';

@Component({
  selector: 'app-itinerary-list',
  templateUrl: './itinerary-list.component.html',
  styleUrls: ['./itinerary-list.component.scss'],
  providers: [ItineraryService]
})
export class ItineraryListComponent implements OnInit {

  @Input()
  itineraries: TravelItineraryJpa[] = [];

  @Input()
  userId : number | null = null;

  dateUtils = DateUtils;

  jwtService = JwtService;

  deleteLoader: boolean = false;
  visitedLoader: boolean = false;

  constructor(private itineraryService: ItineraryService, private router: Router, public dialog: MatDialog, private toastr: ToastrService) { }

  ngOnInit(): void {
  }

  probablyVisited(itinerary: TravelItineraryJpa) : boolean{
    const lastDay: moment.Moment = DateUtils.getDateFromMs(itinerary.startDate, itinerary.days.length - 1);
    const currentDate: moment.Moment = moment();

    return currentDate.isSameOrAfter(lastDay);
  }

  modify(itinerary: TravelItineraryJpa) {
    this.router.navigateByUrl("/search?id=" + itinerary.id);
  }

  deleteItinerary(itinerary: TravelItineraryJpa) {
    return () => {
      const dialogRef = this.dialog.open(InfoModalComponent, {
        data: {
          "title": "Are you sure that you want to delete the selected itinerary?",
          "message": "This action can't be undone. ",
          "showDeny": true,
          "acceptButtonText": "Yes",
          "denyButtonText": "No"
        }
      });
  
      
      dialogRef.afterClosed().subscribe(result => {
        if(result) {
          this.deleteLoader = true;
          this.itineraryService.deleteItinerary(itinerary).subscribe((data) => {
            this.deleteLoader = false;

            this.toastr.success('Itinerary deleted');
      
            this.itineraries = this.itineraries.filter((elem) => {
              return elem.id !== itinerary.id;
            });
          }, (err) => {
            this.deleteLoader = false;
            this.toastr.error('An error has occured while deleting the itinerary');
          })
        }
      });
    }
  }

  confirmItinerary(itinerary: TravelItineraryJpa, index: number) {
    return () => {
      const dialogRef = this.dialog.open(InfoModalComponent, {
        data: {
          "title": "Are you sure that you want to mark as visited this itinerary?",
          "message": "",
          "showDeny": true,
          "acceptButtonText": "Yes",
          "denyButtonText": "No"
        }
      });

      dialogRef.afterClosed().subscribe(result => {
        if(result) {
          this.visitedLoader = true;
          this.itineraryService.confirmItinerary(itinerary).subscribe((data) => {
            this.visitedLoader = false;

            this.itineraries[index].status = "VISITED";

            this.toastr.success('Itinerary marked as visisted.');
          }, (err) => {
            this.visitedLoader = false;

            this.toastr.error("An error occured while marking as visisted the itinerary.");
          })
        }
      });
    }
  }
}
