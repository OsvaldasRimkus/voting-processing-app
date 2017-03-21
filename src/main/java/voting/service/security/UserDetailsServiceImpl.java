package voting.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import voting.model.CountyRep;
import voting.service.CountyRepService;

/**
 * Created by andrius on 3/6/17.
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CountyRepService countyRepService;

    @Autowired
    public UserDetailsServiceImpl(CountyRepService countyRepService) {
        this.countyRepService = countyRepService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CountyRep cr = countyRepService.getCountyRepByUsername(username);

        if (cr == null) throw new UsernameNotFoundException(String.format("Vartotojas: [%s] nerastas", username));

        return new User(
                cr.getUsername(),
                cr.getPassword_digest(),
                AuthorityUtils.createAuthorityList(cr.getRoles())
        );
    }

}
