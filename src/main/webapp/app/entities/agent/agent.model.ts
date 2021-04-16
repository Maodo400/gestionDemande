import { IUser } from 'app/entities/user/user.model';
import { ITache } from 'app/entities/tache/tache.model';

export interface IAgent {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  tel?: string | null;
  fonction?: string | null;
  user?: IUser | null;
  taches?: ITache[] | null;
}

export class Agent implements IAgent {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public tel?: string | null,
    public fonction?: string | null,
    public user?: IUser | null,
    public taches?: ITache[] | null
  ) {}
}

export function getAgentIdentifier(agent: IAgent): number | undefined {
  return agent.id;
}
