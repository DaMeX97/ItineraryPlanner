import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import {FormGroup} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { City } from 'src/app/models/city';
import { InfoModalComponent } from 'src/app/shared/uikit/info-modal/info-modal.component';
import * as moment from 'moment';
import { SearchService } from 'src/app/services/search.service';
import { TouristAttraction } from 'src/app/models/touristAttraction';
import { TravelItinerary } from 'src/app/models/travelItinerary';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { TravelMovement } from 'src/app/models/travelMovement';
import { AddAttractionModalComponent } from './add-attraction-modal/add-attraction-modal.component';
import { JwtService } from 'src/app/services/jwt.service';
import { UserService } from 'src/app/services/user.service';
import { ActivatedRoute } from '@angular/router';
import { ItineraryService } from 'src/app/services/itinerary.service';
import { DeviceDetectorService } from 'ngx-device-detector';
import { ToastrService } from 'ngx-toastr';
import { DateUtils } from 'src/app/utils/dateUtils';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
  providers: [SearchService, ItineraryService]
})
export class SearchComponent implements OnInit {
  logged: boolean = false;

  city: City | null = null;
  
  data: TouristAttraction[] = [];
  itinerary: TravelItinerary = {} as TravelItinerary;

  startTime: string = "08:00";
  endTime: string = "16:00";

  breakMinutes: number = 10;

  hoursPerDay: number = 8;
  numberOfDays: number = 0;

  startDay: moment.Moment = moment();
  endDay: moment.Moment = moment();

  travelItinerary = TravelItinerary;

  constructor(private toastr: ToastrService, public dialog: MatDialog, private searchService: SearchService, private itineraryService: ItineraryService, private route: ActivatedRoute, private deviceService: DeviceDetectorService) { }

  ngOnInit(): void {
    this.travelItinerary.searchDone = false;

    this.logged = JwtService.isLogged();
    TravelItinerary.setSearchService(this.searchService);
    TravelItinerary.setToastrService(this.toastr);

    this.city = null;
    this.data = [];
    this.hoursPerDay = 8;
    this.numberOfDays = 0;
    this.startDay = moment();
    this.endDay = moment();

    
    if(this.route.snapshot.queryParamMap.has('id')) {
      // Load a saved itinerary
      this.loadItinerary(this.route.snapshot.queryParamMap.get('id'));
    }
  }

  isMobile() {
    return this.deviceService.isMobile();
  }

  loadItinerary(idString: string | null) {
    if(idString === null) {
      return;
    }
    const id = Number(idString);

    if(id === NaN) {
      return;
    }

    this.itineraryService.getItinerary(id).subscribe((data) => {
      let touristAttractionData: TouristAttraction[] = [];
      data.days.forEach((elem) => {
        touristAttractionData = touristAttractionData.concat(elem.attractions);
      });

      this.data = touristAttractionData;
      this.hoursPerDay = data.hoursPerDay;
      this.numberOfDays = data.days.length;
      this.startDay = moment(data.startDate);
      this.endDay = this.startDay.clone().add(this.numberOfDays, 'days');

      this.startTime = this.startDay.format("HH:mm");
      this.endTime = this.startDay.clone().add(this.hoursPerDay, 'hours').format("HH:mm");

      this.city = data.city;

      this.breakMinutes = data.breakMinutes;

      this.itinerary = new TravelItinerary(touristAttractionData, data.hoursPerDay, data.days.length, moment(data.startDate), data.city, this.breakMinutes);
      this.itinerary.id = data.id;

      
      TravelItinerary.countOfUpdate++;
      this.itinerary.updateTravelItinerary();
    }, (err) => {
      this.toastr.error('An error has occured while loading the itinerary.');
    })

  }

  getDateFromDayIndex(dayIndex: number) {
    return this.itinerary.getDateString(dayIndex);
  }

  getStartTimeForAttraction(dayIndex: number, index: number, day: TravelMovement[]) {
    const date = this.itinerary.getStartAttractionTime(dayIndex, index, day);

    return date.format("HH:mm");
  }

  getEndTimeForAttraction(dayIndex: number, index: number, day: TravelMovement[]) {
    const date = this.itinerary.getEndAttractionTime(dayIndex, index, day);

    return date.format("HH:mm");
  }

  addNewAttraction(dayIndex: number) {
    const dialogRef = this.dialog.open(AddAttractionModalComponent, {
      data: {
        city: this.city,
        avoidIds: this.itinerary.getAttractions()
      },
      panelClass: ['full-screen-modal-mobile']
    });
    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        const attraction: TouristAttraction = result;

        this.itinerary.addNewAttraction(attraction, dayIndex);
      }
    })
  }


  drop(event: CdkDragDrop<TravelMovement[]>, dayIndex: number){
    if(event.previousContainer == event.container){
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);

      if(event.previousIndex !== event.currentIndex) {
        if(event.previousIndex === 0 && event.currentIndex === (event.container.data.length - 1)) {
          // from first to last

          this.itinerary.deletePrevTime(0, dayIndex); // update new first
          this.itinerary.updateTravelTimeMovement(event.currentIndex - 1, event.currentIndex, dayIndex);
        }
        else if(event.previousIndex === 0 && event.currentIndex < (event.container.data.length - 1)) {
          // from first to inner
          this.itinerary.deletePrevTime(0, dayIndex); // update new first
          this.itinerary.updateTravelTimeMovement(event.currentIndex - 1, event.currentIndex, dayIndex);
          this.itinerary.updateTravelTimeMovement(event.currentIndex, event.currentIndex + 1, dayIndex);
        }
        else if(event.previousIndex === (event.container.data.length - 1) && event.currentIndex === 0) {
          // from last to first

          this.itinerary.deletePrevTime(0, dayIndex); // update new first
          this.itinerary.updateTravelTimeMovement(0, 1, dayIndex);
        }
        else if(event.previousIndex < (event.container.data.length - 1) && event.currentIndex === 0) {
          // from inner to first
          this.itinerary.deletePrevTime(0, dayIndex); // update new first
          this.itinerary.updateTravelTimeMovement(0, 1, dayIndex);
          this.itinerary.updateTravelTimeMovement(event.previousIndex, event.previousIndex + 1, dayIndex);
        }
        else {
          // from inner to inner
          this.itinerary.updateTravelTimeMovement(event.previousIndex - 1, event.previousIndex, dayIndex);
          this.itinerary.updateTravelTimeMovement(event.previousIndex, event.previousIndex + 1, dayIndex);
          this.itinerary.updateTravelTimeMovement(event.currentIndex - 1, event.currentIndex, dayIndex);
          this.itinerary.updateTravelTimeMovement(event.currentIndex, event.currentIndex + 1, dayIndex);
        }
      }
    } else if(!this.isMobile()) {
      transferArrayItem(event.previousContainer.data, event.container.data, event.previousIndex, event.currentIndex);
      //console.log("PreviusIndex:" , event.previousIndex + " / " + event.previousContainer.data.length);
      //console.log("CurrentIndex:" , event.currentIndex + " / " + event.container.data.length);

      if(event.previousIndex === 0 && event.currentIndex === (event.container.data.length - 1)) {
        // from first to last
        this.itinerary.deletePrevTime(0, dayIndex); // update new first
        this.itinerary.updateTravelTimeMovement(event.currentIndex - 1, event.currentIndex, dayIndex);
      }
      else if(event.previousIndex === 0 && event.currentIndex < (event.container.data.length - 1)) {
        // from first to inner
        this.itinerary.deletePrevTime(0, dayIndex); // update new first
        this.itinerary.updateTravelTimeMovement(event.currentIndex - 1, event.currentIndex, dayIndex);
        this.itinerary.updateTravelTimeMovement(event.currentIndex, event.currentIndex + 1, dayIndex);
      }
      else if(event.previousIndex === (event.previousContainer.data.length) && event.currentIndex === 0) {
        // from last to first (element already removed)
        this.itinerary.deletePrevTime(0, dayIndex); // update new first
        this.itinerary.updateTravelTimeMovement(0, 1, dayIndex);
      }
      else if(event.previousIndex < (event.previousContainer.data.length) && event.currentIndex === 0) {
        // from inner to first (element already removed)
        this.itinerary.deletePrevTime(0, dayIndex); // update new first
        this.itinerary.updateTravelTimeMovement(0, 1, dayIndex);
        this.itinerary.updateTravelTimeMovement(event.previousIndex - 1, event.previousIndex, dayIndex);
      }
      else {
        // from inner to inner
        this.itinerary.updateTravelTimeMovement(event.previousIndex - 1, event.previousIndex, dayIndex);
        this.itinerary.updateTravelTimeMovement(event.currentIndex - 1, event.currentIndex, dayIndex);
        this.itinerary.updateTravelTimeMovement(event.currentIndex, event.currentIndex + 1, dayIndex);
      }
    }
  }

  search = (city: City, range: FormGroup) => {
    TravelItinerary.searchDone = false;

    this.city = city;

    const startDate = range.controls.start.value;
    const endDate = range.controls.end.value;

    if(city === null && startDate === null && endDate === null) {
      // No info
      this.dialog.open(InfoModalComponent, {
        data: {
          "title": "Compila i campi",
          "message": "Tutti i campi sono obbligatori e vanno compilati. Per favore, compilali e riprova.",
          "showDeny": false,
          "acceptButtonText": "OK"
        }
      });

      return;
    }

    this.startDay = moment.parseZone(startDate);
    this.endDay = moment.parseZone(endDate);

    this.numberOfDays = this.endDay.diff(this.startDay, 'days') + 1;

    this.startDay.set({
      hour: 8,
      minute: 0,
      second: 0,
      millisecond: 0
    });

    const startDateString: string = this.startDay.format("YYYY-MM-DD");
    const endDateString: string = this.endDay.format("YYYY-MM-DD");

    this.searchService.search(city, startDateString, endDateString).subscribe((data: TouristAttraction[]) => {
      this.data = data;

      // call updateTravelItinerary
      TravelItinerary.countOfUpdate++;
      this.getTravelItinerary();
    });
  }

  getTravelItinerary = () => {
    if(this.city !== null) {
      this.startTime = this.startDay.format("HH:mm");
      this.endTime = this.startDay.clone().add(this.hoursPerDay, 'hours').format("HH:mm");

      this.itinerary = new TravelItinerary(this.data, this.hoursPerDay, this.numberOfDays, this.startDay, this.city, this.breakMinutes);
      this.itinerary.updateTravelItinerary();
    }
  }

  saveItinerary = () => {
    if(!this.itinerary.id) {
      // save new itinerary
      this.itineraryService.saveItinerary(this.itinerary.getTravelItineraryRequest()).subscribe((res) => {
        this.toastr.success('Itinerary saved correctly.');

        this.itinerary.id = res.id;
      }, (err) => {
        this.toastr.error('An error has occured while saving the itinerary.');
      });
    }
    else {
      // update itinerary
      this.itineraryService.updateItinerary(this.itinerary.getTravelItineraryRequest()).subscribe((res) => {
        this.toastr.success('Itinerary updated correctly.');
      }, (err) => {
        this.toastr.error('An error has occured while updating the itinerary.');
      })
    }
  }

  trackByIndex(index: number, el:any): number {
    return index;
  }

  removeAttraction(movementIndex: number, dayIndex: number) {
    this.itinerary.removeAttraction(movementIndex, dayIndex);
  }

  startTimeChange(time: string) {
    this.startTime = time;

    this.updateItineraryParams();
  }

  endTimeChange(time: string) {
    this.endTime = time;

    this.updateItineraryParams();
  }

  updateItineraryParams() {
    const itineraryStartTime = moment(this.itinerary.startDay);
    itineraryStartTime.hours(Number(this.startTime.split(":")[0]));
    this.itinerary.startDay = itineraryStartTime;

    const itineraryEndTime = moment(this.itinerary.startDay);
    itineraryEndTime.hours(Number(this.endTime.split(":")[0]));

    const numberOfHours = itineraryEndTime.diff(itineraryStartTime, 'hours');

    this.itinerary.hoursPerDay = numberOfHours;

    this.itinerary.updateTravelItinerary();
  }

  probablyVisited(itinerary: TravelItinerary) : boolean{
    const lastDay: moment.Moment = DateUtils.getDateFromMs(itinerary.startDay, itinerary.days.length - 1);
    const currentDate: moment.Moment = moment();

    return currentDate.isSameOrAfter(lastDay);
  }
}
