package models.getUsers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import models.Support;
import models.UsersData;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    UsersData data;
    Support support;
}
