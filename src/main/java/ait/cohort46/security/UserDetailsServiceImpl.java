package ait.cohort46.security;

import ait.cohort46.accounting.dao.UserAccountRepository;
import ait.cohort46.accounting.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserAccountRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = repository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
        String[] roles = userAccount.getRoles().stream().
                map(r -> "ROLE_" + r.name()).
                toArray(String[]::new);
        return new User(username, userAccount.getPassword(), AuthorityUtils.createAuthorityList(roles));
    }
}
