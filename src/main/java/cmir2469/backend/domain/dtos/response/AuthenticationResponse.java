package cmir2469.backend.domain.dtos.response;

public class AuthenticationResponse {

    private  String jwt;

    public AuthenticationResponse() {
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "jwt='" + jwt + '\'' +
                '}';
    }
}
