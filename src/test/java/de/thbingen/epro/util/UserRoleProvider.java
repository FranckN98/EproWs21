package de.thbingen.epro.util;

import de.thbingen.epro.model.entity.OkrUser;
import de.thbingen.epro.model.entity.Privilege;
import de.thbingen.epro.model.entity.Role;

import java.util.Set;

public interface UserRoleProvider {

    OkrUser provideUser();

    class ReadOnlyUserRoleProvider implements UserRoleProvider {
        @Override
        public OkrUser provideUser() {
            Role role = new Role("Read Only");
            role.setPrivileges(Set.of(new Privilege("read")));
            OkrUser okrUser = new OkrUser();
            okrUser.setRole(role);
            return okrUser;
        }
    }

    class ViewUsersUserRoleProvider implements UserRoleProvider {
        @Override
        public OkrUser provideUser() {
            Role role = new Role("View Users");
            role.setPrivileges(Set.of(new Privilege("view_users")));
            OkrUser okrUser = new OkrUser();
            okrUser.setRole(role);
            return okrUser;
        }
    }

    public class AccessPrivilegesProvider implements UserRoleProvider {
        @Override
        public OkrUser provideUser() {
            Role role = new Role("Access Privileges");
            role.setPrivileges(Set.of(new Privilege("access_privileges")));
            OkrUser okrUser = new OkrUser();
            okrUser.setRole(role);
            return okrUser;
        }
    }
}
