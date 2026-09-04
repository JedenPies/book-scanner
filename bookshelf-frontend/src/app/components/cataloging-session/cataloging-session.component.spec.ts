import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CatalogingSessionComponent } from './cataloging-session.component';

describe('Editor', () => {
  let component: CatalogingSessionComponent;
  let fixture: ComponentFixture<CatalogingSessionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CatalogingSessionComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(CatalogingSessionComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
