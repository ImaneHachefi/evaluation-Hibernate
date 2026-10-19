package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.EmployeTachePK;

public class EmployeTacheService extends AbstractService<EmployeTache> {

    public EmployeTacheService() { super(EmployeTache.class); }

    public EmployeTache getById(int employeId, int tacheId) {
        return getById(new EmployeTachePK(employeId, tacheId));
    }
}
