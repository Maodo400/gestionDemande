import { IMateriel } from 'app/entities/materiel/materiel.model';
import { IAgent } from 'app/entities/agent/agent.model';
import { EtatTache } from 'app/entities/enumerations/etat-tache.model';

export interface ITache {
  id?: number;
  nom?: string | null;
  etat?: EtatTache | null;
  materiels?: IMateriel[] | null;
  agent?: IAgent | null;
}

export class Tache implements ITache {
  constructor(
    public id?: number,
    public nom?: string | null,
    public etat?: EtatTache | null,
    public materiels?: IMateriel[] | null,
    public agent?: IAgent | null
  ) {}
}

export function getTacheIdentifier(tache: ITache): number | undefined {
  return tache.id;
}
