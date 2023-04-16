package org.techtowm.retrofit2.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.techtowm.retrofit2.Data.UserDTO;
import org.techtowm.retrofit2.Data.UserData;
import org.techtowm.retrofit2.ui.UserActivity;

import java.util.List;

public class LoginViewModel extends ViewModel {
   MutableLiveData<Integer> progressMutableData = new MutableLiveData<>();
   MutableLiveData<String> loginResultMutableData = new MutableLiveData<>();
   MutableLiveData<Integer> usertokenMutableData = new MutableLiveData<>();
   LoginRepository loginRepository;
   LoginModel loginModel;

   public LoginViewModel() {
      progressMutableData.postValue(View.INVISIBLE);
      loginResultMutableData.postValue("");
      usertokenMutableData.postValue(0);
      loginModel = new LoginModel();
      loginRepository = new LoginRepository();
   }

   public void login(String id, String password) {
      progressMutableData.postValue(View.VISIBLE);
      loginRepository.loginRemote(id, password, new LoginRepository.ILoginResponse() {

         @Override
         public void onResponse(UserDTO userDTO) {

            progressMutableData.postValue(View.INVISIBLE);

            if( userDTO.getToken() == 1 ) {
               usertokenMutableData.postValue(1);
               loginResultMutableData.postValue("");
               loginModel.setId(userDTO.getID());
               loginModel.setIdx(userDTO.getIdx());
               loginModel.setName(userDTO.getName());
            }
            else {
               loginResultMutableData.postValue("아이디 또는 비밀번호를 확인해주십시오.");
            }
         }

         @Override
         public void onFailure(Throwable t) {
            progressMutableData.postValue(View.INVISIBLE);
            loginResultMutableData.postValue("아이디 또는 비밀번호를 확인해주십시오.");
         }
      });
   }

   public LiveData<Integer> getProgress() {
      return progressMutableData;
   }

   public LiveData<String> getLoginResult() {
      return loginResultMutableData;
   }

   public LiveData<Integer> getToken() {
      return usertokenMutableData;
   }

   public LoginModel getLoginModel() {
      return loginModel;
   }
}
