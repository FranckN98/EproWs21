package de.thbingen.epro.util;

import de.thbingen.epro.model.entity.OkrUser;
import de.thbingen.epro.model.entity.Privilege;
import de.thbingen.epro.model.entity.Role;

import java.util.Set;

public interface UserProvider {

    OkrUser provideUser();

    class ReadOnlyUserProvider implements UserProvider {
        @Override
        public OkrUser provideUser() {
            Role role = new Role("Read Only");
            role.setPrivileges(Set.of(new Privilege("read")));
            OkrUser okrUser = new OkrUser();
            okrUser.setRole(role);
            return okrUser;
        }
    }

    class ViewUsersUserProvider implements UserProvider {
        @Override
        public OkrUser provideUser() {
            Role role = new Role("View Users");
            role.setPrivileges(Set.of(new Privilege("view_users")));
            OkrUser okrUser = new OkrUser();
            okrUser.setRole(role);
            return okrUser;
        }
    }
}
