import { IUser } from 'app/entities/user/user.model';
import { EnumSexe } from 'app/entities/enumerations/enum-sexe.model';

export interface IChefService {
  id?: number;
  firstName?: string;
  lastName?: string;
  sexe?: EnumSexe | null;
  email?: string;
  tel?: string | null;
  user?: IUser | null;
}

export class ChefService implements IChefService {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public sexe?: EnumSexe | null,
    public email?: string,
    public tel?: string | null,
    public user?: IUser | null
  ) {}
}

export function getChefServiceIdentifier(chefService: IChefService): number | undefined {
  return chefService.id;
}
