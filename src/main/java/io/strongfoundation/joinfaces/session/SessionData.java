package io.strongfoundation.joinfaces.session;

import java.io.Serializable;
import java.time.LocalDateTime;

public record SessionData(
                String username,
                String role,
                LocalDateTime beginAT,
                LocalDateTime expirationAT,
                String uuid)
                implements Serializable {

}
