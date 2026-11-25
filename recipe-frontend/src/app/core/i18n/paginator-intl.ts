import { Injectable } from '@angular/core';
import { MatPaginatorIntl } from '@angular/material/paginator';


@Injectable()
export class SerbianPaginatorIntl extends MatPaginatorIntl {
  override itemsPerPageLabel = 'Po stranici:';
  override nextPageLabel = 'Sledeća stranica';
  override previousPageLabel = 'Prethodna stranica';
  override firstPageLabel = 'Prva stranica';
  override lastPageLabel = 'Poslednja stranica';

  override getRangeLabel = (page: number, pageSize: number, length: number): string => {
    if (length === 0) {
      return '0 od 0';
    }
    const start = page * pageSize + 1;
    const end = Math.min((page + 1) * pageSize, length);
    return `${start} – ${end} od ${length}`;
  };
}
