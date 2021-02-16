import { Component, Input, OnInit } from '@angular/core';

import {FormGroup} from '@angular/forms';

import { Router } from '@angular/router';

@Component({
  selector: 'app-hero-search-banner',
  templateUrl: './hero-search-banner.component.html',
  styleUrls: ['./hero-search-banner.component.sass']
})
export class HeroSearchBannerComponent implements OnInit {
  @Input() 
  luogo!: string;
  @Input() 
  range!: FormGroup;
  @Input()
  onSubmit! : Function;

  constructor(private router: Router) {}

  ngOnInit(): void {
    
  }
}
