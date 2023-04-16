package org.techtowm.retrofit2.ui.join;

import org.techtowm.retrofit2.Data.UserDTO;
import org.techtowm.retrofit2.Retrofit.APIClient;
import org.techtowm.retrofit2.Retrofit.APIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinRepository {
    APIService apiService;
    public JoinRepository() {
        apiService = APIClient.getClient().create(APIService.class);
    }

    public void checkDuplicateRemote(IcheckResponse icheckResponse) {
        Call<List<UserDTO>> call = apiService.getData();

        call.enqueue(new Callback<List<UserDTO>>() {
            @Override
            public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                if( response.isSuccessful() ) {
                    icheckResponse.onResponse(response.body());
                }
                else {
                    icheckResponse.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<UserDTO>> call, Throwable t) {
                icheckResponse.onFailure(t);
            }
        });
    }

    public void joinRemote(String id, String password, String name, String age, IJoinResponse iJoinResponse) {
        Call<UserDTO> call = apiService.insertdata(id, password, name, age);
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if( response.isSuccessful() ) {
                    iJoinResponse.onResponse(response.body());
                }
                else {
                    iJoinResponse.onFaliure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                iJoinResponse.onFaliure(t);
            }
        });
    }

    public interface IcheckResponse {
        void onResponse(List<UserDTO> list);
        void onFailure(Throwable t);
    }

    public interface IJoinResponse {
        void onResponse(UserDTO userDTO);
        void onFaliure(Throwable t);
    }
}
