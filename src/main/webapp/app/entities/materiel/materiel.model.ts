import { ITache } from 'app/entities/tache/tache.model';

export interface IMateriel {
  id?: number;
  libelle?: string | null;
  quantityUse?: number | null;
  quantityStock?: number | null;
  tache?: ITache | null;
}

export class Materiel implements IMateriel {
  constructor(
    public id?: number,
    public libelle?: string | null,
    public quantityUse?: number | null,
    public quantityStock?: number | null,
    public tache?: ITache | null
  ) {}
}

export function getMaterielIdentifier(materiel: IMateriel): number | undefined {
  return materiel.id;
}
