package voting.controller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import voting.dto.admin.AdminRepresentation;
import voting.dto.countyrep.CountyRepFullTerritorialsDTO;
import voting.dto.countyrep.CountyRepresentativeDTO;
import voting.repository.AdminRepository;
import voting.service.CountyRepService;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by andrius on 3/6/17.
 */

@Controller
@RequestMapping("/api/auth/")
public class AuthController {

    private final CountyRepService countyRepService;
    private final AdminRepository adminRepository;

    @Autowired
    public AuthController(CountyRepService countyRepService, AdminRepository adminRepository) {
        this.countyRepService = countyRepService;
        this.adminRepository = adminRepository;
    }

    @GetMapping(path = "logout")
    @ResponseBody
    public boolean loginLogout() { return true; }

    @PostMapping(path = "authorities")
    @ResponseBody
    public Object checkSecuredAuthorities() {
        boolean representative = isContextContainsRole("ROLE_REPRESENTATIVE");
        boolean admin = isContextContainsRole("ROLE_ADMIN");

        if (representative) {
            return "REPRESENTATIVE";
        } else if (admin) {
            return "ADMIN";
        } else {
            return null;
        }
    }

    @PostMapping(path = "username")
    @ResponseBody
    public String getSecuredUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (userDetails instanceof UserDetails) ? ((UserDetails) userDetails).getUsername() : "";
    }

    @PostMapping(path = "role")
    @ResponseBody
    public boolean getSecuredRole(@RequestParam("role") String role) {
        return isContextContainsRole(role);
    }

    @PostMapping(path = "principal")
    @ResponseBody
    public Object getSecuredPrincipal() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (userDetails instanceof UserDetails) ? ((UserDetails) userDetails).getUsername() : null;
        boolean representative = isContextContainsRole("ROLE_REPRESENTATIVE");
        boolean admin = isContextContainsRole("ROLE_ADMIN");

        if (representative) {
            return new CountyRepFullTerritorialsDTO(countyRepService.getCountyRepByUsername(username));
        } else if (admin) {
            return new AdminRepresentation(adminRepository.findOneByUsername(username));
        } else {
            return "";
        }
    }

    private boolean isContextContainsRole(String role) {
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()).contains(role);
    }

}
