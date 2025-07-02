package com.protrack.protrack_be.service.impl;
    
import com.protrack.protrack_be.model.Account;
import com.protrack.protrack_be.model.CustomUserDetails;
import com.protrack.protrack_be.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find an account with: " + email));
        return new CustomUserDetails(account);
    }
}

