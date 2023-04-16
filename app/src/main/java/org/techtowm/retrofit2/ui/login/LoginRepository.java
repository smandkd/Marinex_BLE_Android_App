package org.techtowm.retrofit2.ui.login;

import org.techtowm.retrofit2.Data.UserDTO;
import org.techtowm.retrofit2.Retrofit.APIClient;
import org.techtowm.retrofit2.Retrofit.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    public LoginRepository() {

    }

    public void loginRemote(String id, String password, ILoginResponse loginResponse) {
        APIService apiService = APIClient.getClient().create(APIService.class);
        Call<UserDTO> initiateLogin = apiService.login(id, password);

        initiateLogin.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if( response.isSuccessful() ) {
                    loginResponse.onResponse(response.body());
                }
                else {
                    loginResponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                loginResponse.onFailure(t);
            }
        });
    }



    public interface ILoginResponse {
        void onResponse(UserDTO userDTO);
        void onFailure(Throwable t);
    }
}
