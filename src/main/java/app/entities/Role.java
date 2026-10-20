package app.entities;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {
    USER,
    ADMIN,
    ANYONE
}
