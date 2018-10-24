package de.tcp.tcpdemo.app;

import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Lam, Le (msg systems ag) 2018
 */
@Service
public class SessionService {

    @Getter
    private Set<String> connections;

    @PostConstruct
    private void init() {
         connections = Collections.synchronizedSet(new HashSet<>());
    }
}