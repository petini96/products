package br.com.roboticsmind.products.services;

import br.com.roboticsmind.products.dto.admin.RoleDTO;
import br.com.roboticsmind.products.dto.admin.UserDTO;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.NotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeycloakAdminService {

    @Autowired
    private Keycloak keycloak;

    @Value("${keycloak.admin.realm}")
    private String realm;

    // ID do client cujos papéis queremos gerenciar (ex: 'quasar-app')
    @Value("${keycloak.admin.target-client-id}")
    private String targetClientId;

    public List<UserDTO> listUsers() {
        return getRealmResource().users().list().stream()
                .map(this::toUserDTO)
                .collect(Collectors.toList());
    }

    public List<RoleDTO> listAvailableClientRoles() {
        ClientResource clientResource = getClientResource();
        if (clientResource == null) {
            return Collections.emptyList();
        }
        return clientResource.roles().list().stream()
                .map(this::toRoleDTO)
                .collect(Collectors.toList());
    }

    public List<RoleDTO> getUserClientRoles(String userId) {
        ClientResource clientResource = getClientResource();
        if (clientResource == null) {
            return Collections.emptyList();
        }
        UserResource userResource = getRealmResource().users().get(userId);
        return userResource.roles().clientLevel(clientResource.toRepresentation().getId()).listEffective().stream()
                .map(this::toRoleDTO)
                .collect(Collectors.toList());
    }

    public void updateUserClientRoles(String userId, List<RoleDTO> rolesToAssign) {
        ClientResource clientResource = getClientResource();
        if (clientResource == null) {
            throw new IllegalStateException("Client '" + targetClientId + "' não encontrado.");
        }
        UserResource userResource = getRealmResource().users().get(userId);

        // 1. Pega todos os papéis do client disponíveis
        List<RoleRepresentation> availableRoles = clientResource.roles().list();

        // 2. Pega os papéis que o usuário JÁ POSSUI
        List<RoleRepresentation> currentUserRoles = userResource.roles()
                .clientLevel(clientResource.toRepresentation().getId()).listEffective();

        // 3. Converte os DTOs recebidos para RoleRepresentation
        List<String> rolesToAssignNames = rolesToAssign.stream().map(RoleDTO::name).toList();
        List<RoleRepresentation> targetRoles = availableRoles.stream()
                .filter(role -> rolesToAssignNames.contains(role.getName()))
                .collect(Collectors.toList());

        // 4. Calcula o que precisa ser REMOVIDO
        List<RoleRepresentation> rolesToRemove = currentUserRoles.stream()
                .filter(currentRole -> targetRoles.stream().noneMatch(targetRole -> targetRole.getId().equals(currentRole.getId())))
                .collect(Collectors.toList());

        // 5. Calcula o que precisa ser ADICIONADO
        List<RoleRepresentation> rolesToAdd = targetRoles.stream()
                .filter(targetRole -> currentUserRoles.stream().noneMatch(currentRole -> currentRole.getId().equals(targetRole.getId())))
                .collect(Collectors.toList());

        // 6. Executa as operações
        if (!rolesToRemove.isEmpty()) {
            userResource.roles().clientLevel(clientResource.toRepresentation().getId()).remove(rolesToRemove);
        }
        if (!rolesToAdd.isEmpty()) {
            userResource.roles().clientLevel(clientResource.toRepresentation().getId()).add(rolesToAdd);
        }
    }

    // --- Métodos Auxiliares ---

    private RealmResource getRealmResource() {
        return keycloak.realm(realm);
    }

    private ClientResource getClientResource() {
        ClientRepresentation clientRep = getRealmResource().clients().findByClientId(targetClientId).stream()
                .findFirst().orElse(null);
        if (clientRep == null) {
            return null;
        }
        return getRealmResource().clients().get(clientRep.getId());
    }

    private UserDTO toUserDTO(UserRepresentation userRep) {
        return new UserDTO(
                userRep.getId(),
                userRep.getUsername(),
                userRep.getEmail(),
                userRep.getFirstName(),
                userRep.getLastName()
        );
    }

    private RoleDTO toRoleDTO(RoleRepresentation roleRep) {
        return new RoleDTO(
                roleRep.getId(),
                roleRep.getName(),
                roleRep.getDescription()
        );
    }
}