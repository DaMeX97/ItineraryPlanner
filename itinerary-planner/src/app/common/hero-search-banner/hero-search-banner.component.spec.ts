import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeroSearchBannerComponent } from './hero-search-banner.component';

describe('HeroSearchBannerComponent', () => {
  let component: HeroSearchBannerComponent;
  let fixture: ComponentFixture<HeroSearchBannerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HeroSearchBannerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HeroSearchBannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
