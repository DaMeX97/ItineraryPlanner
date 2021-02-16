import { Component, OnInit } from '@angular/core';
import {FormGroup, FormControl} from '@angular/forms';
import { SearchService } from './../../services/search/search.service';

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrls: ['./search-page.component.sass'],
  providers: [SearchService]
})
export class SearchPageComponent implements OnInit {

  luogo : string = ""
  range = new FormGroup({
    start: new FormControl(),
    end: new FormControl()
  });

  data = [];

  constructor(private searchService : SearchService) { }

  ngOnInit(): void { }

  search = () => {
    this.searchService.getPlaces("Roma").subscribe((data: any) => {
      if(data.error) {
        // TODO
      }
      else {
        this.data = data.response;
      }
    });
  }

}
